package com.danielwestheide.kontextfrei

class StreamCollectionOpsSpec extends DCollectionOpsProperties[Stream] {
  override implicit val ops: DCollectionOps[Stream] =
    StreamCollectionOps.streamCollectionOps
}
