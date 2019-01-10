package com.danielwestheide.kontextfrei.stream

import com.danielwestheide.kontextfrei.DCollectionPairFunctions
import org.apache.spark.Partitioner

import scala.collection.Map
import scala.reflect.ClassTag

private[kontextfrei] trait StreamPairFunctions
    extends DCollectionPairFunctions[Stream] {

  override final def cogroup[A: ClassTag, B: ClassTag, C: ClassTag](
      x: Stream[(A, B)])(
      y: Stream[(A, C)]): Stream[(A, (Iterable[B], Iterable[C]))] = {
    val xs      = x.groupBy(_._1).mapValues(_.map(_._2))
    val ys      = y.groupBy(_._1).mapValues(_.map(_._2))
    val allKeys = (xs.keys ++ ys.keys).toStream
    allKeys.map { key =>
      val xsWithKey = xs.getOrElse(key, Stream.empty)
      val ysWithKey = ys.getOrElse(key, Stream.empty)
      key -> (xsWithKey, ysWithKey)
    }
  }

  override final def values[A: ClassTag, B: ClassTag](
      x: Stream[(A, B)]): Stream[B] =
    x.map(_._2)

  override final def keys[A: ClassTag, B: ClassTag](
      x: Stream[(A, B)]): Stream[A] =
    x.map(_._1)

  override final def leftOuterJoin[A: ClassTag, B: ClassTag, C: ClassTag](
      x: Stream[(A, B)])(y: Stream[(A, C)]): Stream[(A, (B, Option[C]))] = {
    flatMapValues(cogroup(x)(y)) {
      case (bs, cs) =>
        if (cs.isEmpty) bs.iterator.map(b => (b, None))
        else
          for {
            b <- bs.iterator
            c <- cs.iterator
          } yield (b, Some(c))
    }
  }
  override final def rightOuterJoin[A: ClassTag, B: ClassTag, C: ClassTag](
      x: Stream[(A, B)])(y: Stream[(A, C)]): Stream[(A, (Option[B], C))] = {
    flatMapValues(cogroup(x)(y)) {
      case (bs, cs) =>
        if (bs.isEmpty) cs.iterator.map(c => (None, c))
        else
          for {
            b <- bs.iterator
            c <- cs.iterator
          } yield (Some(b), c)
    }
  }
  override final def fullOuterJoin[A: ClassTag, B: ClassTag, C: ClassTag](
      x: Stream[(A, B)])(
      y: Stream[(A, C)]): Stream[(A, (Option[B], Option[C]))] = {
    flatMapValues(cogroup(x)(y)) {
      case (bs, Seq()) =>
        bs.iterator.map(v => (Some(v), None))
      case (Seq(), cs) =>
        cs.iterator.map(w => (None, Some(w)))
      case (bs, cs) =>
        for {
          b <- bs.iterator
          c <- cs.iterator
        } yield (Some(b), Some(c))
    }
  }
  override final def mapValues[A: ClassTag, B: ClassTag, C: ClassTag](
      x: Stream[(A, B)])(f: B => C): Stream[(A, C)] =
    x.map { case (k, v) => (k, f(v)) }

  override final def flatMapValues[A: ClassTag, B: ClassTag, C: ClassTag](
      x: Stream[(A, B)])(f: B => TraversableOnce[C]): Stream[(A, C)] =
    x.flatMap {
      case (k, v) =>
        val values = f(v)
        values.map(k -> _)
    }

  override final def reduceByKey[A: ClassTag, B: ClassTag](xs: Stream[(A, B)])(
      f: (B, B) => B): Stream[(A, B)] = {
    val grouped = xs.groupBy(_._1) map {
      case (a, ys) => a -> ys.map(x => x._2)
    }
    grouped.toStream.map { case (a, bs) => (a, bs reduce f) }
  }

  override final def foldByKey[A: ClassTag, B: ClassTag](
      xs: Stream[(A, B)])(zeroValue: B, f: (B, B) => B): Stream[(A, B)] = {
    val grouped = xs.groupBy(_._1) map {
      case (a, ys) => a -> ys.map(x => x._2)
    }
    grouped.toStream.map { case (a, bs) => (a, bs.foldLeft(zeroValue)(f)) }
  }

  override final def aggregateByKey[A: ClassTag, B: ClassTag, C: ClassTag](
      xs: Stream[(A, B)])(zeroValue: C)(
      seqOp: (C, B) => C,
      combOp: (C, C) => C): Stream[(A, C)] = {
    combineByKey(xs)(b => seqOp(zeroValue, b))(seqOp, combOp)
  }

  override final def combineByKey[A: ClassTag, B: ClassTag, C: ClassTag](
      xs: Stream[(A, B)])(createCombiner: B => C)(
      mergeValue: (C, B) => C,
      mergeCombiners: (C, C) => C): Stream[(A, C)] = {
    val grouped = xs.groupBy(_._1) map {
      case (a, ys) => a -> ys.map(x => x._2)
    }
    grouped.toStream.map {
      case (a, bs) =>
        val c = bs
          .grouped(2)
          .toStream
          .par
          .map {
            case head #:: tail =>
              tail.foldLeft[C](createCombiner(head))(mergeValue)
          }
          .reduce(mergeCombiners)
        (a, c)
    }
  }

  override final def countByKey[A: ClassTag, B: ClassTag](
      xs: Stream[(A, B)]): Map[A, Long] = {
    val keyed  = mapValues(xs)(_ => 1L)
    val counts = reduceByKey(keyed)(_ + _)
    counts.toMap
  }

  override final def collectAsMap[A: ClassTag, B: ClassTag](
      xs: Stream[(A, B)]): Map[A, B] =
    xs.toMap

  override def partitionBy[A: ClassTag, B: ClassTag](
      xs: Stream[(A, B)])(partitioner: Partitioner): Stream[(A, B)] = xs
}
