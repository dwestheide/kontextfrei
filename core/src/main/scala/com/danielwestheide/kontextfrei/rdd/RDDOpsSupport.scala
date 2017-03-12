package com.danielwestheide.kontextfrei.rdd

import com.danielwestheide.kontextfrei.DCollectionOps
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

trait RDDOpsSupport {

  implicit def rddCollectionOps(
      implicit sparkContext: SparkContext): DCollectionOps[RDD] =
    new RDDOps(sparkContext)
}

object RDDOpsSupport extends RDDOpsSupport
