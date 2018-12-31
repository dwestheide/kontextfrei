package com.danielwestheide.kontextfrei

import scala.collection.immutable.Seq
import scala.reflect.ClassTag

private[kontextfrei] trait DCollectionConstructors[DCollection[_]] {
  def unit[A: ClassTag](as: Seq[A]): DCollection[A]
  def empty[A: ClassTag]: DCollection[A]
}
