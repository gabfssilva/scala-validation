package org.validation.scala.matchers

/**
  * @author Gabriel Francisco <gabfssilva@gmail.com>
  */
trait ValidationMatcher[-T] extends ((T) => Boolean) {
  def valid: T => Boolean
  override def apply(v1: T): Boolean = valid(v1)
}

trait DualValueValidationMatcher[T] extends ((T, T) => Boolean) {
  def valid: T => T => Boolean
  override def apply(v1: T, v2: T): Boolean = valid(v1)(v2)
}