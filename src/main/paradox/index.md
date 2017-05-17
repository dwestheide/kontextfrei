# kontextfrei

_kontextfrei_ is a Scala library that aims to provide developers with a faster feedback loop when developing [Apache Spark](https://spark.apache.org/) applications. To achieve that, it enables you to write the business logic of your Spark application, as well as your test code, against an abstraction over Spark's `RDD`.

Firing up a `SparkContext` and running your unit tests in a local Spark cluster is really slow. _kontextfrei_ frees you from this hard dependency on a `SparkContext`. By programming your business logic against an API that abstracts over Spark's `RDD`, you can execute your unit tests against Scala collections instead of `RDD`s, leading to a much faster feedback cycle during development.

By providing an API that is virtually identical to Spark's `RDD` API, developers already familiar with Spark do not have to learn a new API to benefit from _kontextfrei_.

This website serves as the hub for all things _kontextfrei_ -- how to use it, who is using it, and how to contribute to the development of this project.

@@@ index

 - [setup](setup.md)
 - [status](status.md)
 - [example](example.md)
 - [resources](resources.md)
 - [contribute](contribute.md)

@@@
