package com.danielwestheide.kontextfrei.scalatest

import com.danielwestheide.kontextfrei.DCollectionOps
import com.danielwestheide.kontextfrei.rdd.RDDOpsSupport
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.scalatest.{BeforeAndAfterAll, Suite}

trait RDDSpec extends KontextfreiSpec[RDD] with BeforeAndAfterAll {
  self: Suite =>
  implicit val sparkContext = new SparkContext("local[2]", "my-test")
  override implicit val ops: DCollectionOps[RDD] =
    RDDOpsSupport.rddCollectionOps
  override protected def afterAll(): Unit = {
    sparkContext.stop()
  }
}
