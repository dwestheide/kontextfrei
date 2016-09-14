name := "kontextfrei"

val common = Seq(
  organization := "com.danielwestheide",
  version := "0.2.1-SNAPSHOT",
  scalaVersion := "2.10.6",
  crossScalaVersions := Seq("2.10.6", "2.11.8"),
  licenses += ("Apache-2.0", url("https://opensource.org/licenses/Apache-2.0")),
  bintrayPackageLabels := Seq("scala", "spark", "testing"),
  scalacOptions ++= Seq("-feature", "-language:higherKinds", "-language:implicitConversions")
)


def spark(scalaVersion: String) = {
  val sparkVersion = scalaVersion match {
    case "2.10" => "1.4.1"
    case "2.11" => "2.0.0"
    case other => fail (s"Unsupported Scala version: $other")
  }
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided"
}
val scalatest = "org.scalatest" %% "scalatest" % "2.2.6" % "test"
val scalacheck = "org.scalacheck" %% "scalacheck" % "1.12.5" % "test"

lazy val core = Project(id = "kontextfrei-core", base = file("core"))
  .settings(common)
  .settings(libraryDependencies ++= Seq(spark(scalaBinaryVersion.value), scalatest, scalacheck))

lazy val root = Project(id = "kontextfrei", base = file("."))
    .settings(common)
    .aggregate(core)

publishArtifact in root := false

publish in root := {}

publishLocal in root := {}
