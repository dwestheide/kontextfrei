package com.danielwestheide.kontextfrei

import org.apache.spark.Partitioner

import scala.collection.Map
import scala.reflect.ClassTag

private[kontextfrei] trait DCollectionPairFunctions[DCollection[_]] {

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
      zeroValue: B,
      func: (B, B) => B): DCollection[(A, B)]

  def reduceByKey[A: ClassTag, B: ClassTag](xs: DCollection[(A, B)])(
      f: (B, B) => B): DCollection[(A, B)]

  def aggregateByKey[A: ClassTag, B: ClassTag, C: ClassTag](
      xs: DCollection[(A, B)])(zeroValue: C)(
      seqOp: (C, B) => C,
      combOp: (C, C) => C): DCollection[(A, C)]

  def combineByKey[A: ClassTag, B: ClassTag, C: ClassTag](
      xs: DCollection[(A, B)])(createCombiner: B => C)(
      mergeValue: (C, B) => C,
      mergeCombiners: (C, C) => C): DCollection[(A, C)]

  def countByKey[A: ClassTag, B: ClassTag](
      xs: DCollection[(A, B)]): Map[A, Long]

  def collectAsMap[A: ClassTag, B: ClassTag](
      xs: DCollection[(A, B)]): Map[A, B]

  def partitionBy[A: ClassTag, B: ClassTag](
    xs: DCollection[(A, B)])(partitioner: Partitioner): DCollection[(A, B)]
}
