package com.danielwestheide.kontextfrei

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import scala.collection.Map
import scala.collection.immutable.Seq
import scala.reflect.ClassTag

trait RDDCollectionOps {

  implicit def rddCollectionOps(implicit sparkContext: SparkContext): DCollectionOps[RDD] = new DCollectionOps[RDD] {
    def unit[A : ClassTag](as: Seq[A]): RDD[A] = sparkContext.parallelize(as)

    def cartesian[A: ClassTag, B: ClassTag](as: RDD[A])(bs: RDD[B]): RDD[(A, B)] = as cartesian bs
    def cogroup[A: ClassTag, B: ClassTag, C: ClassTag](x: RDD[(A, B)])(y: RDD[(A, C)]): RDD[(A, (Iterable[B], Iterable[C]))] = x.cogroup(y)
    def collect[A: ClassTag, B: ClassTag](as: RDD[A])(pf: PartialFunction[A, B]): RDD[B] = as collect pf
    def distinct[A : ClassTag](as: RDD[A]): RDD[A] = as.distinct()
    def map[A: ClassTag, B: ClassTag](as: RDD[A])(f: A => B): RDD[B] = as map f
    def flatMap[A: ClassTag, B: ClassTag](as: RDD[A])(f: A => TraversableOnce[B]): RDD[B] = as flatMap f
    def filter[A: ClassTag](as: RDD[A])(f: A => Boolean): RDD[A] = as filter f
    def groupBy[A, B : ClassTag](as: RDD[A])(f: A => B): RDD[(B, Iterable[A])] = as groupBy f

    def sortBy[A: ClassTag, B: ClassTag : Ordering](as: RDD[A])(f: (A) => B)(ascending: Boolean): RDD[A] =
      as.sortBy(f, ascending)


    def values[A : ClassTag, B : ClassTag](x: RDD[(A, B)]): RDD[B] = x.values
    def keys[A : ClassTag, B : ClassTag](x: RDD[(A, B)]): RDD[A] = x.keys
    def leftOuterJoin[A : ClassTag, B : ClassTag, C : ClassTag](x: RDD[(A, B)])(y: RDD[(A, C)]): RDD[(A, (B, Option[C]))] = x leftOuterJoin y
    def mapValues[A: ClassTag, B: ClassTag, C: ClassTag]
    (x: RDD[(A, B)])(f: B => C): RDD[(A, C)] = x mapValues f
    def reduceByKey[A: ClassTag, B: ClassTag](xs: RDD[(A, B)])(f: (B, B) => B): RDD[(A, B)] = xs reduceByKey f
    def aggregateByKey[A: ClassTag, B: ClassTag, C: ClassTag]
    (xs: RDD[(A, B)])
    (zeroValue: C)
    (seqOp: (C, B) => C)
    (combOp: (C, C) => C): RDD[(A, C)] = xs.aggregateByKey(zeroValue)(seqOp, combOp)

    def toArray[A : ClassTag](as: RDD[A]): Array[A] = as.collect()
    def count[A](as: RDD[A]): Long = as.count()
    def countByValue[A: ClassTag](as: RDD[A])(implicit ord: Ordering[A]): Map[A, Long] = as.countByValue()
    def first[A : ClassTag](as: RDD[A]): A = as.first()
  }

}

object RDDCollectionOps extends RDDCollectionOps
