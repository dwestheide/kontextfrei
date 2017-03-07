package com.danielwestheide.kontextfrei

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.{MustMatchers, PropSpecLike}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

trait BaseSpec[DColl[_]]
    extends PropSpecLike
    with GeneratorDrivenPropertyChecks
    with Generators
    with TypeCheckedTripleEquals
    with MustMatchers {

  implicit def ops: DCollectionOps[DColl]

}
