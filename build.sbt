name := "kontextfrei"

val common = Seq(
  organization := "com.danielwestheide",
  version := "0.0.1-SNAPSHOT",
  scalacOptions ++= Seq("-feature", "-language:higherKinds", "-language:implicitConversions")
)

val spark = "org.apache.spark" %% "spark-core" % "1.4.1"
val scalatest = "org.scalatest" %% "scalatest" % "2.2.6" % "test"
val scalacheck = "org.scalacheck" %% "scalacheck" % "1.12.5" % "test"
val jodaTime = "joda-time" % "joda-time" % "2.9.3"
val jodaConvert = "org.joda" % "joda-convert" % "1.8.1"

scalaVersion := "2.10.6"

lazy val core = Project(id = "kontextfrei-core", base = file("core"))
  .settings(common)
  .settings(libraryDependencies ++= Seq(spark, scalatest, scalacheck))

lazy val root = Project(id = "kontextfrei", base = file("."))
    .aggregate(core)
