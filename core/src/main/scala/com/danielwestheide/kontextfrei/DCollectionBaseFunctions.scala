package com.danielwestheide.kontextfrei

import scala.collection.Map
import scala.reflect.ClassTag

private[kontextfrei] trait DCollectionBaseFunctions[DCollection[_]] {

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

  def sortBy[A: ClassTag, B: ClassTag: Ordering](as: DCollection[A])(
      f: A => B)(ascending: Boolean): DCollection[A]

  def collectAsArray[A: ClassTag](as: DCollection[A]): Array[A]

  def count[A](as: DCollection[A]): Long

  def countByValue[A: ClassTag](as: DCollection[A])(
      implicit ord: Ordering[A]): Map[A, Long]

  def first[A: ClassTag](as: DCollection[A]): A

}
