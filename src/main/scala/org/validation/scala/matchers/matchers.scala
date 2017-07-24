package org.validation.scala

import scala.reflect.ClassTag

/**
  * @author Gabriel Francisco <gabfssilva@gmail.com>
  */
package object matchers {
  def empty = new ValidationMatcher[String] {
    override def valid: (String) => Boolean = str => str.isEmpty
  }

  def typeOf[T](implicit c: ClassTag[T]) = new ValidationMatcher[Any] {
    override def valid: (Any) => Boolean = t => t.getClass.isAssignableFrom(c.runtimeClass)
  }

  def notNull = new ValidationMatcher[Any] {
    override def valid: (Any) => Boolean = any => any != null
  }

  def notBlank = new ValidationMatcher[String] {
    override def valid: (String) => Boolean = str => str != null && !str.isEmpty
  }

  def equalTo(other: Any) = new ValidationMatcher[Any] {
    override def valid: (Any) => Boolean = any => any == other
  }

  def theSame(other: Any): ValidationMatcher[Any] = equalTo(other)

  def higherThan(other: AnyVal) = new HigherThanMatcher(other)

  def lowerThan(other: AnyVal) = new LowerThanMatcher(other)

  def higherOrEqualTo(other: AnyVal) = new HigherOrEqualToMatcher(other)

  def lowerOrEqualTo(other: AnyVal) = new LowerOrEqualToMatcher(other)

  class LowerThanMatcher(other: AnyVal) extends ValidationMatcher[AnyVal] {
    override def valid: (AnyVal) => Boolean = value =>
      (value, other) match {
        case (n: Int, o: Int) => n < o
        case (n: Long, o: Long) => n < o
        case (n: Double, o: Double) => n < o
        case (n: Float, o: Float) => n < o
        case (n: Short, o: Short) => n < o
        case (n: Byte, o: Byte) => n < o
        case _ => false
      }
  }

  class LowerOrEqualToMatcher(other: AnyVal) extends ValidationMatcher[AnyVal] {
    override def valid: (AnyVal) => Boolean = value =>
      (value, other) match {
        case (n: Int, o: Int) => n <= o
        case (n: Long, o: Long) => n <= o
        case (n: Double, o: Double) => n <= o
        case (n: Float, o: Float) => n <= o
        case (n: Short, o: Short) => n <= o
        case (n: Byte, o: Byte) => n <= o
        case _ => false
      }
  }

  class HigherOrEqualToMatcher(other: AnyVal) extends ValidationMatcher[AnyVal] {
    override def valid: (AnyVal) => Boolean = value =>
      (value, other) match {
        case (n: Int, o: Int) => n >= o
        case (n: Long, o: Long) => n >= o
        case (n: Double, o: Double) => n >= o
        case (n: Float, o: Float) => n >= o
        case (n: Short, o: Short) => n >= o
        case (n: Byte, o: Byte) => n >= o
        case _ => false
      }
  }

  class HigherThanMatcher(other: AnyVal) extends ValidationMatcher[AnyVal] {
    override def valid: (AnyVal) => Boolean = value =>
      (value, other) match {
        case (n: Int, o: Int) => n > o
        case (n: Long, o: Long) => n > o
        case (n: Double, o: Double) => n > o
        case (n: Float, o: Float) => n > o
        case (n: Short, o: Short) => n > o
        case (n: Byte, o: Byte) => n > o
        case _ => false
      }
  }

}
