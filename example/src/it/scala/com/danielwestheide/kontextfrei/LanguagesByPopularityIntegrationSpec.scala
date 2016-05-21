package com.danielwestheide.kontextfrei

import com.danielwestheide.kontextfrei.example.{JobLogic, LanguagesByPopularityProperties}
import org.apache.spark.rdd.RDD

class LanguagesByPopularityIntegrationSpec extends RDDSpec with LanguagesByPopularityProperties[RDD] {
  override val logic = new JobLogic[RDD]
}
