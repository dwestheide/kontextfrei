package com.danielwestheide.kontextfrei.scalatest

import com.danielwestheide.kontextfrei.stream.StreamOpsSupport

trait StreamSpec extends KontextfreiSpec[Stream] {
  override implicit def ops = StreamOpsSupport.streamCollectionOps
}
