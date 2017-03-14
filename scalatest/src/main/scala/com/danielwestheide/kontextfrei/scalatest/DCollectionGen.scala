package com.danielwestheide.kontextfrei.scalatest

import com.danielwestheide.kontextfrei.DCollectionOps
import org.scalacheck.{Arbitrary, Gen}

import scala.reflect.ClassTag

trait DCollectionGen {

  def dcollectionOf[A: ClassTag, DCollection[_]](gen: => Gen[A])(
      implicit ops: DCollectionOps[DCollection]): Gen[DCollection[A]] = {
    Gen.nonEmptyListOf(gen).map(ops.unit(_))
  }

  implicit def arbDCollection[A: ClassTag, DCollection[_]](
      implicit ops: DCollectionOps[DCollection],
      arbA: Arbitrary[A]): Arbitrary[DCollection[A]] =
    Arbitrary(dcollectionOf(arbA.arbitrary))
}

object DCollectionGen extends DCollectionGen
