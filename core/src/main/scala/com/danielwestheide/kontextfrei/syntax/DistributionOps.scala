package com.danielwestheide.kontextfrei.syntax

import com.danielwestheide.kontextfrei.DCollectionOps

import scala.collection.immutable.Seq
import scala.reflect.ClassTag

trait DistributionOps {
  final def unit[DCollection[_], A: ClassTag](as: Seq[A])(
      implicit dc: DCollectionOps[DCollection]): DCollection[A] = dc.unit(as)
  final def empty[DCollection[_], A: ClassTag](
      implicit dc: DCollectionOps[DCollection]): DCollection[A] = dc.empty
}
