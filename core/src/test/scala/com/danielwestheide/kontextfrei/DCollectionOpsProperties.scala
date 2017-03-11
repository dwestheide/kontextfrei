package com.danielwestheide.kontextfrei

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.Inspectors

trait DCollectionOpsProperties[DColl[_]] extends BaseSpec[DColl] {

  import DCollectionOps.Imports._
  import org.scalatest.OptionValues._

  property("cartesian returns a DCollection with N * M elements") {
    forAll { (xs: List[String], ys: List[Int]) =>
      val result = unit(xs).cartesian(unit(ys)).collect()
      result.length mustEqual xs.size * ys.size
    }
  }

  property(
    "cartesian returns a DCollection where each A is paired with each B") {
    forAll { (xs: List[String], ys: List[Int]) =>
      val result = unit(xs).cartesian(unit(ys)).collect()
      Inspectors.forAll(xs) { x =>
        Inspectors.forAll(ys) { y =>
          assert(result.contains(x -> y))
        }
      }
    }
  }

  property("cogroup groups all values from A and B that have the same key") {
    forAll { (xs: List[(String, Int)], ys: List[(String, Int)]) =>
      val result = unit(xs).cogroup(unit(ys)).collect()
      result.map(_._1).toSet mustEqual (xs.map(_._1) ::: ys.map(_._1)).toSet
      Inspectors.forAll(xs) {
        case (k, v) =>
          result.find(_._1 == k).exists(_._2._1.exists(_ == v))
      }
      Inspectors.forAll(ys) {
        case (k, v) =>
          result.find(_._1 == k).exists(_._2._2.exists(_ == v))
      }
    }
  }

  property("xs.collect(pf) == xs.filter(pf.isDefinedAt).map(pf)") {
    forAll { (xs: List[String], pf: PartialFunction[String, Int]) =>
      unit(xs)
        .collect(pf)
        .collect() mustEqual unit(xs).filter(pf.isDefinedAt).map(pf).collect()
    }
  }

  property("distinct returns a DCollection of distinct elements") {
    forAll(listWithDuplicates[String]) { xs =>
      whenever(xs.distinct !== xs) {
        unit(xs).distinct().collect().sorted mustEqual unit(xs)
          .collect()
          .distinct
          .sorted
      }
    }
  }

  property("Map adheres to the first functor law") {
    forAll { xs: List[Int] =>
      unit(xs).map(identity).collect() mustEqual unit(xs).collect()
    }
  }

  property("Map adheres to the second functor law") {
    forAll { (xs: List[String], f: String => Int, g: Int => Int) =>
      unit(xs).map(f).map(g).collect() mustEqual unit(xs)
        .map(f andThen g)
        .collect()
    }
  }

  property(
    "filter returns a DCollection with only elements matching the predicate") {
    forAll { (xs: List[String], pred: String => Boolean) =>
      val filteredXs = unit(xs).filter(pred).collect()
      Inspectors.forAll(filteredXs)(pred(_) mustBe true)
    }
  }

  property(
    "filter returns a DCollection with all elements matching the predicate") {
    forAll { (xs: List[String], pred: String => Boolean) =>
      val allMatchingXs = xs.filter(pred)
      val filteredXs    = unit(xs).filter(pred).collect()
      Inspectors.forAll(allMatchingXs)(filteredXs.contains(_))
    }
  }

  property("flatMap adheres to left identity law") {
    forAll { (x: String, f: String => String) =>
      // generated functions returning a sequence are not serializable
      val g: String => Iterable[String] = s => Seq(f(s), f(s))
      unit(List(x)).flatMap(g).collect() mustEqual g(x).toArray
    }
  }

  property("flatMap adheres to right identity law") {
    forAll { x: String =>
      unit(List(x)).flatMap(s => List(s)).collect() mustEqual Array(x)
    }
  }

  property("flatMap adheres to associativity law") {
    forAll { (xs: List[String], f: String => String, g: String => String) =>
      // generated functions returning a sequence are not serializable
      val f1: String => Iterable[String] = s => Seq(f(s), f(s))
      val g1: String => Iterable[String] = s => Seq(g(s), f(s))
      val result1                        = unit(xs).flatMap(f1).flatMap(g1).collect()
      val result2                        = unit(xs).flatMap(x => f1(x).flatMap(y => g1(y))).collect()
      result1 mustEqual result2
    }
  }

  property("flatMapValues adheres to left identity law") {
    forAll { (x: (Int, String), f: String => String) =>
      // generated functions returning a sequence are not serializable
      val f1: String => Iterable[String] = s => Seq(f(s), f(s))
      unit(List(x))
        .flatMapValues(f1)
        .values
        .collect() mustEqual f1(x._2).toArray
    }
  }

  property("flatMapValues adheres to right identity law") {
    forAll { x: (Int, String) =>
      unit(List(x)).flatMapValues(s => List(s)).collect() mustEqual Array(x)
    }
  }

  property("flatMapValues adheres to associativity law") {
    forAll { (xs: List[(Int, String)], f: String => String, g: String => String) =>
      // generated functions returning a sequence are not serializable
      val f1: String => Iterable[String] = s => Seq(f(s), f(s))
      val g1: String => Iterable[String] = s => Seq(g(s), f(s))
      val result1                        = unit(xs).flatMapValues(f1).flatMapValues(g1).collect()
      val result2 =
        unit(xs).flatMapValues(x => f1(x).flatMap(y => g1(y))).collect()
      result1 mustEqual result2
    }
  }

  property("groupBy returns DCollection with distinct keys") {
    forAll { (xs: List[String], f: String => Int) =>
      val groupedXs = unit(xs).groupBy(f).collect()
      groupedXs.map(_._1) mustEqual groupedXs.map(_._1).distinct
    }
  }

  property("groupBy groups all values a with the same result f(a) together") {
    forAll { (xs: List[String], f: String => Int) =>
      val groupedXs = unit(xs).groupBy(f).collect()
      Inspectors.forAll(groupedXs) {
        case (k, v) =>
          Inspectors.forAll(xs.filter(x => f(x) === k)) { x =>
            assert(v.toSet(x))
          }
      }
    }
  }

  property("groupBy does not change number of total values") {
    forAll { (xs: List[String], f: String => Int) =>
      val groupedXs = unit(xs).groupBy(f).collect()
      groupedXs.flatMap(_._2).size mustEqual xs.size
    }
  }

  property("groupBy with numPartitions returns DCollection with distinct keys") {
    import SmallNumbers._
    forAll { (xs: List[String], f: String => Int, numPartitions: Int) =>
      val groupedXs = unit(xs).groupBy(f, numPartitions).collect()
      groupedXs.map(_._1) mustEqual groupedXs.map(_._1).distinct
    }
  }

  property(
    "groupBy with numPartitions groups all values a with the same result f(a) together") {
    import SmallNumbers._
    forAll { (xs: List[String], f: String => Int, numPartitions: Int) =>
      val groupedXs = unit(xs).groupBy(f, numPartitions).collect()
      Inspectors.forAll(groupedXs) {
        case (k, v) =>
          Inspectors.forAll(xs.filter(x => f(x) === k)) { x =>
            assert(v.toSet(x))
          }
      }
    }
  }

  property("groupBy with numPartitions does not change number of total values") {
    import SmallNumbers._
    forAll { (xs: List[String], f: String => Int, numPartitions: Int) =>
      val groupedXs = unit(xs).groupBy(f, numPartitions).collect()
      groupedXs.flatMap(_._2).size mustEqual xs.size
    }
  }

  property(
    "mapPartitions with identity function returns unchanged DCollection") {
    forAll { (xs: List[String]) =>
      val result = unit(xs).mapPartitions(identity).collect()
      assert(result.toList === xs)
    }
  }

  property(
    "mapPartitions removes elements according to the passed in function") {
    forAll { (xs: List[Int]) =>
      val result = unit(xs).mapPartitions { it =>
        it.collect {
          case x if x % 2 == 0 => x
        }
      }
      assert(result.collect().toList === xs.filter(_ % 2 == 0))
    }
  }

  property("keyBy keys every x by f(x) and keeps values stable") {
    forAll { (xs: List[String], f: String => Int) =>
      val result = unit(xs).keyBy(f).collect()
      Inspectors.forAll(result) {
        case (k, x) =>
          assert(k === f(x))
      }
      assert(xs === result.toList.map(_._2))
    }
  }

  property(
    "The union of xs and ys contains all elements from xs and all from ys") {
    forAll { (xs: List[String], ys: List[String]) =>
      val result  = (unit(xs) ++ unit(ys)).collect()
      val result2 = unit(xs).union(unit(ys)).collect()
      assert(result.length === xs.size + ys.size)
      Inspectors.forAll(xs)(result.contains)
      Inspectors.forAll(ys)(result.contains)
      assert(result.toSeq === result2.toSeq)
    }
  }

  property(
    "sortBy returns a DCollection sorted by the given function, ascending") {
    forAll { (xs: List[String], f: String => Int) =>
      val result = unit(xs).sortBy(f, ascending = true).collect()
      result.sortBy(f) mustEqual result
    }
  }

  property(
    "sortBy returns a DCollection sorted by the given function, descending") {
    forAll { (xs: List[String], f: String => Int) =>
      val result = unit(xs).sortBy(f, ascending = false).collect()
      result.sortBy(f)(Ordering[Int].reverse) mustEqual result
    }
  }

  property("mapValues adheres to the first functor law") {
    forAll { xs: List[(String, Int)] =>
      unit(xs).mapValues(identity).collect() mustEqual unit(xs).collect()
    }
  }

  property(
    "leftOuterJoining with only common, unique keys means no joined element is None") {
    forAll { (xs: List[String], f: String => Int) =>
      val left   = unit(xs.distinct).map(x => x -> f(x))
      val right  = unit(xs.distinct).map(x => x -> f(x))
      val result = left.leftOuterJoin(right).collect()
      result.length mustEqual left.count()
      Inspectors.forAll(result) {
        case (k, (l, r)) => l mustEqual r.value
      }
    }
  }

  property(
    "leftOuterJoining leads to one element for each right key that has a matching left key") {
    import SmallNumbers._
    forAll { (k: String, n: Int) =>
      val left   = unit(List(k -> k))
      val right  = unit((1 to n).map(k -> _))
      val result = left.leftOuterJoin(right).collect()
      result.length mustEqual n
      Inspectors.forAll(1 to n) { x =>
        result.contains((k, (k, Some(x))))
      }
    }
  }

  property("leftOuterJoining means joined elements have the same key") {
    forAll { (xs: List[String], f: String => Int) =>
      val left   = unit(xs).map(x => f(x) -> x)
      val right  = unit(xs).map(x => f(x) -> x)
      val result = left.leftOuterJoin(right).collect()
      Inspectors.forAll(result) {
        case (k, (l, r)) => f(l) mustEqual f(r.value)
      }
    }
  }

  property(
    "leftOuterJoining with only missing elements means every left element has a None right element") {
    forAll { (xs: List[String], f: String => Int) =>
      val left   = unit(xs).map(x => f(x) -> x)
      val right  = unit(List.empty[String]).map(x => f(x) -> x)
      val result = left.leftOuterJoin(right).collect()
      result.length mustEqual xs.size
      Inspectors.forAll(result) {
        case (k, (l, r)) => assert(r.isEmpty)
      }
    }
  }

  property(
    "rightOuterJoining with only common, unique keys means no joined element is None") {
    forAll { (xs: List[String], f: String => Int) =>
      val left   = unit(xs.distinct).map(x => x -> f(x))
      val right  = unit(xs.distinct).map(x => x -> f(x))
      val result = left.rightOuterJoin(right).collect()
      Inspectors.forAll(result) {
        case (k, (l, r)) => l.value mustEqual r
      }
    }
  }

  property(
    "rightOuterJoining leads to one element for each left key that has a matching right key") {
    import SmallNumbers._
    forAll { (k: String, n: Int) =>
      val right  = unit(List(k -> k))
      val left   = unit((1 to n).map(k -> _))
      val result = left.rightOuterJoin(right).collect()
      result.length mustEqual n
      Inspectors.forAll(1 to n) { x =>
        result.contains((k, (Some(x), k)))
      }
    }
  }

  property("rightOuterJoining means joined elements have the same key") {
    forAll { (xs: List[String], f: String => Int) =>
      val left   = unit(xs).map(x => f(x) -> x)
      val right  = unit(xs).map(x => f(x) -> x)
      val result = left.rightOuterJoin(right).collect()
      Inspectors.forAll(result) {
        case (k, (l, r)) => f(l.value) mustEqual f(r)
      }
    }
  }

  property(
    "rightOuterJoining with only missing elements means every right element has a None left element") {
    forAll { (xs: List[String], f: String => Int) =>
      val left   = unit(List.empty[String]).map(x => f(x) -> x)
      val right  = unit(xs).map(x => f(x) -> x)
      val result = left.rightOuterJoin(right).collect()
      result.length mustEqual xs.size
      Inspectors.forAll(result) {
        case (k, (l, r)) => assert(l.isEmpty)
      }
    }
  }

  property(
    "fullOuterJoining with only common, unique keys means no joined element is None") {
    forAll { (xs: List[String], f: String => Int) =>
      val left   = unit(xs.distinct).map(x => x -> f(x))
      val right  = unit(xs.distinct).map(x => x -> f(x))
      val result = left.fullOuterJoin(right).collect()
      Inspectors.forAll(result) {
        case (k, (l, r)) => l.value mustEqual r.value
      }
    }
  }

  property("fullOuterJoining means joined elements have the same key") {
    forAll { (xs: List[String], f: String => Int) =>
      val left   = unit(xs).map(x => f(x) -> x)
      val right  = unit(xs).map(x => f(x) -> x)
      val result = left.fullOuterJoin(right).collect()
      Inspectors.forAll(result) {
        case (k, (l, r)) => f(l.value) mustEqual f(r.value)
      }
    }
  }

  property(
    "fullOuterJoin with only missing left elements means every right element has a None right element") {
    forAll { (xs: List[String], f: String => Int) =>
      val left   = unit(List.empty[String]).map(x => f(x) -> x)
      val right  = unit(xs).map(x => f(x) -> x)
      val result = left.fullOuterJoin(right).collect()
      result.length mustEqual xs.size
      Inspectors.forAll(result) {
        case (k, (l, r)) =>
          assert(l.isEmpty)
          assert(r.nonEmpty)
      }
    }
  }

  property(
    "fullOuterJoining with only missing right elements means every left element has a None right element") {
    forAll { (xs: List[String], f: String => Int) =>
      val left   = unit(xs).map(x => f(x) -> x)
      val right  = unit(List.empty[String]).map(x => f(x) -> x)
      val result = left.fullOuterJoin(right).collect()
      result.length mustEqual xs.size
      Inspectors.forAll(result) {
        case (k, (l, r)) =>
          assert(r.isEmpty)
          assert(l.nonEmpty)
      }
    }
  }

  property("mapValues adheres to the second functor law") {
    forAll { (xs: List[(String, String)], f: String => Int, g: Int => Int) =>
      unit(xs).mapValues(f).mapValues(g).collect() mustEqual unit(xs)
        .mapValues(f andThen g)
        .collect()
    }
  }

  property("mapValues doesn't have any effect on the keys") {
    forAll { (xs: List[(String, String)], f: String => Int) =>
      unit(xs).mapValues(f).collect().map(_._1) mustEqual unit(xs)
        .collect()
        .map(_._1)
    }
  }

  property("keys == map(_._1") {
    forAll { (xs: List[(Int, String)]) =>
      unit(xs).keys.collect() mustEqual unit(xs.map(_._1)).collect()
    }
  }

  property("values == map(_._2)") {
    forAll { (xs: List[(Int, String)]) =>
      unit(xs).values.collect() mustEqual unit(xs.map(_._2)).collect()
    }
  }

  property(
    "reduceByKey applies associative function to all elements with the same key") {
    forAll { (xs: List[String], f: String => Int) =>
      val result  = unit(xs).map(x => f(x) -> x).reduceByKey(_ + _).collect()
      val xsByKey = xs.groupBy(f)
      Inspectors.forAll(result) {
        case (k, v) => v mustEqual xsByKey(k).reduce(_ + _)
      }
    }
  }

  property(
    "foldByKey applies associative function to all elements with the same key") {
    forAll { (xs: List[String], f: String => Int) =>
      val result  = unit(xs).map(x => f(x) -> x).foldByKey("")(_ + _).collect()
      val xsByKey = xs.groupBy(f)
      Inspectors.forAll(result) {
        case (k, v) => v mustEqual xsByKey(k).reduce(_ + _)
      }
    }
  }

  property(
    "aggregateByKey applies associative functions on all elements with the same key") {
    forAll { (xs: List[(String, String)]) =>
      val result  = unit(xs).aggregateByKey(0)(_ + _.length, _ + _).collect()
      val xsByKey = xs.groupBy(_._1).mapValues(_.map(_._2))
      Inspectors.forAll(result) {
        case (k, v) => v mustEqual xsByKey(k).aggregate(0)(_ + _.length, _ + _)
      }
    }
  }

  property(
    "combineByKey applies associative functions on all elements with the same key") {
    forAll { (xs: List[(String, String)]) =>
      val result =
        unit(xs).combineByKey[Int](_.length, _ + _.length, _ + _).collect()
      val xsByKey = xs.groupBy(_._1).mapValues(_.map(_._2))
      Inspectors.forAll(result) {
        case (k, v) => v mustEqual xsByKey(k).aggregate(0)(_ + _.length, _ + _)
      }
    }
  }

  property("count returns the number of elements in the DCollection") {
    forAll { xs: List[String] =>
      unit(xs).count() mustEqual xs.size
    }
  }

  property(
    "countByValue returns the number of occurrences of each element in the DCollection") {
    forAll { xs: List[String] =>
      val result = unit(xs).countByValue()
      Inspectors.forAll(result) {
        case (element, count) => count mustEqual xs.count(_ == element)
      }
    }
  }

  property(
    "countByKey returns the number of occurrences of each key in the DCollection") {
    forAll { xs: List[(Int, String)] =>
      val result = unit(xs).countByKey()
      Inspectors.forAll(result) {
        case (key, count) => count mustEqual xs.count(_._1 == key)
      }
    }
  }

  property("collectAsMap() returns one element for each key") {
    forAll { xs: List[(Int, String)] =>
      val result = unit(xs ++ xs).collectAsMap()
      Inspectors.forAll(xs) {
        case (key, _) =>
          result.contains(key)
      }
    }
  }

  property("first returns the first element of the DCollection") {
    forAll { xs: Set[String] =>
      whenever(xs.nonEmpty) {
        unit(xs.toList)
          .sortBy(identity)
          .first() mustEqual xs.toList.sorted.head
        unit(xs.toList).first() mustEqual xs.head
      }
    }
  }

}
