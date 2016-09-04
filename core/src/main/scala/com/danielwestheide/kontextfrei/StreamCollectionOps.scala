package com.danielwestheide.kontextfrei

import scala.collection.Map
import scala.collection.immutable.Seq
import scala.reflect.ClassTag

trait StreamCollectionOps {
  implicit val streamCollectionOps: DCollectionOps[Stream] = new DCollectionOps[Stream] {
    def unit[A: ClassTag](as: Seq[A]): Stream[A] = as.toStream
    def cartesian[A: ClassTag, B: ClassTag](as: Stream[A])(bs: Stream[B]): Stream[(A, B)] = for {
      a <- as
      b <- bs
    } yield (a, b)
    def collect[A: ClassTag, B: ClassTag](as: Stream[A])(pf: PartialFunction[A, B]): Stream[B] = as collect pf
    def distinct[A : ClassTag](as: Stream[A]): Stream[A] = as.distinct
    def map[A: ClassTag, B: ClassTag](as: Stream[A])(f: A => B): Stream[B] = as map f
    def flatMap[A: ClassTag, B: ClassTag](as: Stream[A])(f: (A) => TraversableOnce[B]): Stream[B] = as flatMap f
    def filter[A: ClassTag](as: Stream[A])(f: A => Boolean): Stream[A] = as filter f
    def groupBy[A, B: ClassTag](as: Stream[A])(f: (A) => B): Stream[(B, Iterable[A])] = (as groupBy f).toStream

    def sortBy[A: ClassTag, B: ClassTag : Ordering](as: Stream[A])(f: (A) => B)(ascending: Boolean): Stream[A] = {
      val ordering = implicitly[Ordering[B]]
      as.sortBy(f)(if (ascending) ordering else ordering.reverse)
    }

    def cogroup[A : ClassTag, B : ClassTag, C : ClassTag]
    (x: Stream[(A, B)])(y: Stream[(A, C)]): Stream[(A, (Iterable[B], Iterable[C]))] = {
      val xs = x.groupBy(_._1).mapValues(_.map(_._2))
      val ys = y.groupBy(_._1).mapValues(_.map(_._2))
      val allKeys = (xs.keys ++ ys.keys).toStream
      allKeys.map { key =>
        val xsWithKey = xs.getOrElse(key, Stream.empty)
        val ysWithKey = ys.getOrElse(key, Stream.empty)
        key -> (xsWithKey, ysWithKey)
      }
    }

    def values[A : ClassTag, B : ClassTag](x: Stream[(A, B)]): Stream[B] = x.map(_._2)
    def keys[A : ClassTag, B : ClassTag](x: Stream[(A, B)]): Stream[A] = x.map(_._1)

    def leftOuterJoin[A: ClassTag, B: ClassTag, C: ClassTag](x: Stream[(A, B)])(y: Stream[(A, C)]): Stream[(A, (B, Option[C]))] = {
      flatMapValues(cogroup(x)(y)) { case (bs, cs) =>
        if (cs.isEmpty) bs.iterator.map(b => (b, None))
        else for {
          b <- bs.iterator
          c <- cs.iterator
        } yield (b, Some(c))
      }
    }
    def rightOuterJoin[A: ClassTag, B: ClassTag, C: ClassTag](x: Stream[(A, B)])(y: Stream[(A, C)]): Stream[(A, (Option[B], C))] = {
      flatMapValues(cogroup(x)(y)) { case (bs, cs) =>
        if (bs.isEmpty) cs.iterator.map(c => (None, c))
        else for {
          b <- bs.iterator
          c <- cs.iterator
        } yield (Some(b), c)
      }
    }
    def mapValues[A: ClassTag, B: ClassTag, C: ClassTag](x: Stream[(A, B)])(f: B => C): Stream[(A, C)] =
      x map { case (k, v) => (k, f(v)) }
    def flatMapValues[A: ClassTag, B: ClassTag, C: ClassTag](x: Stream[(A, B)])(f: B => TraversableOnce[C]): Stream[(A, C)] =
      x flatMap { case (k, v) =>
        val values = f(v)
        values.map(k -> _)
      }
    def reduceByKey[A: ClassTag, B: ClassTag](xs: Stream[(A, B)])(f: (B, B) => B): Stream[(A, B)] = {
      val grouped = xs.groupBy(_._1) map { case (a, ys) => a -> ys.map(x => x._2) }
      grouped.toStream map { case (a, bs) => (a, bs reduce f) }
    }
    def foldByKey[A: ClassTag, B: ClassTag](xs: Stream[(A, B)])(zeroValue: B)(f: (B, B) => B): Stream[(A, B)] = {
      val grouped = xs.groupBy(_._1) map { case (a, ys) => a -> ys.map(x => x._2) }
      grouped.toStream map { case (a, bs) => (a, bs.foldLeft(zeroValue)(f)) }
    }

    def aggregateByKey[A: ClassTag, B: ClassTag, C: ClassTag]
    (xs: Stream[(A, B)])
    (zeroValue: C)
    (seqOp: (C, B) => C)
    (combOp: (C, C) => C): Stream[(A, C)] = {
      val grouped = xs.groupBy(_._1) map { case (a, ys) => a -> ys.map(x => x._2) }
      grouped.toStream map { case (a, bs) => (a, bs.aggregate(zeroValue)(seqOp, combOp)) }
    }

    def toArray[A : ClassTag](as: Stream[A]): Array[A] = as.toArray
    def count[A](as: Stream[A]): Long = as.size
    def countByValue[A: ClassTag](as: Stream[A])(implicit ord: Ordering[A]): collection.Map[A, Long] =
      as.groupBy(identity) map { case (k, v) => (k, v.size.toLong) }
    def first[A : ClassTag](as: Stream[A]): A = as.headOption getOrElse {
      throw new UnsupportedOperationException("empty collection")
    }

    def countByKey[A: ClassTag, B: ClassTag](xs: Stream[(A, B)]): Map[A, Long] = {
      val keyed = mapValues(xs)(_ => 1L)
      val counts = reduceByKey(keyed)(_ + _)
      counts.toMap
    }
  }
}
object StreamCollectionOps extends StreamCollectionOps
