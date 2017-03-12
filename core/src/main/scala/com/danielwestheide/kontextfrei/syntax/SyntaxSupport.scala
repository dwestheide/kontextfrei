package com.danielwestheide.kontextfrei.syntax

import com.danielwestheide.kontextfrei.DCollectionOps

import scala.reflect.ClassTag

trait SyntaxSupport {
  implicit def toDCollectionOps[DCollection[_], A: ClassTag](
      target: DCollection[A])(implicit dc: DCollectionOps[DCollection]) =
    new BaseSyntax[DCollection, A](dc, target)
  implicit def toDCollectionPairOps[DCollection[_], A: ClassTag, B: ClassTag](
      target: DCollection[(A, B)])(implicit dc: DCollectionOps[DCollection]) =
    new PairSyntax[DCollection, A, B](dc, target)
}
