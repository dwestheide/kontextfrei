package com.danielwestheide.kontextfrei.scalatest

import com.danielwestheide.kontextfrei.StreamCollectionOps

trait StreamSpec extends KontextfreiSpec[Stream]  {
  override implicit def ops = StreamCollectionOps.streamCollectionOps
}
