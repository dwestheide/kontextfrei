package com.danielwestheide.kontextfrei.example

import com.danielwestheide.kontextfrei.{DCollectionOps, StreamCollectionOps}

abstract class UnitSpec extends BaseSpec[Stream] {
  override implicit val ops: DCollectionOps[Stream] = StreamCollectionOps.streamCollectionOps
}
