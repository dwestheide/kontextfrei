package com.danielwestheide.kontextfrei.syntax

import com.danielwestheide.kontextfrei.DCollectionOps
import org.apache.spark.Partitioner

import scala.collection.Map
import scala.reflect.ClassTag

class PairSyntax[DCollection[_], A: ClassTag, B: ClassTag](
    val self: DCollectionOps[DCollection],
    val coll: DCollection[(A, B)]) {

  final def keys: DCollection[A] = self.keys(coll)

  final def values: DCollection[B] = self.values(coll)

  final def cogroup[C: ClassTag](other: DCollection[(A, C)])
    : DCollection[(A, (Iterable[B], Iterable[C]))] =
    self.cogroup(coll)(other)

  final def leftOuterJoin[C: ClassTag](
      other: DCollection[(A, C)]): DCollection[(A, (B, Option[C]))] =
    self.leftOuterJoin(coll)(other)

  final def rightOuterJoin[C: ClassTag](
      other: DCollection[(A, C)]): DCollection[(A, (Option[B], C))] =
    self.rightOuterJoin(coll)(other)

  final def fullOuterJoin[C: ClassTag](
      other: DCollection[(A, C)]): DCollection[(A, (Option[B], Option[C]))] =
    self.fullOuterJoin(coll)(other)

  final def mapValues[C: ClassTag](f: B => C): DCollection[(A, C)] =
    self.mapValues(coll)(f)

  final def flatMapValues[C: ClassTag](
      f: B => TraversableOnce[C]): DCollection[(A, C)] =
    self.flatMapValues(coll)(f)

  final def reduceByKey(f: (B, B) => B): DCollection[(A, B)] =
    self.reduceByKey(coll)(f)

  final def foldByKey(zeroValue: B)(f: (B, B) => B): DCollection[(A, B)] =
    self.foldByKey(coll)(zeroValue, f)

  final def aggregateByKey[C: ClassTag](zeroValue: C)(
      seqOp: (C, B) => C,
      combOp: (C, C) => C): DCollection[(A, C)] =
    self.aggregateByKey(coll)(zeroValue)(seqOp, combOp)

  final def combineByKey[C: ClassTag](
      createCombiner: B => C,
      mergeValue: (C, B) => C,
      mergeCombiners: (C, C) => C): DCollection[(A, C)] =
    self.combineByKey(coll)(createCombiner)(mergeValue, mergeCombiners)

  final def countByKey(): Map[A, Long] = self.countByKey(coll)

  final def collectAsMap(): Map[A, B] = self.collectAsMap(coll)

  final def partitionBy(partitioner: Partitioner): DCollection[(A, B)] =
    self.partitionBy(coll)(partitioner)
}
