package com.danielwestheide.kontextfrei
import org.apache.spark.Partitioner

import scala.reflect.ClassTag

private[kontextfrei] trait DCollectionOrderedFunctions[DCollection[_]] {

  def sortByKey[A: ClassTag: Ordering, B: ClassTag](x: DCollection[(A, B)])(
      ascending: Boolean): DCollection[(A, B)]

  def sortByKeyWithNumPartitions[A: ClassTag: Ordering, B: ClassTag](
      x: DCollection[(A, B)])(ascending: Boolean,
                              numPartitions: Int): DCollection[(A, B)]

  def filterByRange[A: ClassTag: Ordering, B: ClassTag](
      x: DCollection[(A, B)])(lower: A, upper: A): DCollection[(A, B)]

  def repartitionAndSortWithinPartitions[A: ClassTag: Ordering, B: ClassTag](
      x: DCollection[(A, B)])(partitioner: Partitioner): DCollection[(A, B)]

}
