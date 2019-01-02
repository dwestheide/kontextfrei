package com.danielwestheide.kontextfrei.rdd

import com.danielwestheide.kontextfrei.DCollectionBaseFunctions
import org.apache.spark.Partitioner
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel

import scala.collection.Map
import scala.reflect.ClassTag

private[kontextfrei] trait RDDBaseFunctions
    extends DCollectionBaseFunctions[RDD] {
  override final def cartesian[A: ClassTag, B: ClassTag](as: RDD[A])(
      bs: RDD[B]): RDD[(A, B)] =
    as.cartesian(bs)

  override final def collect[A: ClassTag, B: ClassTag](as: RDD[A])(
      pf: PartialFunction[A, B]): RDD[B] =
    as.collect(pf)

  override final def distinct[A: ClassTag](as: RDD[A]): RDD[A] = as.distinct()

  override def distinctWithNumPartitions[A: ClassTag](as: RDD[A])(
      numPartitions: Int): RDD[A] = as.distinct(numPartitions)

  override final def map[A: ClassTag, B: ClassTag](as: RDD[A])(
      f: A => B): RDD[B] =
    as.map(f)

  override final def flatMap[A: ClassTag, B: ClassTag](as: RDD[A])(
      f: A => TraversableOnce[B]): RDD[B] =
    as.flatMap(f)

  override final def filter[A: ClassTag](as: RDD[A])(f: A => Boolean): RDD[A] =
    as.filter(f)

  override final def groupBy[A, B: ClassTag](as: RDD[A])(
      f: A => B): RDD[(B, Iterable[A])] =
    as.groupBy(f)

  override final def groupByWithNumPartitions[A, B: ClassTag](
      as: RDD[A])(f: A => B, numPartitions: Int): RDD[(B, Iterable[A])] =
    as.groupBy(f, numPartitions)

  override final def groupByWithPartitioner[A, B: ClassTag](
      as: RDD[A])(f: A => B, partitioner: Partitioner): RDD[(B, Iterable[A])] =
    as.groupBy(f, partitioner)

  override final def mapPartitions[A: ClassTag, B: ClassTag](as: RDD[A])(
      f: Iterator[A] => Iterator[B],
      preservesPartitioning: Boolean = false): RDD[B] =
    as.mapPartitions(f, preservesPartitioning)

  override final def keyBy[A: ClassTag, B](as: RDD[A])(f: A => B): RDD[(B, A)] =
    as.keyBy(f)

  override final def union[A: ClassTag](xs: RDD[A])(ys: RDD[A]): RDD[A] =
    xs.union(ys)

  override final def intersection[A: ClassTag](xs: RDD[A])(ys: RDD[A]): RDD[A] =
    xs.intersection(ys)

  override final def intersectionWithPartitioner[A: ClassTag](
      xs: RDD[A])(ys: RDD[A], partitioner: Partitioner): RDD[A] =
    xs.intersection(ys, partitioner)

  override final def intersectionWithNumPartitions[A: ClassTag](
      xs: RDD[A])(ys: RDD[A], numPartitions: Int): RDD[A] =
    xs.intersection(ys, numPartitions)

  override final def zip[A: ClassTag, B: ClassTag](xs: RDD[A])(
      ys: RDD[B]): RDD[(A, B)] = xs.zip(ys)

  override final def zipWithIndex[A: ClassTag](xs: RDD[A]): RDD[(A, Long)] =
    xs.zipWithIndex()

  override final def zipWithUniqueId[A: ClassTag](xs: RDD[A]): RDD[(A, Long)] =
    xs.zipWithUniqueId()

  override final def zipPartitions[A: ClassTag, B: ClassTag, C: ClassTag](
      as: RDD[A])(bs: RDD[B])(
      f: (Iterator[A], Iterator[B]) => Iterator[C]): RDD[C] =
    as.zipPartitions(bs)(f)

  override final def zipPartitionsWithPreservesPartitioning[A: ClassTag,
                                                            B: ClassTag,
                                                            C: ClassTag](
      as: RDD[A])(bs: RDD[B], preservesPartitioning: Boolean)(
      f: (Iterator[A], Iterator[B]) => Iterator[C]): RDD[C] =
    as.zipPartitions(bs, preservesPartitioning)(f)

  override final def zipPartitions3[A: ClassTag,
                                    B: ClassTag,
                                    C: ClassTag,
                                    D: ClassTag](as: RDD[A])(bs: RDD[B],
                                                             cs: RDD[C])(
      f: (Iterator[A], Iterator[B], Iterator[C]) => Iterator[D]): RDD[D] =
    as.zipPartitions(bs, cs)(f)

  override final def zipPartitions3WithPreservesPartitioning[A: ClassTag,
                                                             B: ClassTag,
                                                             C: ClassTag,
                                                             D: ClassTag](
      as: RDD[A])(bs: RDD[B], cs: RDD[C], preservesPartitioning: Boolean)(
      f: (Iterator[A], Iterator[B], Iterator[C]) => Iterator[D]): RDD[D] =
    as.zipPartitions(bs, cs, preservesPartitioning)(f)

  override final def zipPartitions4[A: ClassTag,
                                    B: ClassTag,
                                    C: ClassTag,
                                    D: ClassTag,
                                    E: ClassTag](as: RDD[A])(
      bs: RDD[B],
      cs: RDD[C],
      ds: RDD[D])(f: (Iterator[A],
                      Iterator[B],
                      Iterator[C],
                      Iterator[D]) => Iterator[E]): RDD[E] =
    as.zipPartitions(bs, cs, ds)(f)

  override final def zipPartitions4WithPreservesPartitioning[A: ClassTag,
                                                             B: ClassTag,
                                                             C: ClassTag,
                                                             D: ClassTag,
                                                             E: ClassTag](
      as: RDD[A])(
      bs: RDD[B],
      cs: RDD[C],
      ds: RDD[D],
      preservesPartitioning: Boolean)(f: (Iterator[A],
                                          Iterator[B],
                                          Iterator[C],
                                          Iterator[D]) => Iterator[E]): RDD[E] =
    as.zipPartitions(bs, cs, ds, preservesPartitioning)(f)

  override final def subtract[A: ClassTag](xs: RDD[A])(ys: RDD[A]): RDD[A] =
    xs.subtract(ys)

  override final def subtractWithNumPartitions[A: ClassTag](
      xs: RDD[A])(ys: RDD[A], numPartitions: Int): RDD[A] =
    xs.subtract(ys, numPartitions)

  override final def subtractWithPartitioner[A: ClassTag](
      xs: RDD[A])(ys: RDD[A], partitioner: Partitioner): RDD[A] =
    xs.subtract(ys, partitioner)

  override final def persist[A: ClassTag](xs: RDD[A]): RDD[A] = xs.persist()

  override final def persistWithStorageLevel[A: ClassTag](xs: RDD[A])(
      level: StorageLevel): RDD[A] = xs.persist(level)

  override final def unpersist[A: ClassTag](xs: RDD[A])(
      blocking: Boolean = true): RDD[A] =
    xs.unpersist(blocking)

  override final def glom[A: ClassTag](xs: RDD[A]): RDD[Array[A]] = xs.glom()

  override final def sortBy[A: ClassTag, B: ClassTag: Ordering](as: RDD[A])(
      f: (A) => B)(ascending: Boolean): RDD[A] =
    as.sortBy(f, ascending)

  override final def sortByWithNumPartitions[A: ClassTag,
                                             B: ClassTag: Ordering](as: RDD[A])(
      f: A => B)(ascending: Boolean)(numPartitions: Int): RDD[A] =
    as.sortBy(f, ascending, numPartitions)

  override final def collectAsArray[A: ClassTag](as: RDD[A]): Array[A] =
    as.collect()

  override final def count[A](as: RDD[A]): Long = as.count()

  override final def countByValue[A: ClassTag](as: RDD[A])(
      implicit ord: Ordering[A]): Map[A, Long] =
    as.countByValue()

  override final def reduce[A: ClassTag](as: RDD[A])(f: (A, A) => A): A =
    as.reduce(f)

  override final def fold[A: ClassTag](as: RDD[A])(zeroValue: A)(
      op: (A, A) => A): A = as.fold(zeroValue)(op)

  override final def aggregate[A: ClassTag, B: ClassTag](as: RDD[A])(
      zeroValue: B)(seqOp: (B, A) => B, combOp: (B, B) => B): B =
    as.aggregate(zeroValue)(seqOp, combOp)

  override final def treeReduce[A: ClassTag](
      as: RDD[A])(f: (A, A) => A, depth: Int = 2): A = as.treeReduce(f, depth)

  override final def treeAggregate[A: ClassTag, B: ClassTag](as: RDD[A])(
      zeroValue: B)(seqOp: (B, A) => B,
                    combOp: (B, B) => B,
                    depth: Int = 2): B =
    as.treeAggregate(zeroValue)(seqOp, combOp, depth)

  override final def first[A: ClassTag](as: RDD[A]): A = as.first()

  override def take[A: ClassTag](as: RDD[A])(n: Int): Array[A] = as.take(n)

  override def takeOrdered[A: ClassTag](as: RDD[A])(num: Int)(
      implicit ord: Ordering[A]): Array[A] = as.takeOrdered(num)

  override def top[A: ClassTag](as: RDD[A])(num: Int)(
      implicit ord: Ordering[A]): Array[A] = as.top(num)

  override final def min[A: ClassTag](as: RDD[A])(
      implicit ord: Ordering[A]): A = as.min()

  override final def max[A: ClassTag](as: RDD[A])(
      implicit ord: Ordering[A]): A = as.max()

  override final def foreach[A: ClassTag](as: RDD[A])(f: A => Unit): Unit =
    as.foreach(f)

  override final def foreachPartition[A: ClassTag](as: RDD[A])(
      f: Iterator[A] => Unit): Unit = as.foreachPartition(f)

  override final def isEmpty[A: ClassTag](as: RDD[A]): Boolean = as.isEmpty()

  override final def toLocalIterator[A: ClassTag](as: RDD[A]): Iterator[A] =
    as.toLocalIterator

  override final def repartition[A: ClassTag](as: RDD[A])(
      numPartitions: Int): RDD[A] = as.repartition(numPartitions)

  override final def coalesce[A: ClassTag](
      as: RDD[A])(numPartitions: Int, shuffle: Boolean = false): RDD[A] =
    as.coalesce(numPartitions, shuffle)

  override final def setName[A: ClassTag](as: RDD[A])(name: String): RDD[A] = as.setName(name)

  override final def defaultPartitioner[A: ClassTag](
      as: RDD[A])(others: RDD[_]*): Partitioner =
    Partitioner.defaultPartitioner(as, others:_*)
}
