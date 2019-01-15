package com.danielwestheide.kontextfrei.rdd
import com.danielwestheide.kontextfrei.DCollectionOrderedFunctions
import org.apache.spark.rdd.RDD

import scala.reflect.ClassTag

private[kontextfrei] trait RDDOrderedFunctions
    extends DCollectionOrderedFunctions[RDD] { this: RDDBase =>

  override final def sortByKey[A: ClassTag: Ordering, B: ClassTag](
      x: RDD[(A, B)])(ascending: Boolean): RDD[(A, B)] = withSite(x) {
    _.sortByKey(ascending)
  }

  override final def sortByKeyWithNumPartitions[A: ClassTag: Ordering,
                                                B: ClassTag](
      x: RDD[(A, B)])(ascending: Boolean, numPartitions: Int): RDD[(A, B)] = withSite(x) {
    _.sortByKey(ascending, numPartitions)
  }

  override final def filterByRange[A: ClassTag: Ordering, B: ClassTag](
      x: RDD[(A, B)])(lower: A, upper: A): RDD[(A, B)] = withSite(x) {
    _.filterByRange(lower, upper)
  }
}
