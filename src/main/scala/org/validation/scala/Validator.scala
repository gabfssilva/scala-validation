package org.validation.scala

object Validator {
  def apply[T](f: T => List[Validation]) = new Validator[T]() {
    override def validate(entity: T): List[Validation] = f(entity)
  }
}

trait Validator[T] {
  def validate(entity: T): List[Validation]
}