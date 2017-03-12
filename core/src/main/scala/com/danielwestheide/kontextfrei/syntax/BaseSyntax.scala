package com.danielwestheide.kontextfrei.syntax

import com.danielwestheide.kontextfrei.DCollectionOps

import scala.collection.Map
import scala.reflect.ClassTag

class BaseSyntax[DCollection[_], A: ClassTag](
    val self: DCollectionOps[DCollection],
    val coll: DCollection[A]) {

  final def cartesian[B: ClassTag](bs: DCollection[B]): DCollection[(A, B)] =
    self.cartesian(coll)(bs)

  final def collect[B: ClassTag](pf: PartialFunction[A, B]): DCollection[B] =
    self.collect(coll)(pf)

  final def distinct(): DCollection[A] = self.distinct(coll)

  final def map[B: ClassTag](f: A => B): DCollection[B] = self.map(coll)(f)

  final def flatMap[B: ClassTag](f: A => TraversableOnce[B]): DCollection[B] =
    self.flatMap(coll)(f)
  final def filter(f: A => Boolean): DCollection[A] = self.filter(coll)(f)

  final def groupBy[B: ClassTag](f: A => B): DCollection[(B, Iterable[A])] =
    self.groupBy(coll)(f)

  final def groupBy[B: ClassTag](
      f: A => B,
      numPartitions: Int): DCollection[(B, Iterable[A])] =
    self.groupByWithNumPartitions(coll)(f, numPartitions)

  final def mapPartitions[B: ClassTag](
      f: Iterator[A] => Iterator[B],
      preservesPartitioning: Boolean = false): DCollection[B] =
    self.mapPartitions(coll)(f, preservesPartitioning)

  final def keyBy[B](f: A => B): DCollection[(B, A)] = self.keyBy(coll)(f)

  final def union(other: DCollection[A]): DCollection[A] =
    self.union(coll, other)

  final def ++(other: DCollection[A]): DCollection[A] = self.union(coll, other)

  final def sortBy[B: ClassTag: Ordering](
      f: A => B,
      ascending: Boolean = true): DCollection[A] =
    self.sortBy(coll)(f)(ascending)

  final def collect(): Array[A] = self.collectAsArray(coll)

  final def count(): Long = self.count(coll)

  final def countByValue()(implicit ord: Ordering[A]): Map[A, Long] =
    self.countByValue(coll)

  final def first(): A = self.first(coll)

}
