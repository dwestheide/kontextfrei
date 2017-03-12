package com.danielwestheide.kontextfrei.rdd

import org.apache.spark.SparkContext

private[kontextfrei] trait RDDBase {
  def sparkContext: SparkContext
}
