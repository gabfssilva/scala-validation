package org.validation

import org.validation.scala.matchers.ValidationMatcher

/**
  * @author Gabriel Francisco <gabfssilva@gmail.com>
  */
package object scala {
  def validate[T](entityData: (String, T))(implicit validator: Validator[T]): List[Violation] = {
    entityData match {
      case (null, entity) =>
        validator
          .validate(entity)
          .filter(!_.validationExpression)
          .map(v => Violation(v.violationMessage, v.fieldPath))

      case (path, entity) =>
        val v = Validator(path) { obj: T =>
          validator.validate(entity)
        }

        v.validate(entity)
          .filter(!_.validationExpression)
          .map(v => Violation(v.violationMessage, v.fieldPath))
    }
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

  implicit def asTuple[T](entity: => T): (String, T) = Tuple2(null, entity)

  def assure(check: (String, Boolean))(violation: => String): Validation = {
    val (path, c) = check
    new Validation(c, violation, Option(path))
  }

  def assureEntity[T](validatableData: => (String, T))(implicit entityValidator: Validator[T]): List[Validation] = {
    validatableData match {
      case (null, entity) => entityValidator.validate(entity)
      case (basePath, entity) => Validator(basePath) { obj: T => entityValidator.validate(obj) }.validate(entity)
    }
  }

  def assureEntities[T](validatableData: (String, Iterable[T]))(implicit entityValidator: Validator[T]): List[Validation] = {
    val (basePath, entities) = validatableData
    entities.zipWithIndex.flatMap { elem =>
      val (entity, index) = elem
      assureEntity(s"$basePath[$index]" ~> entity)
    }.toList
  }

  implicit class AnyMatchers(obj: Any) {
    def is(exp: => ValidationMatcher[Any]): Boolean = exp(obj)
  }

  implicit class StringMatchers(obj: String) {
    def is(exp: => ValidationMatcher[String]): Boolean = exp(obj)

    def ~>[T](entity: => T) = Tuple2(obj, entity)
  }

  implicit class AnyValMatchers(obj: AnyVal) {
    def is(exp: => ValidationMatcher[AnyVal]): Boolean = exp(obj)
  }

  implicit class IntMatchers(obj: Int) {
    def is(exp: => ValidationMatcher[Int]): Boolean = exp(obj)
  }

}