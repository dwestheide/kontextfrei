package com.danielwestheide.kontextfrei
import scala.reflect.ClassTag

private[kontextfrei] trait DCollectionOrderedFunctions[DCollection[_]] {

  def sortByKey[A: ClassTag: Ordering, B: ClassTag](x: DCollection[(A, B)])(
      ascending: Boolean): DCollection[(A, B)]

  def sortByKeyWithNumPartitions[A: ClassTag: Ordering, B: ClassTag](
      x: DCollection[(A, B)])(ascending: Boolean,
                              numPartitions: Int): DCollection[(A, B)]

  def filterByRange[A: ClassTag: Ordering, B: ClassTag](
      x: DCollection[(A, B)])(lower: A, upper: A): DCollection[(A, B)]

}
