package com.danielwestheide.kontextfrei.scalatest

import com.danielwestheide.kontextfrei.DCollectionOps

trait KontextfreiSpec[DColl[_]] {
  implicit def ops: DCollectionOps[DColl]
}
