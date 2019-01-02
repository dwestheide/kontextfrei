package com.danielwestheide.kontextfrei

trait DCollectionOps[DCollection[_]]
    extends DCollectionBaseFunctions[DCollection]
    with DCollectionPairFunctions[DCollection]
    with DCollectionConstructors[DCollection]
    with DCollectionOrderedFunctions[DCollection]
