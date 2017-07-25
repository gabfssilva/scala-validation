package org.validation.scala

import org.scalatest.{FeatureSpec, Matchers}

/**
  * @author Gabriel Francisco <gabfssilva@gmail.com>
  */
class ValidatorFeature extends FeatureSpec with Matchers {

  feature("Assure that basic validation is working") {
    scenario("with int value") {
      import org.validation.scala._
      import org.validation.scala.matchers._

      implicit val intValidator = Validator { i: Int =>
        assure(i is equalTo(0)) {
          "int cannot be different than zero"
        }
      }

      val result = validate(2)

      result should contain(Violation("int cannot be different than zero"))
    }

    scenario("with string value") {
      import org.validation.scala._

      implicit val stringValidator = Validator { i: String =>
        import org.validation.scala.matchers._

        assure(i is equalTo("this is just a string")) {
          "string not expected"
        }
      }

      val result = validate("this is just a string")

      result shouldBe empty
    }

    scenario("with case class") {
      import org.validation.scala._
      import org.validation.scala.matchers._

      case class Person(name: String, age: Int, occupation: String)

      implicit val personValidator = Validator { person: Person =>
        assure(person.name is notBlank) {
          "name cannot be empty or null"
        } ~ assure(person.age is higherThan(17)) {
          "age cannot be lower than 18"
        } ~ assure(person.occupation is equalTo("Software Engineer")) {
          "sorry, you must be a software engineer"
        }
      }

      val result = validate(Person("Gabriel", 24, "Lawyer"))

      result.size shouldBe 1
      result should contain(Violation("sorry, you must be a software engineer"))
    }
  }

}
