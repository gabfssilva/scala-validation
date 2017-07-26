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
        assure(i is equalTo(0)) { "int cannot be different than zero" }
      }

      val result = validate(2)

      result should contain(Violation("int cannot be different than zero"))
    }

    scenario("with string value") {
      import org.validation.scala._

      implicit val stringValidator = Validator { i: String =>
        import org.validation.scala.matchers._

        assure(i is equalTo("this is just a string")) { "string not expected" }
      }

      val result = validate("this is just a string")

      result shouldBe empty
    }

    scenario("with case class") {
      import org.validation.scala._
      import org.validation.scala.matchers._

      case class Person(name: String, age: Int, occupation: String)

      implicit val personValidator = Validator { person: Person =>
        assure(person.name is notBlank) { "name cannot be empty or null" } ~
        assure(person.age is higherThan(17)) { "age cannot be lower than 18" } ~
        assure(person.occupation is equalTo("Software Engineer")) { "sorry, you must be a software engineer" }
      }

      val result = validate(Person("Gabriel", 24, "Lawyer"))

      result.size shouldBe 1
      result should contain(Violation("sorry, you must be a software engineer"))
    }

    scenario("with one to one relationship") {
      import org.validation.scala._
      import org.validation.scala.matchers._

      case class Person(name: String, age: Int, occupation: String, address: Address)
      case class Address(address: String)

      implicit val addressValidator = Validator { address: Address =>
        assure(address.address == "haha") { "address field must be 'haha'" }
      }

      implicit val personValidator = Validator { person: Person =>
        assure("person.name" ~> (person.name is notBlank)) { "name cannot be empty or null" } ~
        assureEntity { "person.address" ~> person.address } ~
        assure(person.age is higherThan(17)) { "age cannot be lower than 18" } ~
        assure(person.occupation is equalTo("Software Engineer")) { "sorry, you must be a software engineer" }
      }

      val result = validate(Person("Gabriel", 24, "Lawyer", Address("hoho")))

      result.size shouldBe 2
      result should contain(Violation("sorry, you must be a software engineer"))
      result should contain(Violation("address field must be 'haha'"))
    }

    scenario("with one to many relationship") {
      import org.validation.scala._
      import org.validation.scala.matchers._

      case class Person(name: String, age: Int, occupation: String, addresses: List[Address])
      case class Address(address: String)

      implicit val addressValidator = Validator { address: Address =>
        assure("address" ~> (address.address == "haha")) { s"address field must be 'haha', not '${address.address}'" }
      }

      implicit val personValidator = Validator { person: Person =>
        assure(person.name is notBlank) { "name cannot be empty or null" } ~
        assureEntities { "addresses" ~> person.addresses } ~
        assure(person.age is higherThan(17)) { "age cannot be lower than 18"} ~
        assure("occupation" ~> (person.occupation is equalTo("Software Engineer"))) { "sorry, you must be a software engineer" }
      }

      val result = validate("person" ~> Person("Gabriel", 24, "Lawyer", List(Address("hoho"), Address("hehe"), Address("haha"))))

      result.size shouldBe 3
      result should contain(Violation("sorry, you must be a software engineer", Some("person.occupation")))
      result should contain(Violation("address field must be 'haha', not 'hoho'", Some("person.addresses[0].address")))
      result should contain(Violation("address field must be 'haha', not 'hehe'", Some("person.addresses[1].address")))
    }
  }

}
