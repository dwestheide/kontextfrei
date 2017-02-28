# kontextfrei
[![Build Status](https://travis-ci.org/dwestheide/kontextfrei.svg?branch=master)](https://travis-ci.org/dwestheide/kontextfrei)
[![codecov](https://codecov.io/gh/dwestheide/kontextfrei/branch/master/graph/badge.svg)](https://codecov.io/gh/dwestheide/kontextfrei)

## What is this?

This library enables you to write the business logic of your Spark application without depending on
`RDD`s and the `SparkContext`.

## Motivation

Why you would want to do that? Because firing up a `SparkContext`
and running your unit tests in a local Spark cluster is really slow. _kontextfrei_ frees you from
this hard dependency on a `SparkContext`, ultimately leading to a much faster feedback cycle during
development.

## How does this work?

By using a typeclass, `DCollectionOps` and higher kinded types, we are able to provide an interface
that looks like that of an `RDD`. There are two instances of this typeclass: one for `RDD`, another
one for Scala's `Stream` from the standard library. The application logic is written against a
generic type for which an instance of `DCollectionOps` is available. This allows you to plug this
logic into Spark's `RDD` in your application bootstrapping code, and choose between the `Stream`
instance and the `RDD` instance of the `DCollectionOps` typeclass for unit and integration tests,
respectively. The actual test code can be shared between the different tests, as it is independent
of a specific instance of `DCollectionOps`.

## Usage

Add a dependency on the the current version of `kontextfrei-core` to your `build.sbt`.

_kontextfrei_ assumes that the Spark dependency is provided by your application, so have to explicitly add a dependency to Spark.

Currently, _kontextfrei_ binary releases are only built against Spark 2.0.0 with Scala 2.11 and Spark 1.4.1 with Scala 2.10.

### Scala 2.10 / Spark 1.4.1

```scala
resolvers += "dwestheide" at "https://dl.bintray.com/dwestheide/maven"
libraryDependencies += "com.danielwestheide" %% "kontextfrei-core" % "0.4.0"
libraryDependencies += "com.danielwestheide" %% "kontextfrei-scalatest" % "0.4.0"
libraryDependencies += "org.apache.spark" %% "spark-core" % "1.4.1" % "provided"
```

### Scala 2.11 / Spark 2.0

```scala
resolvers += "dwestheide" at "https://dl.bintray.com/dwestheide/maven"
libraryDependencies += "com.danielwestheide" %% "kontextfrei-core" % "0.4.0"
libraryDependencies += "com.danielwestheide" %% "kontextfrei-scalatest" % "0.4.0"
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.0.0" % "provided"
```

## Example

For an example that showcases how the library can be used, please have a look at [kontextfrei-example](https://github.com/dwestheide/kontextfrei-example).

## Status

This library is in an early stage, and comes with the following limitations:

* only compiled and tested against Spark 1.4.1 and 2.0.0, other versions may not work
* not feature-complete: only a subset of the operations available on `RDD`s is supported so far
* not used in production yet - while there is extensive test coverage, nothing can be said yet about the feasibility of using this library in production

## Contributions welcome

I would be happy about feedback from people who tried this library out, and especially about any contributions to get over the current limitations described above.
