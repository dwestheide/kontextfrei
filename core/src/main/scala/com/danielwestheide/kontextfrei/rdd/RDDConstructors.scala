package com.danielwestheide.kontextfrei.rdd

import com.danielwestheide.kontextfrei.DCollectionConstructors
import org.apache.spark.rdd.RDD

import scala.collection.immutable.Seq
import scala.reflect.ClassTag

private[kontextfrei] trait RDDConstructors
    extends DCollectionConstructors[RDD]
    with RDDBase {
  override final def unit[A: ClassTag](as: Seq[A]): RDD[A] =
    sparkContext.parallelize(as)

  override final def empty[A: ClassTag]: RDD[A] = sparkContext.emptyRDD
}
