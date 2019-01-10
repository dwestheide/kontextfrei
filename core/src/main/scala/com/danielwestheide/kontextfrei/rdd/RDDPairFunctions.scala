package com.danielwestheide.kontextfrei.rdd

import com.danielwestheide.kontextfrei.DCollectionPairFunctions
import org.apache.spark.Partitioner
import org.apache.spark.rdd.RDD

import scala.collection.Map
import scala.reflect.ClassTag

private[kontextfrei] trait RDDPairFunctions
    extends DCollectionPairFunctions[RDD] {

  override final def cogroup[A: ClassTag, B: ClassTag, C: ClassTag](
      x: RDD[(A, B)])(y: RDD[(A, C)]): RDD[(A, (Iterable[B], Iterable[C]))] =
    x.cogroup(y)

  override final def values[A: ClassTag, B: ClassTag](x: RDD[(A, B)]): RDD[B] =
    x.values

  override final def keys[A: ClassTag, B: ClassTag](x: RDD[(A, B)]): RDD[A] =
    x.keys

  override final def leftOuterJoin[A: ClassTag, B: ClassTag, C: ClassTag](
      x: RDD[(A, B)])(y: RDD[(A, C)]): RDD[(A, (B, Option[C]))] =
    x.leftOuterJoin(y)

  override final def rightOuterJoin[A: ClassTag, B: ClassTag, C: ClassTag](
      x: RDD[(A, B)])(y: RDD[(A, C)]): RDD[(A, (Option[B], C))] =
    x.rightOuterJoin(y)

  override final def fullOuterJoin[A: ClassTag, B: ClassTag, C: ClassTag](
      x: RDD[(A, B)])(y: RDD[(A, C)]): RDD[(A, (Option[B], Option[C]))] =
    x.fullOuterJoin(y)

  override final def mapValues[A: ClassTag, B: ClassTag, C: ClassTag](
      x: RDD[(A, B)])(f: B => C): RDD[(A, C)] = x.mapValues(f)

  override final def flatMapValues[A: ClassTag, B: ClassTag, C: ClassTag](
      x: RDD[(A, B)])(f: B => TraversableOnce[C]): RDD[(A, C)] =
    x.flatMapValues(f)

  override final def reduceByKey[A: ClassTag, B: ClassTag](xs: RDD[(A, B)])(
      f: (B, B) => B): RDD[(A, B)] =
    xs.reduceByKey(f)

  override final def foldByKey[A: ClassTag, B: ClassTag](
      xs: RDD[(A, B)])(zeroValue: B, f: (B, B) => B): RDD[(A, B)] =
    xs.foldByKey(zeroValue)(f)

  override final def aggregateByKey[A: ClassTag, B: ClassTag, C: ClassTag](
      xs: RDD[(A, B)])(zeroValue: C)(seqOp: (C, B) => C,
                                     combOp: (C, C) => C): RDD[(A, C)] =
    xs.aggregateByKey(zeroValue)(seqOp, combOp)

  override final def combineByKey[A: ClassTag, B: ClassTag, C: ClassTag](
      xs: RDD[(A, B)])(createCombiner: B => C)(
      mergeValue: (C, B) => C,
      mergeCombiners: (C, C) => C): RDD[(A, C)] =
    xs.combineByKey(createCombiner, mergeValue, mergeCombiners)

  override final def countByKey[A: ClassTag, B: ClassTag](
      xs: RDD[(A, B)]): Map[A, Long] =
    xs.countByKey()

  override final def collectAsMap[A: ClassTag, B: ClassTag](
      xs: RDD[(A, B)]): Map[A, B] =
    xs.collectAsMap()

  override final def partitionBy[A: ClassTag, B: ClassTag](
      xs: RDD[(A, B)])(partitioner: Partitioner): RDD[(A, B)] = xs.partitionBy(partitioner)
}
