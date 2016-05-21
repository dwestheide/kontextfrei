package com.danielwestheide.kontextfrei

import com.danielwestheide.kontextfrei.example.{JobLogic, UsersByPopularityProperties}
import org.apache.spark.rdd.RDD

class UsersByPopularityIntegrationSpec extends RDDSpec with UsersByPopularityProperties[RDD] {
  override implicit val ops: DCollectionOps[RDD] = RDDCollectionOps.rddCollectionOps
  override val logic = new JobLogic[RDD]
}
