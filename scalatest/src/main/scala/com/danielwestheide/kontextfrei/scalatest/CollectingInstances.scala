package com.danielwestheide.kontextfrei.scalatest

import com.danielwestheide.kontextfrei.DCollectionOps
import org.scalatest.enablers.Collecting

import scala.collection.GenTraversable
import scala.reflect.ClassTag

trait CollectingInstances {
  implicit def collectingDCollection[A: ClassTag, DCollection[_]](
      implicit ops: DCollectionOps[DCollection])
    : Collecting[A, DCollection[A]] = new Collecting[A, DCollection[A]] {
    override def loneElementOf(collection: DCollection[A]): Option[A] = {
      val as = ops.collectAsArray(collection)
      if (as.length == 1) Some(as(0)) else None
    }

    override def sizeOf(collection: DCollection[A]): Int =
      ops.count(collection).toInt

    override def genTraversableFrom(
        collection: DCollection[A]): GenTraversable[A] =
      ops.collectAsArray(collection)
  }
}

object CollectingInstances extends CollectingInstances
