package com.danielwestheide.kontextfrei.rdd
import com.danielwestheide.kontextfrei.DCollectionOrderedFunctions
import org.apache.spark.rdd.RDD

import scala.reflect.ClassTag

private[kontextfrei] trait RDDOrderedFunctions
    extends DCollectionOrderedFunctions[RDD] {

  override final def sortByKey[A: ClassTag: Ordering, B: ClassTag](
      x: RDD[(A, B)])(ascending: Boolean): RDD[(A, B)] = x.sortByKey(ascending)

  override final def sortByKeyWithNumPartitions[A: ClassTag: Ordering,
                                                B: ClassTag](
      x: RDD[(A, B)])(ascending: Boolean, numPartitions: Int): RDD[(A, B)] =
    x.sortByKey(ascending, numPartitions)

}
