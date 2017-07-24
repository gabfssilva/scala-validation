package org.validation.scala

/**
  * @author Gabriel Francisco <gabfssilva@gmail.com>
  */
class Validation(val validationExpression: Boolean, message: => String) {
  def violationMessage: String = message
}
case class Violation(message: String)