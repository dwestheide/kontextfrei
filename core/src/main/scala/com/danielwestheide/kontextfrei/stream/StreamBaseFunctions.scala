package com.danielwestheide.kontextfrei.stream

import com.danielwestheide.kontextfrei.DCollectionBaseFunctions
import org.apache.spark.{Partitioner, SparkException}

import scala.reflect.ClassTag

private[kontextfrei] trait StreamBaseFunctions
    extends DCollectionBaseFunctions[Stream] {

  override final def cartesian[A: ClassTag, B: ClassTag](as: Stream[A])(
      bs: Stream[B]): Stream[(A, B)] =
    for {
      a <- as
      b <- bs
    } yield (a, b)

  override final def collect[A: ClassTag, B: ClassTag](as: Stream[A])(
      pf: PartialFunction[A, B]): Stream[B] =
    as.collect(pf)

  override final def distinct[A: ClassTag](as: Stream[A]): Stream[A] =
    as.distinct

  override def distinctWithNumPartitions[A: ClassTag](as: Stream[A])(
      numPartitions: Int): Stream[A] = as.distinct

  override final def map[A: ClassTag, B: ClassTag](as: Stream[A])(
      f: A => B): Stream[B] =
    as.map(f)

  override final def flatMap[A: ClassTag, B: ClassTag](as: Stream[A])(
      f: (A) => TraversableOnce[B]): Stream[B] =
    as.flatMap(f)

  override final def filter[A: ClassTag](as: Stream[A])(
      f: A => Boolean): Stream[A] =
    as.filter(f)

  override final def groupBy[A, B: ClassTag](as: Stream[A])(
      f: (A) => B): Stream[(B, Iterable[A])] =
    as.groupBy(f).toStream

  override final def groupByWithNumPartitions[A, B: ClassTag](as: Stream[A])(
      f: (A) => B,
      numPartitions: Int): Stream[(B, Iterable[A])] =
    groupBy(as)(f)

  override final def groupByWithPartitioner[A, B: ClassTag](as: Stream[A])(
      f: (A) => B,
      partitioner: Partitioner): Stream[(B, Iterable[A])] =
    groupBy(as)(f)

  override final def mapPartitions[A: ClassTag, B: ClassTag](as: Stream[A])(
      f: Iterator[A] => Iterator[B],
      preservesPartitioning: Boolean = false): Stream[B] = {
    f(as.toIterator).toStream
  }

  override final def keyBy[A: ClassTag, B](as: Stream[A])(
      f: A => B): Stream[(B, A)] =
    as.map(a => f(a) -> a)

  override final def union[A: ClassTag](xs: Stream[A])(
      ys: Stream[A]): Stream[A] =
    xs.union(ys)

  override final def intersection[A: ClassTag](xs: Stream[A])(
      ys: Stream[A]): Stream[A] =
    xs.distinct.intersect(ys.distinct)

  override final def intersectionWithPartitioner[A: ClassTag](
      xs: Stream[A])(ys: Stream[A], partitioner: Partitioner): Stream[A] =
    intersection(xs)(ys)

  override final def intersectionWithNumPartitions[A: ClassTag](
      xs: Stream[A])(ys: Stream[A], numPartitions: Int): Stream[A] =
    intersection(xs)(ys)

  override final def zip[A: ClassTag, B: ClassTag](xs: Stream[A])(
      ys: Stream[B]): Stream[(A, B)] = {
    val result = xs.zip(ys)
    if (result.size < xs.size || result.size < ys.size)
      throw new SparkException(
        "Zipping only works if both collections have same number of elements")
    else result
  }

  override final def subtract[A: ClassTag](xs: Stream[A])(
      ys: Stream[A]): Stream[A] = xs.filterNot(ys.toSet)

  override final def subtractWithNumPartitions[A: ClassTag](
      xs: Stream[A])(ys: Stream[A], numPartitions: Int): Stream[A] =
    subtract(xs)(ys)

  override final def subtractWithPartitioner[A: ClassTag](
      xs: Stream[A])(ys: Stream[A], partitioner: Partitioner): Stream[A] =
    subtract(xs)(ys)

  override final def sortBy[A: ClassTag, B: ClassTag: Ordering](as: Stream[A])(
      f: (A) => B)(ascending: Boolean): Stream[A] = {
    val ordering = implicitly[Ordering[B]]
    as.sortBy(f)(if (ascending) ordering else ordering.reverse)
  }

  override final def sortByWithNumPartitions[A: ClassTag,
                                             B: ClassTag: Ordering](
      as: Stream[A])(f: A => B)(ascending: Boolean)(
      numPartitions: Int): Stream[A] = {
    val ordering = implicitly[Ordering[B]]
    as.sortBy(f)(if (ascending) ordering else ordering.reverse)
  }

  override final def collectAsArray[A: ClassTag](as: Stream[A]): Array[A] =
    as.toArray

  override final def count[A](as: Stream[A]): Long = as.size
  override final def countByValue[A: ClassTag](as: Stream[A])(
      implicit ord: Ordering[A]): collection.Map[A, Long] =
    as.groupBy(identity) map { case (k, v) => (k, v.size.toLong) }

  override final def first[A: ClassTag](as: Stream[A]): A =
    as.headOption getOrElse {
      throw new UnsupportedOperationException("empty collection")
    }

  override def take[A: ClassTag](as: Stream[A])(n: Int): Array[A] =
    as.take(n).toArray

  override def takeOrdered[A: ClassTag](as: Stream[A])(num: Int)(
      implicit ord: Ordering[A]): Array[A] = as.sorted.take(num).toArray

  override def top[A: ClassTag](as: Stream[A])(num: Int)(
      implicit ord: Ordering[A]): Array[A] =
    as.sorted(ord.reverse).take(num).toArray

  override def repartition[A: ClassTag](as: Stream[A])(
      numPartitions: Int): Stream[A] = as

  override def coalesce[A: ClassTag](
      as: Stream[A])(numPartitions: Int, shuffle: Boolean = false): Stream[A] =
    as

}
