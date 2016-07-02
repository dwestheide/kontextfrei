package com.danielwestheide.kontextfrei

import org.scalatest.Inspectors

trait DCollectionOpsProperties[DColl[_]] extends BaseSpec[DColl] {

  import DCollectionOps.Imports._

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
      Inspectors.forAll(groupedXs) { case (k, v) =>
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


}
