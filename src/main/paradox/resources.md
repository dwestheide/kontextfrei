# Resources

This page collects external resources that can help you learn more about _kontextfrei_.

## Blog posts

[Introducing kontextfrei](https://danielwestheide.com/blog/2017/10/31/introducing-kontextfrei.html)

## Conference talks

### kontextfrei: A new approach to testable Spark applications, Scalar Conf Warsaw 2017, April 08, 2017

<iframe width="560" height="315" src="https://www.youtube.com/embed/Z_Ab5Y90fuw" frameborder="0" allowfullscreen></iframe>

Slides are available on [Speaker Deck](https://speakerdeck.com/dwestheide/kontextfrei-a-new-approach-to-testable-spark-applications)

#### Abstract

Apache Spark has become the de-facto standard for writing big data processing pipelines. While the business logic of Spark applications is often at least as complex as what we have been dealing with in a pre-big data world, enabling developers to write comprehensive, fast unit test suites has not been a priority in the design of Spark. The main problem is that you cannot test your code without at least running a local SparkContext. These tests are not really unit tests, and they are too slow for pursuing a test-driven development approach.

In this talk, I will introduce thekontextfrei library, which aims to liberate you from the chains of the SparkContext. I will show how it helps restoring the fast feedback loop we are taking for granted. In addition, I will explain how kontextfrei is implemented and discuss some of the design decisions made and look at alternative approaches and current limitations.
