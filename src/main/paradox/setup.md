# Setup

_kontextfrei_ is currently divided into two separate modules:

- `kontextfrei-core`: The main artifact to depend on when using _kontextfrei_
- `kontextfrei-scalatest`: An optional artifact that provides some helpers for using _kontextfrei_ with [ScalaTest](http://www.scalatest.org/).

`kontextfrei-core` assumes that the Spark dependency is provided by your application, so you have to explicitly add a dependency to Spark in your build definition. This is because you usually want to depend on Spark in `provided` scope anyway in your applications.

@@@ note

If you decide to use `kontextfrei-scalatest`, this will come with a transitive dependency on the ScalaTest and ScalaCheck libraries. `kontextfrei` is compiled against ScalaTest 3.0.4 and ScalaCheck 1.13.5.

@@@

Currently, _kontextfrei_ binary releases are built against Spark 1.4.1, 2.0.0, 2.1.0, and 2.2.0, each of them both for  Scala 2.11 and 2.10.

Here is what you need to add to your `build.sbt` file in order to use `kontextfrei-core` and `kontextfrei-scalatest` in your SBT project:

## Spark 1.4.1

```scala
resolvers += "dwestheide" at "https://dl.bintray.com/dwestheide/maven"
libraryDependencies += "com.danielwestheide" %% "kontextfrei-core-spark-1.4.1" % "0.7.1"
libraryDependencies += "com.danielwestheide" %% "kontextfrei-scalatest-spark-1.4.1" % "0.7.1" % "test,it"
libraryDependencies += "org.apache.spark" %% "spark-core" % "1.4.1" % "provided"
```

## Spark 2.0

```scala
resolvers += "dwestheide" at "https://dl.bintray.com/dwestheide/maven"
libraryDependencies += "com.danielwestheide" %% "kontextfrei-core-spark-2.0.0" % "0.7.1"
libraryDependencies += "com.danielwestheide" %% "kontextfrei-scalatest-spark-2.0.0" % "0.7.1" % "test,it"
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.0.0" % "provided"
```

## Spark 2.1.0

```scala
resolvers += "dwestheide" at "https://dl.bintray.com/dwestheide/maven"
libraryDependencies += "com.danielwestheide" %% "kontextfrei-core-spark-2.1.0" % "0.7.1"
libraryDependencies += "com.danielwestheide" %% "kontextfrei-scalatest-spark-2.1.0" % "0.7.1" % "test,it"
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.1.0" % "provided"
```

## Spark 2.2.0

```scala
resolvers += "dwestheide" at "https://dl.bintray.com/dwestheide/maven"
libraryDependencies += "com.danielwestheide" %% "kontextfrei-core-spark-2.2.0" % "0.7.1"
libraryDependencies += "com.danielwestheide" %% "kontextfrei-scalatest-spark-2.2.0" % "0.7.1" % "test,it"
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.2.0" % "provided"
```
