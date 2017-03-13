import sbt._
object KontextfreiKeys {
  val sparkVersion = settingKey[String]("Version of Spark to build against")
}
