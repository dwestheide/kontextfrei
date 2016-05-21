# kontextfrei

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

## Status

This library is unreleased, a work in progress and must be considered experimental. Feel free to try
it out, any feedback is definitely welcome, but please don't use this for any serious projects yet.