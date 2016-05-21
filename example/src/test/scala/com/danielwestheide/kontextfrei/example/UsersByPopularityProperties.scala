package com.danielwestheide.kontextfrei.example

import com.danielwestheide.kontextfrei.DCollectionOps

trait UsersByPopularityProperties[DColl[_]] extends BaseSpec[DColl] {

  import DCollectionOps.Imports._
  def logic: JobLogic[DColl]

  property("Each user appears only once") {
    forAll { starredEvents: List[RepoStarred] =>
      val result = logic.usersByPopularity(unit(starredEvents)).collect().toList
      result.distinct mustEqual result
    }
  }

  property("Total counts correspond to number of events") {
    forAll { starredEvents: List[RepoStarred] =>
      val result = logic.usersByPopularity(unit(starredEvents)).collect().toList
      result.map(_._2).sum mustEqual starredEvents.size
    }
  }

  property("User with maximum stars is the first element in result") {
    forAll { starredEvents: List[RepoStarred] =>
      val result = logic.usersByPopularity(unit(starredEvents)).collect().toList
      result.head mustEqual result.maxBy(_._2)
    }
  }

}
