package com.danielwestheide.kontextfrei

import com.danielwestheide.kontextfrei.rdd.RDDOpsSupport
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.scalatest.BeforeAndAfterAll

class RDDCollectionOpsSpec
    extends DCollectionOpsProperties[RDD]
    with BeforeAndAfterAll {
  implicit val sparkContext = new SparkContext("local[2]", "dcollection-spec")
  override implicit val ops: DCollectionOps[RDD] =
    RDDOpsSupport.rddCollectionOps
  override protected def afterAll(): Unit = {
    sparkContext.stop()
  }
}
