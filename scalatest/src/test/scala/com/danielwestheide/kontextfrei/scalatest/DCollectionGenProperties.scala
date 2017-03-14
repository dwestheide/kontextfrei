package com.danielwestheide.kontextfrei.scalatest

import org.apache.spark.rdd.RDD
import org.scalatest.PropSpecLike
import org.scalatest.prop.GeneratorDrivenPropertyChecks

trait DCollectionGenProperties[DColl[_]]
    extends PropSpecLike
    with GeneratorDrivenPropertyChecks
    with DCollectionGen
    with KontextfreiSpec[DColl] {

  property("Can get arbitrary DCollections") {
    forAll { xs: DColl[String] =>
      ops.count(xs) === ops.collectAsArray(xs).length
    }
  }

}

class DCollectionGenStreamSpec
    extends DCollectionGenProperties[Stream]
    with StreamSpec
class DCollectionGenRDDSpec extends DCollectionGenProperties[RDD] with RDDSpec
