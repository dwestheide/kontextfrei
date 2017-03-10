package com.danielwestheide.kontextfrei

import scala.collection.Map
import scala.collection.immutable.Seq
import scala.reflect.ClassTag

trait DCollectionOps[DCollection[_]] {

  // instantiations:
  def unit[A: ClassTag](as: Seq[A]): DCollection[A]

  // transformations:
  def cartesian[A: ClassTag, B: ClassTag](as: DCollection[A])(
      bs: DCollection[B]): DCollection[(A, B)]
  def collect[A: ClassTag, B: ClassTag](as: DCollection[A])(
      pf: PartialFunction[A, B]): DCollection[B]
  def distinct[A: ClassTag](as: DCollection[A]): DCollection[A]
  def map[A: ClassTag, B: ClassTag](as: DCollection[A])(
      f: A => B): DCollection[B]
  def flatMap[A: ClassTag, B: ClassTag](as: DCollection[A])(
      f: A => TraversableOnce[B]): DCollection[B]
  def filter[A: ClassTag](as: DCollection[A])(f: A => Boolean): DCollection[A]
  def groupBy[A, B: ClassTag](as: DCollection[A])(
      f: A => B): DCollection[(B, Iterable[A])]
  def groupByWithNumPartitions[A, B: ClassTag](as: DCollection[A])(
      f: A => B,
      numPartitions: Int): DCollection[(B, Iterable[A])]
  def mapPartitions[A: ClassTag, B: ClassTag](as: DCollection[A])(
      f: Iterator[A] => Iterator[B],
      preservesPartitioning: Boolean = false): DCollection[B]
  def keyBy[A: ClassTag, B](as: DCollection[A])(f: A => B): DCollection[(B, A)]
  def union[A: ClassTag](xs: DCollection[A],
                         ys: DCollection[A]): DCollection[A]

  // sorting:
  def sortBy[A: ClassTag, B: ClassTag: Ordering](as: DCollection[A])(
      f: A => B)(ascending: Boolean): DCollection[A]

  // pair transformations:
  def values[A: ClassTag, B: ClassTag](x: DCollection[(A, B)]): DCollection[B]
  def keys[A: ClassTag, B: ClassTag](x: DCollection[(A, B)]): DCollection[A]
  def cogroup[A: ClassTag, B: ClassTag, C: ClassTag](x: DCollection[(A, B)])(
      y: DCollection[(A, C)]): DCollection[(A, (Iterable[B], Iterable[C]))]
  def leftOuterJoin[A: ClassTag, B: ClassTag, C: ClassTag](
      x: DCollection[(A, B)])(
      y: DCollection[(A, C)]): DCollection[(A, (B, Option[C]))]
  def rightOuterJoin[A: ClassTag, B: ClassTag, C: ClassTag](
      x: DCollection[(A, B)])(
      y: DCollection[(A, C)]): DCollection[(A, (Option[B], C))]
  def fullOuterJoin[A: ClassTag, B: ClassTag, C: ClassTag](
      x: DCollection[(A, B)])(
      y: DCollection[(A, C)]): DCollection[(A, (Option[B], Option[C]))]
  def mapValues[A: ClassTag, B: ClassTag, C: ClassTag](x: DCollection[(A, B)])(
      f: B => C): DCollection[(A, C)]
  def flatMapValues[A: ClassTag, B: ClassTag, C: ClassTag](
      xs: DCollection[(A, B)])(f: B => TraversableOnce[C]): DCollection[(A, C)]
  def foldByKey[A: ClassTag, B: ClassTag](xs: DCollection[(A, B)])(
      zeroValue: B)(func: (B, B) => B): DCollection[(A, B)]
  def reduceByKey[A: ClassTag, B: ClassTag](xs: DCollection[(A, B)])(
      f: (B, B) => B): DCollection[(A, B)]
  def aggregateByKey[A: ClassTag, B: ClassTag, C: ClassTag](
      xs: DCollection[(A, B)])(zeroValue: C)(seqOp: (C, B) => C)(
      combOp: (C, C) => C): DCollection[(A, C)]

  // actions:
  def collectAsArray[A: ClassTag](as: DCollection[A]): Array[A]
  def count[A](as: DCollection[A]): Long
  def countByValue[A: ClassTag](as: DCollection[A])(
      implicit ord: Ordering[A]): Map[A, Long]
  def first[A: ClassTag](as: DCollection[A]): A

  // pair actions:
  def countByKey[A: ClassTag, B: ClassTag](
      xs: DCollection[(A, B)]): Map[A, Long]
  def collectAsMap[A: ClassTag, B: ClassTag](
      xs: DCollection[(A, B)]): Map[A, B]

}

object DCollectionOps {

  trait DistributionOps {
    def unit[DCollection[_], A: ClassTag](as: Seq[A])(
        implicit dc: DCollectionOps[DCollection]): DCollection[A] = dc.unit(as)
  }

  class Syntax[DCollection[_], A: ClassTag](
      val self: DCollectionOps[DCollection],
      val coll: DCollection[A]) {
    def cartesian[B: ClassTag](bs: DCollection[B]): DCollection[(A, B)] =
      self.cartesian(coll)(bs)
    def collect[B: ClassTag](pf: PartialFunction[A, B]): DCollection[B] =
      self.collect(coll)(pf)
    def distinct(): DCollection[A]                  = self.distinct(coll)
    def map[B: ClassTag](f: A => B): DCollection[B] = self.map(coll)(f)
    def flatMap[B: ClassTag](f: A => TraversableOnce[B]): DCollection[B] =
      self.flatMap(coll)(f)
    def filter(f: A => Boolean): DCollection[A] = self.filter(coll)(f)
    def groupBy[B: ClassTag](f: A => B): DCollection[(B, Iterable[A])] =
      self.groupBy(coll)(f)
    def groupBy[B: ClassTag](
        f: A => B,
        numPartitions: Int): DCollection[(B, Iterable[A])] =
      self.groupByWithNumPartitions(coll)(f, numPartitions)
    def mapPartitions[B: ClassTag](
        f: Iterator[A] => Iterator[B],
        preservesPartitioning: Boolean = false): DCollection[B] =
      self.mapPartitions(coll)(f, preservesPartitioning)
    def keyBy[B](f: A => B): DCollection[(B, A)]     = self.keyBy(coll)(f)
    def union(other: DCollection[A]): DCollection[A] = self.union(coll, other)
    def ++(other: DCollection[A]): DCollection[A]    = self.union(coll, other)

    def sortBy[B: ClassTag: Ordering](
        f: A => B,
        ascending: Boolean = true): DCollection[A] =
      self.sortBy(coll)(f)(ascending)

    def collect(): Array[A] = self.collectAsArray(coll)
    def count(): Long       = self.count(coll)
    def countByValue()(implicit ord: Ordering[A]): Map[A, Long] =
      self.countByValue(coll)
    def first(): A = self.first(coll)
  }

  class PairSyntax[DCollection[_], A: ClassTag, B: ClassTag](
      val self: DCollectionOps[DCollection],
      val coll: DCollection[(A, B)]) {
    def keys: DCollection[A]   = self.keys(coll)
    def values: DCollection[B] = self.values(coll)
    def cogroup[C: ClassTag](other: DCollection[(A, C)])
      : DCollection[(A, (Iterable[B], Iterable[C]))] =
      self.cogroup(coll)(other)
    def leftOuterJoin[C: ClassTag](
        other: DCollection[(A, C)]): DCollection[(A, (B, Option[C]))] =
      self.leftOuterJoin(coll)(other)
    def rightOuterJoin[C: ClassTag](
        other: DCollection[(A, C)]): DCollection[(A, (Option[B], C))] =
      self.rightOuterJoin(coll)(other)
    def fullOuterJoin[C: ClassTag](
        other: DCollection[(A, C)]): DCollection[(A, (Option[B], Option[C]))] =
      self.fullOuterJoin(coll)(other)
    def mapValues[C: ClassTag](f: B => C): DCollection[(A, C)] =
      self.mapValues(coll)(f)
    def flatMapValues[C: ClassTag](
        f: B => TraversableOnce[C]): DCollection[(A, C)] =
      self.flatMapValues(coll)(f)
    def reduceByKey(f: (B, B) => B): DCollection[(A, B)] =
      self.reduceByKey(coll)(f)
    def foldByKey(zeroValue: B)(f: (B, B) => B): DCollection[(A, B)] =
      self.foldByKey(coll)(zeroValue)(f)
    def aggregateByKey[C: ClassTag](zeroValue: C)(
        seqOp: (C, B) => C,
        combOp: (C, C) => C): DCollection[(A, C)] =
      self.aggregateByKey(coll)(zeroValue)(seqOp)(combOp)
    def countByKey(): Map[A, Long] = self.countByKey(coll)
    def collectAsMap(): Map[A, B]  = self.collectAsMap(coll)
  }

  trait ToSyntax {
    implicit def toDCollectionOps[DCollection[_], A: ClassTag](
        target: DCollection[A])(implicit dc: DCollectionOps[DCollection]) =
      new Syntax[DCollection, A](dc, target)
    implicit def toDCollectionPairOps[DCollection[_],
                                      A: ClassTag,
                                      B: ClassTag](
        target: DCollection[(A, B)])(
        implicit dc: DCollectionOps[DCollection]) =
      new PairSyntax[DCollection, A, B](dc, target)
  }

  trait AllOps   extends DistributionOps with ToSyntax
  object syntax  extends ToSyntax
  object Imports extends AllOps
}
