package com.danielwestheide.kontextfrei

import org.apache.spark.Partitioner
import org.apache.spark.storage.StorageLevel

import scala.collection.Map
import scala.reflect.ClassTag

private[kontextfrei] trait DCollectionBaseFunctions[DCollection[_]] {

  def cartesian[A: ClassTag, B: ClassTag](as: DCollection[A])(
      bs: DCollection[B]): DCollection[(A, B)]

  def collect[A: ClassTag, B: ClassTag](as: DCollection[A])(
      pf: PartialFunction[A, B]): DCollection[B]

  def distinct[A: ClassTag](as: DCollection[A]): DCollection[A]

  def distinctWithNumPartitions[A: ClassTag](as: DCollection[A])(
      numPartitions: Int): DCollection[A]

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

  def groupByWithPartitioner[A, B: ClassTag](as: DCollection[A])(
      f: A => B,
      partitioner: Partitioner): DCollection[(B, Iterable[A])]

  def mapPartitions[A: ClassTag, B: ClassTag](as: DCollection[A])(
      f: Iterator[A] => Iterator[B],
      preservesPartitioning: Boolean = false): DCollection[B]

  def keyBy[A: ClassTag, B](as: DCollection[A])(f: A => B): DCollection[(B, A)]

  def union[A: ClassTag](xs: DCollection[A])(ys: DCollection[A]): DCollection[A]

  def intersection[A: ClassTag](xs: DCollection[A])(
      ys: DCollection[A]): DCollection[A]

  def intersectionWithPartitioner[A: ClassTag](xs: DCollection[A])(
      ys: DCollection[A],
      partitioner: Partitioner): DCollection[A]

  def intersectionWithNumPartitions[A: ClassTag](xs: DCollection[A])(
      ys: DCollection[A],
      numPartitions: Int): DCollection[A]

  def zip[A: ClassTag, B: ClassTag](xs: DCollection[A])(
      ys: DCollection[B]): DCollection[(A, B)]

  def zipWithIndex[A: ClassTag](xs: DCollection[A]): DCollection[(A, Long)]

  def zipWithUniqueId[A: ClassTag](xs: DCollection[A]): DCollection[(A, Long)]

  def zipPartitions[A: ClassTag, B: ClassTag, C: ClassTag](as: DCollection[A])(
      bs: DCollection[B])(
      f: (Iterator[A], Iterator[B]) => Iterator[C]): DCollection[C]

  def zipPartitionsWithPreservesPartitioning[A: ClassTag,
                                             B: ClassTag,
                                             C: ClassTag](
      as: DCollection[A])(bs: DCollection[B], preservesPartitioning: Boolean)(
      f: (Iterator[A], Iterator[B]) => Iterator[C]): DCollection[C]

  def zipPartitions3[A: ClassTag, B: ClassTag, C: ClassTag, D: ClassTag](
      as: DCollection[A])(bs: DCollection[B], cs: DCollection[C])(
      f: (Iterator[A], Iterator[B], Iterator[C]) => Iterator[D]): DCollection[D]

  def zipPartitions3WithPreservesPartitioning[A: ClassTag,
                                              B: ClassTag,
                                              C: ClassTag,
                                              D: ClassTag](as: DCollection[A])(
      bs: DCollection[B],
      cs: DCollection[C],
      preservesPartitioning: Boolean)(
      f: (Iterator[A], Iterator[B], Iterator[C]) => Iterator[D]): DCollection[D]

  def zipPartitions4[A: ClassTag,
                     B: ClassTag,
                     C: ClassTag,
                     D: ClassTag,
                     E: ClassTag](as: DCollection[A])(
      bs: DCollection[B],
      cs: DCollection[C],
      ds: DCollection[D])(f: (Iterator[A],
                              Iterator[B],
                              Iterator[C],
                              Iterator[D]) => Iterator[E]): DCollection[E]

  def zipPartitions4WithPreservesPartitioning[A: ClassTag,
                                              B: ClassTag,
                                              C: ClassTag,
                                              D: ClassTag,
                                              E: ClassTag](as: DCollection[A])(
      bs: DCollection[B],
      cs: DCollection[C],
      ds: DCollection[D],
      preservesPartitioning: Boolean)(
      f: (Iterator[A], Iterator[B], Iterator[C], Iterator[D]) => Iterator[E])
    : DCollection[E]

  def subtract[A: ClassTag](xs: DCollection[A])(
      ys: DCollection[A]): DCollection[A]

  def subtractWithNumPartitions[A: ClassTag](xs: DCollection[A])(
      ys: DCollection[A],
      numPartitions: Int): DCollection[A]

  def subtractWithPartitioner[A: ClassTag](xs: DCollection[A])(
      ys: DCollection[A],
      partitioner: Partitioner): DCollection[A]

  def persist[A: ClassTag](xs: DCollection[A]): DCollection[A]

  def persistWithStorageLevel[A: ClassTag](xs: DCollection[A])(
      level: StorageLevel): DCollection[A]

  def unpersist[A: ClassTag](xs: DCollection[A])(
      blocking: Boolean = true): DCollection[A]

  def glom[A: ClassTag](xs: DCollection[A]): DCollection[Array[A]]

  def sortBy[A: ClassTag, B: ClassTag: Ordering](as: DCollection[A])(f: A => B)(
      ascending: Boolean): DCollection[A]

  def sortByWithNumPartitions[A: ClassTag, B: ClassTag: Ordering](
      as: DCollection[A])(f: A => B)(ascending: Boolean)(
      numPartitions: Int): DCollection[A]

  def collectAsArray[A: ClassTag](as: DCollection[A]): Array[A]

  def count[A](as: DCollection[A]): Long

  def countByValue[A: ClassTag](as: DCollection[A])(
      implicit ord: Ordering[A]): Map[A, Long]

  def reduce[A: ClassTag](as: DCollection[A])(f: (A, A) => A): A

  def fold[A: ClassTag](as: DCollection[A])(zeroValue: A)(op: (A, A) => A): A

  def aggregate[A: ClassTag, B: ClassTag](as: DCollection[A])(
      zeroValue: B)(seqOp: (B, A) => B, combOp: (B, B) => B): B

  def treeReduce[A: ClassTag](as: DCollection[A])(f: (A, A) => A, depth: Int = 2): A

  def treeAggregate[A: ClassTag, B: ClassTag](as: DCollection[A])(
    zeroValue: B)(seqOp: (B, A) => B, combOp: (B, B) => B, depth: Int = 2): B

  def first[A: ClassTag](as: DCollection[A]): A

  def take[A: ClassTag](as: DCollection[A])(n: Int): Array[A]

  def takeOrdered[A: ClassTag](as: DCollection[A])(num: Int)(
      implicit ord: Ordering[A]): Array[A]

  def top[A: ClassTag](as: DCollection[A])(num: Int)(
      implicit ord: Ordering[A]): Array[A]

  def min[A: ClassTag](as: DCollection[A])(implicit ord: Ordering[A]): A

  def max[A: ClassTag](as: DCollection[A])(implicit ord: Ordering[A]): A

  def foreach[A: ClassTag](as: DCollection[A])(f: A => Unit): Unit

  def foreachPartition[A: ClassTag](as: DCollection[A])(
      f: Iterator[A] => Unit): Unit

  def isEmpty[A: ClassTag](as: DCollection[A]): Boolean

  def toLocalIterator[A: ClassTag](as: DCollection[A]): Iterator[A]

  def repartition[A: ClassTag](as: DCollection[A])(
      numPartitions: Int): DCollection[A]

  def coalesce[A: ClassTag](as: DCollection[A])(
      numPartitions: Int,
      shuffle: Boolean = false): DCollection[A]

  def setName[A: ClassTag](as: DCollection[A])(
    name: String): DCollection[A]
}
