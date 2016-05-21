package com.danielwestheide.kontextfrei.example

import com.danielwestheide.kontextfrei.DCollectionOps
import org.scalatest.{MustMatchers, PropSpecLike}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

trait BaseSpec[DColl[_]] extends PropSpecLike
  with GeneratorDrivenPropertyChecks
  with Generators
  with MustMatchers {

  implicit def ops: DCollectionOps[DColl]
}
