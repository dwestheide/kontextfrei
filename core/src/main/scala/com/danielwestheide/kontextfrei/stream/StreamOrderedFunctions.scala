package com.danielwestheide.kontextfrei.stream
import com.danielwestheide.kontextfrei.DCollectionOrderedFunctions

import scala.reflect.ClassTag

private[kontextfrei] trait StreamOrderedFunctions extends DCollectionOrderedFunctions[Stream] {

  override final def sortByKey[
  A: ClassTag: Ordering,
  B: ClassTag](x: Stream[(A, B)])(
    ascending: Boolean): Stream[(A, B)] = x.sortBy(_._1)(ordering(ascending))

  override final def sortByKeyWithNumPartitions[
      A: ClassTag: Ordering,
      B: ClassTag](x: Stream[(A, B)])(
      ascending: Boolean,
      numPartitions: Int): Stream[(A, B)] = x.sortBy(_._1)(ordering(ascending))

  private def ordering[A](ascending: Boolean)(implicit ev: Ordering[A]): Ordering[A] =
    if (ascending) ev
    else ev.reverse

}
