package com.danielwestheide.kontextfrei.example

import com.danielwestheide.kontextfrei.DCollectionOps

trait LanguagesByPopularityProperties[DColl[_]] extends BaseSpec[DColl] {

  import DCollectionOps.Imports._
  def logic: JobLogic[DColl]

  property("Each language from created events has a star count") {
    forAll(createdAndStarredEvents) { case (createdEvts, starredEvts) =>
      whenever(starredEvts forall existingRepo(createdEvts)) {
        val result = logic
          .languagesByPopularity(unit(starredEvts), unit(createdEvts)).collect().toList
        result.map(_._1).toSet mustEqual createdEvts.map(_.mainLanguage).toSet
      }
    }
  }

  property("The total counts is the number of repo starred events") {
    forAll(createdAndStarredEvents) { case (createdEvts, starredEvts) =>
      whenever(starredEvts forall existingRepo(createdEvts)) {
        val result = logic
          .languagesByPopularity(unit(starredEvts), unit(createdEvts)).collect().toList
        result.map(_._2).sum mustEqual starredEvts.size
      }
    }
  }

  property("Language with maximum stars is the first element in result") {
    forAll(createdAndStarredEvents) { case (createdEvts, starredEvts) =>
      whenever(starredEvts forall existingRepo(createdEvts)) {
        val result = logic
          .languagesByPopularity(unit(starredEvts), unit(createdEvts)).collect().toList
        result.head mustEqual result.maxBy(_._2)
      }
    }
  }

  private def existingRepo(createdEvts: List[RepoCreated])(evt: RepoStarred): Boolean = {
    createdEvts.exists(e => e.owner == evt.owner && e.name == evt.repo)
  }

}
