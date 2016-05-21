package com.danielwestheide.kontextfrei.example

import com.danielwestheide.kontextfrei.DCollectionOps
import org.joda.time.DateTime

class JobLogic[DCollection[_] : DCollectionOps] {

  import DCollectionOps.syntax._

  def usersByPopularity(repoStarredEvts: DCollection[RepoStarred]): DCollection[(String, Long)] = {
    repoStarredEvts
      .map(evt => evt.owner -> 1L)
      .reduceByKey(_ + _)
      .sortBy(_._2, ascending = false)
  }

  def languagesByPopularity(starred: DCollection[RepoStarred],
                            created: DCollection[RepoCreated]): DCollection[(String, Long)] = {
    val starsByIdentity = starred
      .map(evt => (evt.owner, evt.repo) -> 1L)
    val languagesByRepoIdentity = created.map(evt => (evt.owner, evt.name) -> evt.mainLanguage)
    languagesByRepoIdentity
      .leftOuterJoin(starsByIdentity)
      .map { case (_, (language, count)) => (language, count.getOrElse(0L)) }
      .reduceByKey(_ + _)
      .sortBy(_._2, ascending = false)
  }

  def parseRepoStarredEvents(csv: DCollection[String]): DCollection[RepoStarred] = {
    csv.map { evt =>
      evt.split(",").toList match {
        case dtStr :: owner :: repo :: starrer :: Nil =>
          RepoStarred(DateTime.parse(dtStr), owner, repo, starrer)
        case _ => sys.error("invalid data, sorry!")
      }
    }
  }

  def parseRepoCreatedEvents(csv: DCollection[String]): DCollection[RepoCreated] = {
    csv.map { evt =>
      evt.split(",").toList match {
        case dtStr :: owner :: name :: mainLanguage :: Nil =>
          RepoCreated(DateTime.parse(dtStr), owner, name, mainLanguage)
        case _ => sys.error("invalid data, sorry!")
      }
    }
  }

  def toCsv(counts: DCollection[(String, Long)]): DCollection[String] =
    counts map { case (identifier, count) => s"$identifier,$count" }


}
