package com.danielwestheide.kontextfrei.syntax
import com.danielwestheide.kontextfrei.DCollectionOps

import scala.reflect.ClassTag

class OrderedSyntax[DCollection[_], A: ClassTag : Ordering, B: ClassTag](
    val self: DCollectionOps[DCollection],
    val coll: DCollection[(A, B)]) {

  final def sortByKey(ascending: Boolean): DCollection[(A, B)] =
    self.sortByKey(coll)(ascending)

  final def sortByKey(): DCollection[(A, B)] =
    self.sortByKey(coll)(ascending = true)

  final def sortByKey(ascending: Boolean = true, numPartitions: Int): DCollection[(A, B)] =
    self.sortByKeyWithNumPartitions(coll)(ascending, numPartitions)

  final def filterByRange(lower: A, upper: A): DCollection[(A, B)] =
    self.filterByRange(coll)(lower, upper)

}
