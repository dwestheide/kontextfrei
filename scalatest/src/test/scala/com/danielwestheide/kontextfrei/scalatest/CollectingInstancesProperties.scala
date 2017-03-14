package com.danielwestheide.kontextfrei.scalatest

import org.apache.spark.rdd.RDD
import org.scalatest.enablers.Collecting
import org.scalatest.{Inspectors, PropSpec, PropSpecLike}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

trait CollectingInstancesProperties[DColl[_]]
    extends PropSpecLike
    with GeneratorDrivenPropertyChecks
    with KontextfreiSpec[DColl]
    with CollectingInstances {

  property("There is a Collecting instance for DCollection") {
    forAll { (xs: List[String]) =>
      val dcoll = ops.unit(xs)
      Inspectors.forAll(dcoll) { x =>
        assert(xs.contains(x))
      }
    }
  }

  property(
    "Collecting nature of DCollection returns the original size of the input sequence") {
    forAll { (xs: List[String]) =>
      val dcoll = ops.unit(xs)
      assert(
        implicitly[Collecting[String, DColl[String]]]
          .sizeOf(dcoll) === xs.size)
    }
  }

  property(
    "Collecting nature of DCollection returns the Some loneElement if input sequence has exactly one element") {
    forAll { (x: String) =>
      val dcoll = ops.unit(List(x))
      assert(
        implicitly[Collecting[String, DColl[String]]]
          .loneElementOf(dcoll) === Some(x))
    }
  }

  property(
    "Collecting nature of DCollection returns the None as loneElement if input sequence as more than one element") {
    forAll { (xs: List[String]) =>
      whenever(xs.size > 1) {
        val dcoll = ops.unit(xs)
        assert(
          implicitly[Collecting[String, DColl[String]]]
            .loneElementOf(dcoll)
            .isEmpty)
      }
    }
  }

  property(
    "Collecting nature of DCollection returns the None as loneElement if input sequence is empty") {
    val dcoll = ops.unit(List.empty[String])
    assert(
      implicitly[Collecting[String, DColl[String]]]
        .loneElementOf(dcoll)
        .isEmpty)
  }

}

class CollectionInstancesStreamSpec
    extends CollectingInstancesProperties[Stream]
    with StreamSpec

class CollectionInstancesRDDSpec
    extends CollectingInstancesProperties[RDD]
    with RDDSpec
