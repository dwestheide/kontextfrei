# Contributing to kontextfrei

Hey there, thanks a lot for considering to contribute to this project. Any kind of contribution is welcome, from reporting bugs or requesting features to improving the documentation, answering questions, or writing code. Here is a short guide how to do all of these things.

## Reporting bugs

If you think you have found a bug, please create a new issue in the [kontextfrei repository](https://github.com/dwestheide/kontextfrei/issues) and add the _bug_ label. Before you do so, please check if the bug has already been reported by someone else.

A good bug report explains clearly what is wrong, what the expected behaviour is, and how to reproduce it. It includes as much information about the environment as possible, for example the specific version of this library, Scala, and Apache Spark, and Java.

## Suggesting enhancements

If you think this library is missing a feature or can be enhanced in any way, please share your idea by creating a new issue in the [kontextfrei repository](https://github.com/dwestheide/kontextfrei/issues) and add the _enhancement_ label. Before you do so, please check that an issue for this enhancement does not already exist.

A good feature request explains precisely what the desired behaviour is and why you think it would be useful to many people. Please note that for a simple request like adding a yet unsupported `RDD` operator, there is really no need to do the latter.

## Improving documentation

The documentation is [hosted on GitHub Pages](https://dwestheide.github.io/kontextfrei/) and generated from Markdown files [located in the kontextfrei repository](https://github.com/dwestheide/kontextfrei/tree/master/src/main/paradox) using the [Paradox documentation tool](http://developer.lightbend.com/docs/paradox/latest/).

To improve the documentation, please create a [pull request in the kontextfrei repository](https://github.com/dwestheide/kontextfrei/pulls) with a concise title and description explaining what your change is about. Please rebase your pull request against the master branch and squash the changes in your pull request into a single commit.

## Code contributions

If you want to make a code contribution, it would be great to make sure that a GitHub issue for it exists beforehand. Like contributions to documentation, code can be contributed by creating a [pull request in the kontextfrei repository](https://github.com/dwestheide/kontextfrei/pulls). Please use the description of the pull request to explain the what, why, and if necessary, how of your change. Also, please rebase your pull request against the master branch and squash the changes in your pull request into a single commit.

### Developer Notes
* When adding support for new RDD methods, observe the use of withSite loan pattern in the RDDBaseFunction methods to 
ensure correct reporting of the call site in the Spark UI.
