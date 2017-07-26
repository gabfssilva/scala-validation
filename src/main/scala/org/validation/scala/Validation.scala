package org.validation.scala

/**
  * @author Gabriel Francisco <gabfssilva@gmail.com>
  */
class Validation(val validationExpression: Boolean,
                 message: => String,
                 path: Option[String] = None) {
  def violationMessage: String = message
  def fieldPath: Option[String] = path
}

case class Violation(message: String, path: Option[String] = None)