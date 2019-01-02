package com.danielwestheide.kontextfrei.stream

import com.danielwestheide.kontextfrei.DCollectionBaseFunctions
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{HashPartitioner, Partitioner, SparkException}

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

  override final def distinctWithNumPartitions[A: ClassTag](as: Stream[A])(
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
    if (result.lengthCompare(xs.size) < 0 || result.lengthCompare(ys.size) < 0)
      throw new SparkException(
        "Zipping only works if both collections have same number of elements")
    else result
  }

  override final def zipWithIndex[A: ClassTag](
      xs: Stream[A]): Stream[(A, Long)] = xs.zipWithIndex.map {
    case (k, v) => (k, v.toLong)
  }

  override final def zipWithUniqueId[A: ClassTag](
      xs: Stream[A]): Stream[(A, Long)] =
    zipWithIndex(xs)

  override final def zipPartitions[A: ClassTag, B: ClassTag, C: ClassTag](
      as: Stream[A])(bs: Stream[B])(
      f: (Iterator[A], Iterator[B]) => Iterator[C]): Stream[C] =
    f(as.toIterator, bs.toIterator).toStream

  override final def zipPartitionsWithPreservesPartitioning[A: ClassTag,
                                                            B: ClassTag,
                                                            C: ClassTag](
      as: Stream[A])(bs: Stream[B], preservesPartitioning: Boolean)(
      f: (Iterator[A], Iterator[B]) => Iterator[C]): Stream[C] =
    zipPartitions(as)(bs)(f)

  override final def zipPartitions3[A: ClassTag,
                                    B: ClassTag,
                                    C: ClassTag,
                                    D: ClassTag](as: Stream[A])(bs: Stream[B],
                                                                cs: Stream[C])(
      f: (Iterator[A], Iterator[B], Iterator[C]) => Iterator[D]): Stream[D] =
    f(as.toIterator, bs.toIterator, cs.toIterator).toStream

  override final def zipPartitions3WithPreservesPartitioning[A: ClassTag,
                                                             B: ClassTag,
                                                             C: ClassTag,
                                                             D: ClassTag](
      as: Stream[A])(bs: Stream[B],
                     cs: Stream[C],
                     preservesPartitioning: Boolean)(
      f: (Iterator[A], Iterator[B], Iterator[C]) => Iterator[D]): Stream[D] =
    zipPartitions3(as)(bs, cs)(f)

  override final def zipPartitions4[A: ClassTag,
                                    B: ClassTag,
                                    C: ClassTag,
                                    D: ClassTag,
                                    E: ClassTag](as: Stream[A])(
      bs: Stream[B],
      cs: Stream[C],
      ds: Stream[D])(f: (Iterator[A],
                         Iterator[B],
                         Iterator[C],
                         Iterator[D]) => Iterator[E]): Stream[E] =
    f(as.toIterator, bs.toIterator, cs.toIterator, ds.toIterator).toStream

  override final def zipPartitions4WithPreservesPartitioning[A: ClassTag,
                                                             B: ClassTag,
                                                             C: ClassTag,
                                                             D: ClassTag,
                                                             E: ClassTag](
      as: Stream[A])(bs: Stream[B],
                     cs: Stream[C],
                     ds: Stream[D],
                     preservesPartitioning: Boolean)(
      f: (Iterator[A], Iterator[B], Iterator[C], Iterator[D]) => Iterator[E])
    : Stream[E] = zipPartitions4(as)(bs, cs, ds)(f)

  override final def subtract[A: ClassTag](xs: Stream[A])(
      ys: Stream[A]): Stream[A] = xs.filterNot(ys.toSet)

  override final def subtractWithNumPartitions[A: ClassTag](
      xs: Stream[A])(ys: Stream[A], numPartitions: Int): Stream[A] =
    subtract(xs)(ys)

  override final def subtractWithPartitioner[A: ClassTag](
      xs: Stream[A])(ys: Stream[A], partitioner: Partitioner): Stream[A] =
    subtract(xs)(ys)

  override final def persist[A: ClassTag](xs: Stream[A]): Stream[A] = xs

  override final def persistWithStorageLevel[A: ClassTag](xs: Stream[A])(
      level: StorageLevel): Stream[A] = xs

  override final def unpersist[A: ClassTag](xs: Stream[A])(
      blocking: Boolean = true): Stream[A] = xs

  override final def glom[A: ClassTag](xs: Stream[A]): Stream[Array[A]] =
    Stream(xs.toArray)

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

  override final def reduce[A: ClassTag](as: Stream[A])(f: (A, A) => A): A =
    as.reduceLeft(f)

  override final def fold[A: ClassTag](as: Stream[A])(zeroValue: A)(
      op: (A, A) => A): A =
    as.foldLeft(zeroValue)(op)

  override final def aggregate[A: ClassTag, B: ClassTag](as: Stream[A])(
      zeroValue: B)(seqOp: (B, A) => B, combOp: (B, B) => B): B =
    as.aggregate(zeroValue)(seqOp, combOp)

  override final def treeReduce[A: ClassTag](as: Stream[A])(f: (A, A) => A,
                                                            depth: Int = 2): A =
    as.reduceLeft(f)

  override final def treeAggregate[A: ClassTag, B: ClassTag](as: Stream[A])(
      zeroValue: B)(seqOp: (B, A) => B,
                    combOp: (B, B) => B,
                    depth: Int = 2): B =
    as.aggregate(zeroValue)(seqOp, combOp)

  override final def first[A: ClassTag](as: Stream[A]): A =
    as.headOption getOrElse {
      throw new UnsupportedOperationException("empty collection")
    }

  override final def take[A: ClassTag](as: Stream[A])(n: Int): Array[A] =
    as.take(n).toArray

  override final def takeOrdered[A: ClassTag](as: Stream[A])(num: Int)(
      implicit ord: Ordering[A]): Array[A] = as.sorted.take(num).toArray

  override final def top[A: ClassTag](as: Stream[A])(num: Int)(
      implicit ord: Ordering[A]): Array[A] =
    as.sorted(ord.reverse).take(num).toArray

  override final def min[A: ClassTag](as: Stream[A])(
      implicit ord: Ordering[A]): A = as.min

  override final def max[A: ClassTag](as: Stream[A])(
      implicit ord: Ordering[A]): A = as.max

  override final def foreach[A: ClassTag](as: Stream[A])(f: A => Unit): Unit =
    as.foreach(f)

  override final def foreachPartition[A: ClassTag](as: Stream[A])(
      f: Iterator[A] => Unit): Unit = f(as.toIterator)

  override final def isEmpty[A: ClassTag](as: Stream[A]): Boolean = as.isEmpty

  override final def toLocalIterator[A: ClassTag](as: Stream[A]): Iterator[A] =
    as.toIterator

  override final def repartition[A: ClassTag](as: Stream[A])(
      numPartitions: Int): Stream[A] = as

  override final def coalesce[A: ClassTag](
      as: Stream[A])(numPartitions: Int, shuffle: Boolean = false): Stream[A] =
    as

  override def setName[A: ClassTag](as: Stream[A])(
      name: String): Stream[A] = as

  override final def defaultPartitioner[A: ClassTag](
      as: Stream[A])(others: Stream[_]*): Partitioner =
    new Partitioner {
      override def numPartitions: Int          = ???
      override def getPartition(key: Any): Int = ???
    }
}
