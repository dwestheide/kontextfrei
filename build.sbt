import com.lightbend.paradox.sbt.ParadoxPlugin.autoImport.paradoxTheme
import KontextfreiKeys._

name := "kontextfrei"

sparkVersion in ThisBuild := sys.props.getOrElse("kontextfrei.spark.version",
                                                 "1.4.1")
val common = Seq(
  organization := "com.danielwestheide",
  normalizedName := normalizedName.value + "-spark-" + sparkVersion.value,
  version := "0.7.2-SNAPSHOT",
  scalaVersion := "2.11.8",
  crossScalaVersions := Seq("2.11.8", "2.10.6"),
  licenses += ("Apache-2.0",
  url("https://opensource.org/licenses/Apache-2.0")),
  bintrayPackageLabels := Seq("scala", "spark", "testing"),
  parallelExecution in Test := false,
  scalacOptions ++= Seq("-feature",
                        "-language:higherKinds",
                        "-language:implicitConversions")
)

val scalatest  = "org.scalatest"  %% "scalatest"  % "3.0.4"
val scalacheck = "org.scalacheck" %% "scalacheck" % "1.13.5"

def spark(version: String) =
  "org.apache.spark" %% "spark-core" % version % "provided"

lazy val core = Project(id = "kontextfrei-core", base = file("core"))
  .settings(common)
  .settings(
    libraryDependencies ++= Seq(spark(sparkVersion.value),
                                scalatest  % Test,
                                scalacheck % Test))

lazy val scalaTest =
  Project(id = "kontextfrei-scalatest", base = file("scalatest"))
    .settings(common)
    .settings(
      libraryDependencies ++= Seq(spark(sparkVersion.value),
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
