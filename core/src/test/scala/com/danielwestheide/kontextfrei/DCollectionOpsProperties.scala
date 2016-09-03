package com.danielwestheide.kontextfrei

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

  property("cartesian returns a DCollection where each A is paired with each B") {
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
      Inspectors.forAll(xs) { case (k, v) =>
        result.find(_._1 == k).exists(_._2._1.exists(_ == v))
      }
      Inspectors.forAll(ys) { case (k, v) =>
        result.find(_._1 == k).exists(_._2._2.exists(_ == v))
      }
    }
  }

  property("xs.collect(pf) == xs.filter(pf.isDefinedAt).map(pf)") {
    forAll { (xs: List[String], pf: PartialFunction[String, Int]) =>
      unit(xs).collect(pf).collect() mustEqual unit(xs).filter(pf.isDefinedAt).map(pf).collect()
    }
  }

  property("distinct returns a DCollection of distinct elements") {
    forAll(listWithDuplicates[String]) { xs =>
      whenever(xs.distinct !== xs) {
        unit(xs).distinct().collect().sorted mustEqual unit(xs).collect().distinct.sorted
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
      unit(xs).map(f).map(g).collect() mustEqual unit(xs).map(f andThen g).collect()
    }
  }

  property("filter returns a DCollection with only elements matching the predicate") {
    forAll { (xs: List[String], pred: String => Boolean) =>
      val filteredXs = unit(xs).filter(pred).collect()
      Inspectors.forAll(filteredXs)(pred(_) mustBe true)
    }
  }

  property("filter returns a DCollection with all elements matching the predicate") {
    forAll { (xs: List[String], pred: String => Boolean) =>
      val allMatchingXs = xs.filter(pred)
      val filteredXs = unit(xs).filter(pred).collect()
      Inspectors.forAll(allMatchingXs)(filteredXs.contains(_))
    }
  }

  property("flatMap adheres to left identity law") {
    forAll { (x: String, f: String => Iterable[String]) =>
      unit(List(x)).flatMap(f).collect() mustEqual f(x).toArray
    }
  }

  property("flatMap adheres to right identity law") {
    forAll { x: String =>
      unit(List(x)).flatMap(s => List(s)).collect() mustEqual Array(x)
    }
  }

  property("flatMap adheres to associativity law") {
    forAll { (xs: List[String], f: String => Iterable[String], g: String => Iterable[String]) =>
      val result1 = unit(xs).flatMap(f).flatMap(g).collect()
      val result2 = unit(xs).flatMap(x => f(x).flatMap(y => g(y))).collect()
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

  property("sortBy returns a DCollection sorted by the given function, ascending") {
    forAll { (xs: List[String], f: String => Int) =>
      val result = unit(xs).sortBy(f, ascending = true).collect()
      result.sortBy(f) mustEqual result
    }
  }

  property("sortBy returns a DCollection sorted by the given function, descending") {
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

  property("leftOuterJoining with only common, unique keys means no joined element is None") {
    forAll { (xs: List[String], f: String => Int) =>
      val left = unit(xs.distinct).map(x => x -> f(x))
      val right = unit(xs.distinct).map(x => x -> f(x))
      val result = left.leftOuterJoin(right).collect()
      Inspectors.forAll(result) {
        case (k, (l, r)) => l mustEqual r.value
      }
    }
  }

  property("leftOuterJoining means joined elements have the same key") {
    forAll { (xs: List[String], f: String => Int) =>
      val left = unit(xs).map(x => f(x) -> x)
      val right = unit(xs).map(x => f(x) -> x)
      val result = left.leftOuterJoin(right).collect()
      Inspectors.forAll(result) {
        case (k, (l, r)) => f(l) mustEqual f(r.value)
      }
    }
  }

  property("leftOuterJoining with only missing elements means every left element as a None right element") {
    forAll { (xs: List[String], f: String => Int) =>
      val left = unit(xs).map(x => f(x) -> x)
      val right = unit(List.empty[String]).map(x => f(x) -> x)
      val result = left.leftOuterJoin(right).collect()
      result.length mustEqual xs.size
      Inspectors.forAll(result) {
        case (k, (l, r)) => assert(r.isEmpty)
      }
    }
  }

  property("mapValues adheres to the second functor law") {
    forAll { (xs: List[(String, String)], f: String => Int, g: Int => Int) =>
      unit(xs).mapValues(f).mapValues(g).collect() mustEqual unit(xs).mapValues(f andThen g).collect()
    }
  }

  property("mapValues doesn't have any effect on the keys") {
    forAll { (xs: List[(String, String)], f: String => Int) =>
      unit(xs).mapValues(f).collect().map(_._1) mustEqual unit(xs).collect().map(_._1)
    }
  }

  property("keys == map(_._1") {
    forAll{ (xs: List[(Int, String)]) =>
      unit(xs).keys.collect() mustEqual unit(xs.map(_._1)).collect()
    }
  }

  property("values == map(_._2") {
    forAll{ (xs: List[(Int, String)]) =>
      unit(xs).values.collect() mustEqual unit(xs.map(_._2)).collect()
    }
  }

  property("reduceByKey applies associative function to all elements with the same key") {
    forAll { (xs: List[String], f: String => Int) =>
      val result = unit(xs).map(x => f(x) -> x).reduceByKey(_ + _).collect()
      val xsByKey = xs.groupBy(f)
      Inspectors.forAll(result) {
        case (k, v) => v mustEqual xsByKey(k).reduce(_ + _)
      }
    }
  }

  property("aggregateByKey applies associative functions on all elements with the same key") {
    forAll { (xs: List[(String, String)]) =>
      val result = unit(xs).aggregateByKey(0)(_ + _.length)(_ + _).collect()
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

  property("countByValue returns the number of occurrences of each element in the DCollection") {
    forAll { xs: List[String] =>
      val result = unit(xs).countByValue()
      Inspectors.forAll(result) {
        case (element, count) => count mustEqual xs.count(_ == element)
      }
    }
  }

  property("first returns the first element of the DCollection") {
    forAll { xs: Set[String] =>
      whenever(xs.nonEmpty) {
        unit(xs.toList).sortBy(identity).first() mustEqual xs.toList.sorted.head
        unit(xs.toList).first() mustEqual xs.head
      }
    }
  }


}
