import com.lightbend.paradox.sbt.ParadoxPlugin.autoImport.paradoxTheme
import KontextfreiKeys._

name := "kontextfrei"

sparkVersion in ThisBuild := sys.props.getOrElse("kontextfrei.spark.version",
                                                 "1.4.1")

// Only Spark 2.4.0 or later is available for Scala 2.12
// And Spark 2.4.0 is not available for Scala 2.10
// This check will have to be made more robust when 2.5.0 comes out
def scalaVersions(sparkVersion: String): Seq[String] = {
  if (sparkVersion.startsWith("2.4")) Seq("2.11.12", "2.12.8")
  else if (sparkVersion.startsWith("2.3")) Seq("2.11.12")
  else Seq("2.11.12", "2.10.7")
}

val common = Seq(
  organization := "com.danielwestheide",
  normalizedName := normalizedName.value + "-spark-" + sparkVersion.value,
  version := "0.8.0",
  scalaVersion := "2.11.12",
  crossScalaVersions := scalaVersions(sparkVersion.value),
  licenses += ("Apache-2.0",
  url("https://opensource.org/licenses/Apache-2.0")),
  bintrayPackageLabels := Seq("scala", "spark", "testing"),
  parallelExecution in Test := false,
  scalacOptions ++= Seq("-feature",
                        "-language:higherKinds",
                        "-language:implicitConversions")
)

val scalatest  = "org.scalatest"  %% "scalatest"  % "3.0.5"
val scalacheck = "org.scalacheck" %% "scalacheck" % "1.14.0"

def spark(version: String, scalaVersion: String) = {
  // A very dirty way of aborting the build successfully in case of incompatible
  // versions of Scala and Spark; for use in Travis CI, where I cannot figure
  // out how to make a dependent build only for those combinations of Scala
  // and Spark version that actually exist
  if (scalaVersion.startsWith("2.12") && !version.startsWith("2.4")) sys.exit(0)
  else if ((version.startsWith("2.4") || version.startsWith("2.3")) && scalaVersion.startsWith("2.10")) sys.exit(0)
  else "org.apache.spark" %% "spark-core" % version % "provided"
}

lazy val core = Project(id = "kontextfrei-core", base = file("core"))
  .settings(common)
  .settings(
    libraryDependencies ++= Seq(spark(sparkVersion.value, scalaVersion.value),
                                scalatest  % Test,
                                scalacheck % Test))

lazy val scalaTest =
  Project(id = "kontextfrei-scalatest", base = file("scalatest"))
    .settings(common)
    .settings(
      libraryDependencies ++= Seq(spark(sparkVersion.value, scalaVersion.value),
                                  scalatest,
                                  scalacheck))
    .dependsOn(core)

lazy val root = Project(id = "kontextfrei", base = file("."))
  .settings(common)
  .settings(
    sourceDirectory in Paradox := sourceDirectory.value / "main" / "paradox",
    paradoxTheme := Some(builtinParadoxTheme("generic"))
  )
  .settings(
    git.remoteRepo := "git@github.com:dwestheide/kontextfrei.git"
  )
  .aggregate(core, scalaTest)
  .enablePlugins(GhpagesPlugin)
  .enablePlugins(ParadoxSitePlugin)

publishArtifact in root := false

publish in root := {}

publishLocal in root := {}
