package com.danielwestheide.kontextfrei.rdd

import com.danielwestheide.kontextfrei.DCollectionBaseFunctions
import org.apache.spark.Partitioner
import org.apache.spark.rdd.RDD

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

  override final def keyBy[A: ClassTag, B](as: RDD[A])(
      f: A => B): RDD[(B, A)] =
    as.keyBy(f)

  override final def union[A: ClassTag](xs: RDD[A])(ys: RDD[A]): RDD[A] =
    xs.union(ys)

  override final def intersection[A: ClassTag](xs: RDD[A])(
      ys: RDD[A]): RDD[A] =
    xs.intersection(ys)

  override final def intersectionWithPartitioner[A: ClassTag](
      xs: RDD[A])(ys: RDD[A], partitioner: Partitioner): RDD[A] =
    xs.intersection(ys, partitioner)

  override final def intersectionWithNumPartitions[A: ClassTag](
      xs: RDD[A])(ys: RDD[A], numPartitions: Int): RDD[A] =
    xs.intersection(ys, numPartitions)

  override final def zip[A: ClassTag, B: ClassTag](xs: RDD[A])(
      ys: RDD[B]): RDD[(A, B)] = xs.zip(ys)

  override final def subtract[A: ClassTag](xs: RDD[A])(ys: RDD[A]): RDD[A] =
    xs.subtract(ys)

  override final def subtractWithNumPartitions[A: ClassTag](
      xs: RDD[A])(ys: RDD[A], numPartitions: Int): RDD[A] =
    xs.subtract(ys, numPartitions)

  override final def subtractWithPartitioner[A: ClassTag](
      xs: RDD[A])(ys: RDD[A], partitioner: Partitioner): RDD[A] =
    xs.subtract(ys, partitioner)

  override final def sortBy[A: ClassTag, B: ClassTag: Ordering](as: RDD[A])(
      f: (A) => B)(ascending: Boolean): RDD[A] =
    as.sortBy(f, ascending)

  override final def sortByWithNumPartitions[A: ClassTag,
                                             B: ClassTag: Ordering](
      as: RDD[A])(f: A => B)(ascending: Boolean)(numPartitions: Int): RDD[A] =
    as.sortBy(f, ascending, numPartitions)

  override final def collectAsArray[A: ClassTag](as: RDD[A]): Array[A] =
    as.collect()

  override final def count[A](as: RDD[A]): Long = as.count()

  override final def countByValue[A: ClassTag](as: RDD[A])(
      implicit ord: Ordering[A]): Map[A, Long] =
    as.countByValue()

  override final def first[A: ClassTag](as: RDD[A]): A = as.first()

  override def take[A: ClassTag](as: RDD[A])(n: Int): Array[A] = as.take(n)

  override def takeOrdered[A: ClassTag](as: RDD[A])(num: Int)(
      implicit ord: Ordering[A]): Array[A] = as.takeOrdered(num)

  override def top[A: ClassTag](as: RDD[A])(num: Int)(
      implicit ord: Ordering[A]): Array[A] = as.top(num)

  override def repartition[A: ClassTag](as: RDD[A])(
      numPartitions: Int): RDD[A] = as.repartition(numPartitions)

  override def coalesce[A: ClassTag](as: RDD[A])(numPartitions: Int,
                                                 shuffle: Boolean = false) =
    as.coalesce(numPartitions, shuffle)

}
