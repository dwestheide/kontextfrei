# Setup

_kontextfrei_ is currently divided into two separate modules:

- `kontextfrei-core`: The main artifact to depend on when using _kontextfrei_
- `kontextfrei-scalatest`: An optional artifact that provides some helpers for using _kontextfrei_ with [ScalaTest](http://www.scalatest.org/).

`kontextfrei-core` assumes that the Spark dependency is provided by your application, so you have to explicitly add a dependency to Spark in your build definition. This is because you usually want to depend on Spark in `provided` scope anyway in your applications.

@@@ note

If you decide to use `kontextfrei-scalatest`, this will come with a transitive dependency on the ScalaTest library. This dependency is compiled against ScalaTest 3.0.1.

@@@

Currently, _kontextfrei_ binary releases are only built against Spark 2.0.0 with Scala 2.11 and Spark 1.4.1 with Scala 2.10.

Here is what you need to add to your `build.sbt` file in order to use `kontextfrei-core` and `kontextfrei-scalatest` in your SBT project:

## Scala 2.10 / Spark 1.4.1

```scala
resolvers += "dwestheide" at "https://dl.bintray.com/dwestheide/maven"
libraryDependencies += "com.danielwestheide" %% "kontextfrei-core" % "0.4.0"
libraryDependencies += "com.danielwestheide" %% "kontextfrei-scalatest" % "0.4.0"
libraryDependencies += "org.apache.spark" %% "spark-core" % "1.4.1" % "provided"
```

## Scala 2.11 / Spark 2.0

```scala
resolvers += "dwestheide" at "https://dl.bintray.com/dwestheide/maven"
libraryDependencies += "com.danielwestheide" %% "kontextfrei-core" % "0.4.0"
libraryDependencies += "com.danielwestheide" %% "kontextfrei-scalatest" % "0.4.0"
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.0.0" % "provided"
```
