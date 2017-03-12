package com.danielwestheide.kontextfrei.syntax

import com.danielwestheide.kontextfrei.DCollectionOps

import scala.reflect.ClassTag
import collection.immutable._

trait DistributionSyntaxSupport {
  implicit def seqToDistributable[DCollection[_], A: ClassTag](seq: Seq[A])(
      implicit ops: DCollectionOps[DCollection])
    : DistributableSeq[DCollection, A] =
    new DistributableSeq[DCollection, A](seq)
}

class DistributableSeq[DCollection[_], A: ClassTag](seq: Seq[A])(
    implicit ops: DCollectionOps[DCollection]) {
  def distributed: DCollection[A] = ops.unit(seq)
}
