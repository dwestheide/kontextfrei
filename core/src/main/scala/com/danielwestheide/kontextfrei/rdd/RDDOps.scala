package com.danielwestheide.kontextfrei.rdd

import com.danielwestheide.kontextfrei.DCollectionOps
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

class RDDOps(override final val sparkContext: SparkContext)
    extends DCollectionOps[RDD]
    with RDDBase
    with RDDBaseFunctions
    with RDDPairFunctions
    with RDDConstructors
    with RDDOrderedFunctions
