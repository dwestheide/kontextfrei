package com.danielwestheide.kontextfrei

import org.scalacheck.{Arbitrary, Gen}

object SmallNumbers {
  implicit val arbSmallPosInt: Arbitrary[Int] = Arbitrary(Gen.choose(1, 20))
}
