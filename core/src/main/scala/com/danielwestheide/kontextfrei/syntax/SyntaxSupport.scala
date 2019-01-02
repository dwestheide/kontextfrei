package com.danielwestheide.kontextfrei.syntax

import com.danielwestheide.kontextfrei.DCollectionOps

import scala.reflect.ClassTag

trait SyntaxSupport {
  implicit def toDCollectionOps[DCollection[_], A: ClassTag](
      target: DCollection[A])(
      implicit dc: DCollectionOps[DCollection]): BaseSyntax[DCollection, A] =
    new BaseSyntax[DCollection, A](dc, target)
  implicit def toDCollectionPairOps[DCollection[_], A: ClassTag, B: ClassTag](
      target: DCollection[(A, B)])(
      implicit dc: DCollectionOps[DCollection]): PairSyntax[DCollection, A, B] =
    new PairSyntax[DCollection, A, B](dc, target)
  implicit def toDCollectionOrderedOps[DCollection[_],
                                       A: ClassTag: Ordering,
                                       B: ClassTag](
      target: DCollection[(A, B)])(implicit dc: DCollectionOps[DCollection])
    : OrderedSyntax[DCollection, A, B] =
    new OrderedSyntax[DCollection, A, B](dc, target)

}
