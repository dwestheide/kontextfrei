package com.danielwestheide.kontextfrei.rdd

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

private[kontextfrei] trait RDDBase {
  def sparkContext: SparkContext

  def withSite[A, R](as: RDD[A])(body: RDD[A] => R): R = {

    val (method, site) = RDDBase.callSiteInfo

    as.sparkContext.setCallSite(s"$method at $site")
    body(as)
  }
}

object RDDBase {

  def callSiteInfo: (String, String) = {

    def isInKf(ste: StackTraceElement): Boolean = {
      Option(ste.getClassName()).exists(_.startsWith("com.danielwestheide.kontextfrei"))
    }

    val (kf, user) = Thread.currentThread().getStackTrace()
      .dropWhile(!isInKf(_))
      .span(isInKf)

    val method = kf.lastOption.map(_.getMethodName()).getOrElse("unknown")
    val site = user.headOption.map(s => s"${s.getFileName()}:${s.getLineNumber()}").getOrElse("unknown")

    (method, site)
  }
}
