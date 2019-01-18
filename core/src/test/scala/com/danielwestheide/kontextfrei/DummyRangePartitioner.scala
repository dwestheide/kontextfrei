package com.danielwestheide.kontextfrei
import org.apache.spark.Partitioner

object DummyRangePartitioner extends Partitioner {
  override def numPartitions: Int          = 2
  override def getPartition(key: Any): Int = {
    key match {
      case x: Int =>
        if (x < 0) 0
        else 1
      case _ => 0
    }
  }
}

