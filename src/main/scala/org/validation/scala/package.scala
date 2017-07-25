package org.validation

import org.validation.scala.matchers.ValidationMatcher

/**
  * @author Gabriel Francisco <gabfssilva@gmail.com>
  */
package object scala {
  def validate[T](entity: T)(implicit validator: Validator[T]): List[Violation] = {
    validator
      .validate(entity)
      .filter(!_.validationExpression)
      .map(v => Violation(v.violationMessage))
  }

  implicit class ListValidation(list: List[Validation]) {
    def ~(x: Validation): List[Validation] = new ::(x, list)

    def ~(x: List[Validation]): List[Validation] = list ::: x
  }

  implicit class SingleValidationImplicit(v: Validation) {
    def ~(x: Validation): List[Validation] = v :: x :: Nil

    def ~(x: List[Validation]): List[Validation] = v :: x
  }

  implicit def singleValidationAsList(v: Validation): List[Validation] = List(v)

  def assure(check: Boolean)(violationMessage: => String): Validation = {
    new Validation(check, violationMessage)
  }

  def assureEntity[T](entity: => T)(implicit entityValidator: Validator[T]): List[Validation] = {
    entityValidator.validate(entity)
  }

  def assureEntities[T](entities: => List[T])(implicit entityValidator: Validator[T]): List[Validation] = {
    entities.flatMap { entity =>
      assureEntity(entity)
    }
  }

  implicit class AnyMatchers(obj: Any) {
    def is(exp: => ValidationMatcher[Any]): Boolean = exp(obj)
  }

  implicit class StringMatchers(obj: String) {
    def is(exp: => ValidationMatcher[String]): Boolean = exp(obj)
  }

  implicit class AnyValMatchers(obj: AnyVal) {
    def is(exp: => ValidationMatcher[AnyVal]): Boolean = exp(obj)
  }

  implicit class IntMatchers(obj: Int) {
    def is(exp: => ValidationMatcher[Int]): Boolean = exp(obj)
  }

}