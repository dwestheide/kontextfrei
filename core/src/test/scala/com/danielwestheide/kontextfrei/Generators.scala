package com.danielwestheide.kontextfrei

import org.scalacheck.{Arbitrary, Gen}

trait Generators {

  import org.scalactic.TypeCheckedTripleEquals._

  def listWithDuplicates[A: Arbitrary]: Gen[List[A]] =
    for {
      xs <- Gen.nonEmptyListOf(Arbitrary.arbitrary[A])
      ys <- Gen.listOfN(xs.size * 2, Gen.oneOf(xs))
    } yield ys

  val intPredicate1: Gen[Int => Boolean] = for {
    i <- Arbitrary.arbitrary[Int]
  } yield (x: Int) => x < i

  val intPredicate2: Gen[Int => Boolean] = for {
    i <- Arbitrary.arbitrary[Int]
  } yield (x: Int) => x <= i

  val intPredicate3: Gen[Int => Boolean] = for {
    i <- Arbitrary.arbitrary[Int]
  } yield (x: Int) => x === i

  val intPredicate4: Gen[Int => Boolean] = for {
    i <- Arbitrary.arbitrary[Int]
  } yield (x: Int) => x > i

  val intPredicate5: Gen[Int => Boolean] = for {
    i <- Arbitrary.arbitrary[Int]
  } yield (x: Int) => x >= i

  val intPredicate: Gen[Int => Boolean] =
    Gen.oneOf(intPredicate1,
              intPredicate2,
              intPredicate3,
              intPredicate4,
              intPredicate5)

  val stringPredicate1: Gen[String => Boolean] = for {
    p <- intPredicate
  } yield (x: String) => p(x.length)

  val stringPredicate2: Gen[String => Boolean] = for {
    s <- Arbitrary.arbitrary[String]
  } yield (x: String) => x.startsWith(s)

  val stringPredicate3: Gen[String => Boolean] = for {
    s <- Arbitrary.arbitrary[String]
  } yield (x: String) => x.endsWith(s)

  val stringPredicate4: Gen[String => Boolean] = for {
    s <- Arbitrary.arbitrary[String]
  } yield (x: String) => x.contains(s)

  def stringPredicate: Gen[String => Boolean] =
    Gen.oneOf(stringPredicate1,
              stringPredicate2,
              stringPredicate3,
              stringPredicate4)

  val stringToInt1: Gen[String => Int] = for {
    s <- Arbitrary.arbitrary[String]
  } yield (x: String) => x.compareTo(s)

  val stringToInt2: Gen[String => Int] = Gen.const(_.hashCode)

  val stringToInt3: Gen[String => Int] = Gen.const(_.length)

  val stringToInt4: Gen[String => Int] = for {
    c <- Arbitrary.arbitrary[Char]
  } yield (x: String) => x.indexOf(c)

  def stringToInt: Gen[String => Int] =
    Gen.oneOf(stringToInt1, stringToInt2, stringToInt3, stringToInt4)

  val intToInt1: Gen[Int => Int] = for {
    y <- Arbitrary.arbitrary[Int]
  } yield (x: Int) => x + y

  val intToInt2: Gen[Int => Int] = for {
    y <- Arbitrary.arbitrary[Int]
  } yield (x: Int) => x - y

  val intToInt3: Gen[Int => Int] = for {
    y <- Arbitrary.arbitrary[Int]
  } yield (x: Int) => x * y

  val intToInt4: Gen[Int => Int] = for {
    y <- Arbitrary.arbitrary[Int]
  } yield (x: Int) => x / y

  val intToInt5: Gen[Int => Int] = for {
    y <- Arbitrary.arbitrary[Int]
  } yield (x: Int) => x max y

  val intToInt6: Gen[Int => Int] = for {
    y <- Arbitrary.arbitrary[Int]
  } yield (x: Int) => x min y

  val intToInt: Gen[Int => Int] = Gen.oneOf(
    intToInt1,
    intToInt2,
    intToInt3,
    intToInt4,
    intToInt5,
    intToInt6
  )

  val stringToStrings1: Gen[String => Iterable[String]] = for {
    c <- Arbitrary.arbitrary[Char]
  } yield (x: String) => x.split(c)

  val stringToStrings2: Gen[String => Iterable[String]] = for {
    i <- Arbitrary.arbitrary[Int]
  } yield
    (x: String) => {
      val (a, b) = x.splitAt(i)
      List(a, b)
    }

  val stringToStrings3: Gen[String => Iterable[String]] = for {
    c <- Arbitrary.arbitrary[Char]
  } yield (x: String) => x.toCharArray.map(_.toString)

  val stringToString: Gen[String => Iterable[String]] = Gen.oneOf(
    stringToStrings1,
    stringToStrings2,
    stringToStrings3
  )

  val stringToIntPF: Gen[PartialFunction[String, Int]] = for {
    pred <- stringPredicate
    f    <- stringToInt
  } yield {
    val pf: PartialFunction[String, Int] = {
      case s if pred(s) => f(s)
    }
    pf
  }

  implicit val arbStringToIntPF = Arbitrary(stringToIntPF)

}
