package com.danielwestheide.kontextfrei.stream

import com.danielwestheide.kontextfrei.DCollectionOps

class StreamOps
    extends DCollectionOps[Stream]
    with StreamBaseFunctions
    with StreamPairFunctions
    with StreamConstructors
    with StreamOrderedFunctions
