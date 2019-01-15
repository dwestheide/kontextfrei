package com.danielwestheide.test

import org.scalatest.{FlatSpec, Matchers}

class RDDBaseTest extends FlatSpec with Matchers {

  behavior of "RDDBase"

  it should "return calling site from outside kontextfrei package" in {
    import com.danielwestheide.kontextfrei.rdd.CallSiteInfoTestHelper._

    val (method, site) = rddmethod
    method should be ("rddmethod")
    site should be ("RDDBaseTest.scala:12")
  }

}
