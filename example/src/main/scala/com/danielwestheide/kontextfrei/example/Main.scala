package com.danielwestheide.kontextfrei.example

import com.danielwestheide.kontextfrei.RDDCollectionOps
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object Main extends App with RDDCollectionOps {
  implicit val sparkContext = new SparkContext("local[1]", "test-app")
  try {
    val logic = new JobLogic[RDD]
    val repoStarredEvts = logic
      .parseRepoStarredEvents(sparkContext.textFile("test-data/repo_starred.csv"))
    val repoCreatedEvts = logic
      .parseRepoCreatedEvents(sparkContext.textFile("test-data/repo_created.csv"))
    val usersByPopularity = logic.usersByPopularity(repoStarredEvts)
    val languagesByPopularity =
      logic.languagesByPopularity(repoStarredEvts, repoCreatedEvts)
    logic.toCsv(usersByPopularity).saveAsTextFile("target/users_by_popularity.csv")
    logic.toCsv(languagesByPopularity).saveAsTextFile("target/languages_by_popularity.csv")
  }
  finally {
    sparkContext.stop()
  }
}
