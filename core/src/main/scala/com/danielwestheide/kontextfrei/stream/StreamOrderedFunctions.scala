package com.danielwestheide.kontextfrei.stream
import com.danielwestheide.kontextfrei.DCollectionOrderedFunctions
import org.apache.spark.Partitioner

import scala.reflect.ClassTag

private[kontextfrei] trait StreamOrderedFunctions
    extends DCollectionOrderedFunctions[Stream] {

  import Ordering.Implicits._

  override final def sortByKey[A: ClassTag: Ordering, B: ClassTag](
      x: Stream[(A, B)])(ascending: Boolean): Stream[(A, B)] =
    x.sortBy(_._1)(ordering(ascending))

  override final def sortByKeyWithNumPartitions[A: ClassTag: Ordering,
                                                B: ClassTag](x: Stream[(A, B)])(
      ascending: Boolean,
      numPartitions: Int): Stream[(A, B)] = x.sortBy(_._1)(ordering(ascending))

  override final def filterByRange[A: ClassTag: Ordering, B: ClassTag](
      x: Stream[(A, B)])(lower: A, upper: A): Stream[(A, B)] =
    x.filter(e => e._1 >= lower && e._1 <= upper)

  override def repartitionAndSortWithinPartitions[A: ClassTag: Ordering,
                                                  B: ClassTag](
      x: Stream[(A, B)])(partitioner: Partitioner): Stream[(A, B)] =
    x.sortBy(_._1)(ordering(ascending = true))

  private def ordering[A](ascending: Boolean)(
      implicit ev: Ordering[A]): Ordering[A] =
    if (ascending) ev
    else ev.reverse

}
