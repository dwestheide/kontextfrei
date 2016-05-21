package com.danielwestheide.kontextfrei.example

import org.joda.time.{DateTime, DateTimeZone}
import org.scalacheck.{Arbitrary, Gen}

trait Generators {

  val dateTimes: Gen[DateTime] = for {
    hourOfDay <- Gen.choose(0, 23)
    minuteOfHour <- Gen.choose(0, 59)
    secondOfMinute <- Gen.choose(0, 59)
  } yield new DateTime(2016, 5, 20, hourOfDay, minuteOfHour, secondOfMinute, DateTimeZone.UTC)

  val user: Gen[String] = Gen.oneOf(Seq(
    "dwestheide", "alberta", "bertab", "chrisc", "dopfi", "ebhrd", "francesca", "georgiab",
    "harryb", "ilonac", "joewill", "karina", "liam", "manfred", "ninag", "octocat", "philp",
    "rike", "stephaniew", "tinad", "ulrich", "wilw", "zoek"
  ))

  val repo: Gen[String] = Gen.oneOf(Seq(
    "hackathon-2016", "my-playground", "slick", "akka", "eventuate", "emacs-config",
    "asciidoctor", "asciidoctor-pdf", "color-themes", "confluent", "rho", "sbt", "sbt-git",
    "siren-scala", "spray", "webster", "jetty", "purescript-book", "hugo", "doob", "consul", "spray-siren",
    "slick", "play", "akka-http", "kafka", "halselhof", "cookbook-123", "foo", "bar", "foobar"
  ))

  val language: Gen[String] = Gen.oneOf(Seq(
    "Scala", "Java", "JavaScript", "Go", "Haskell", "Python", "Groovy", "Ruby"
  ))

  val repoStarred: Gen[RepoStarred] = for {
    at <- dateTimes
    owner <- user
    repo <- repo
    starrerId <- user
  } yield RepoStarred(at, owner, repo, starrerId)

  val repoCreated: Gen[RepoCreated] = for {
    at <- dateTimes
    owner <- user
    name <- repo
    mainLanguage <- language
  } yield RepoCreated(at, owner, name, mainLanguage)

  def specificRepoStarred(owner: String, repo: String): Gen[RepoStarred] = for {
    at <- dateTimes
    starrerId <- user
  } yield RepoStarred(at, owner, repo, starrerId)

  val createdAndStarred: Gen[(RepoCreated, List[RepoStarred])] = for {
    created <- repoCreated
    starred <- Gen.nonEmptyListOf(specificRepoStarred(created.owner, created.name))
  } yield (created, starred)

  val createdAndStarredEvents: Gen[(List[RepoCreated], List[RepoStarred])] = for {
    events <- Gen.nonEmptyListOf(createdAndStarred)
    (created, starred) = events.unzip
    uniqueCreated = created.map(e => (e.owner, e.name) -> e).toMap.values.toList
  } yield (uniqueCreated, starred.flatten)

  implicit val arbRepoStarred = Arbitrary(repoStarred)
  implicit val arbRepoCreated = Arbitrary(repoCreated)
  implicit def arbNonEmptyList[A](implicit ev: Arbitrary[A]) = {
    Arbitrary(Gen.nonEmptyListOf(ev.arbitrary))
  }

}
