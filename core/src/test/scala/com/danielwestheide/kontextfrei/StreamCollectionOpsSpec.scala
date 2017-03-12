package com.danielwestheide.kontextfrei

import com.danielwestheide.kontextfrei.stream.StreamOpsSupport

class StreamCollectionOpsSpec extends DCollectionOpsProperties[Stream] {
  override implicit val ops: DCollectionOps[Stream] =
    StreamOpsSupport.streamCollectionOps
}
