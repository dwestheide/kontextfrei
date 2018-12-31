package com.danielwestheide.kontextfrei.stream

import com.danielwestheide.kontextfrei.DCollectionConstructors

import scala.collection.immutable.Seq
import scala.reflect.ClassTag

private[kontextfrei] trait StreamConstructors
    extends DCollectionConstructors[Stream] {
  override final def unit[A: ClassTag](as: Seq[A]): Stream[A] = as.toStream
  override final def empty[A: ClassTag]: Stream[A] = Stream.empty
}
