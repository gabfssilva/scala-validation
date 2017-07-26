package org.validation.scala

object Validator {
  def apply[T](f: T => List[Validation]) = new Validator[T]() {
    override def validate(entity: T): List[Validation] = f(entity)
  }

  def apply[T](basePath: String)(f: T => List[Validation]) = new Validator[T]() {
    override def validate(entity: T): List[Validation] = {
      f(entity).map { validation =>
        new Validation(
          validation.validationExpression,
          validation.violationMessage,
          validation.fieldPath match {
            case None => None
            case Some(path) => Some(s"$basePath.$path")
          }
        )
      }
    }
  }
}

trait Validator[T] {
  def validate(entity: T): List[Validation]
}