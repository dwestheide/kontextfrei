package com.danielwestheide.kontextfrei.rdd

import com.danielwestheide.kontextfrei.DCollectionPairFunctions
import org.apache.spark.Partitioner
import org.apache.spark.rdd.RDD

import scala.collection.Map
import scala.reflect.ClassTag

private[kontextfrei] trait RDDPairFunctions
    extends DCollectionPairFunctions[RDD] { this: RDDBase =>

  override final def cogroup[A: ClassTag, B: ClassTag, C: ClassTag](
      x: RDD[(A, B)])(y: RDD[(A, C)]): RDD[(A, (Iterable[B], Iterable[C]))] = withSite(x) {
    _.cogroup(y)
  }

  override final def values[A: ClassTag, B: ClassTag](x: RDD[(A, B)]): RDD[B] = withSite(x) {
    _.values
  }

  override final def keys[A: ClassTag, B: ClassTag](x: RDD[(A, B)]): RDD[A] = withSite(x) {
    _.keys
  }

  override final def leftOuterJoin[A: ClassTag, B: ClassTag, C: ClassTag](
      x: RDD[(A, B)])(y: RDD[(A, C)]): RDD[(A, (B, Option[C]))] = withSite(x) {
    _.leftOuterJoin(y)
  }

  override final def rightOuterJoin[A: ClassTag, B: ClassTag, C: ClassTag](
      x: RDD[(A, B)])(y: RDD[(A, C)]): RDD[(A, (Option[B], C))] = withSite(x) {
    _.rightOuterJoin(y)
  }

  override final def fullOuterJoin[A: ClassTag, B: ClassTag, C: ClassTag](
      x: RDD[(A, B)])(y: RDD[(A, C)]): RDD[(A, (Option[B], Option[C]))] = withSite(x) {
    _.fullOuterJoin(y)
  }

  override final def mapValues[A: ClassTag, B: ClassTag, C: ClassTag](
      x: RDD[(A, B)])(f: B => C): RDD[(A, C)] = withSite(x) {
    _.mapValues(f)
  }

  override final def flatMapValues[A: ClassTag, B: ClassTag, C: ClassTag](
      x: RDD[(A, B)])(f: B => TraversableOnce[C]): RDD[(A, C)] = withSite(x) {
    _.flatMapValues(f)
  }

  override final def reduceByKey[A: ClassTag, B: ClassTag](xs: RDD[(A, B)])(
      f: (B, B) => B): RDD[(A, B)] = withSite(xs) {
    _.reduceByKey(f)
  }

  override final def foldByKey[A: ClassTag, B: ClassTag](
      xs: RDD[(A, B)])(zeroValue: B, f: (B, B) => B): RDD[(A, B)] = withSite(xs) {
    _.foldByKey(zeroValue)(f)
  }

  override final def aggregateByKey[A: ClassTag, B: ClassTag, C: ClassTag](
      xs: RDD[(A, B)])(zeroValue: C)(seqOp: (C, B) => C,
                                     combOp: (C, C) => C): RDD[(A, C)] = withSite(xs) {
    _.aggregateByKey(zeroValue)(seqOp, combOp)
  }

  override final def combineByKey[A: ClassTag, B: ClassTag, C: ClassTag](
      xs: RDD[(A, B)])(createCombiner: B => C)(
      mergeValue: (C, B) => C,
      mergeCombiners: (C, C) => C): RDD[(A, C)] = withSite(xs) {
    _.combineByKey(createCombiner, mergeValue, mergeCombiners)
  }

  override final def countByKey[A: ClassTag, B: ClassTag](
      xs: RDD[(A, B)]): Map[A, Long] = withSite(xs) {
    _.countByKey()
  }

  override final def collectAsMap[A: ClassTag, B: ClassTag](
      xs: RDD[(A, B)]): Map[A, B] = withSite(xs) {
    _.collectAsMap()
  }

  override final def partitionBy[A: ClassTag, B: ClassTag](
      xs: RDD[(A, B)])(partitioner: Partitioner): RDD[(A, B)] = withSite(xs) {
    _.partitionBy(partitioner)
  }
}
