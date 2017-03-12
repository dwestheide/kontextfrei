package com.danielwestheide.kontextfrei.stream

import com.danielwestheide.kontextfrei.DCollectionOps

trait StreamOpsSupport {
  implicit val streamCollectionOps: DCollectionOps[Stream] =
    new StreamOps
}

object StreamOpsSupport extends StreamOpsSupport
