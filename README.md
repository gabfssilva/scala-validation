# scala-validation
A simple library for object validations

#### This library is under development, so, any help is welcome. ;)

## Set up

##### Note: This library targets only Scala 2.12

```scala
resolvers += "Scala Validation Releases" at "http://dl.bintray.com/scala-validation/releases"

libraryDependencies += "org.scala.validation" %% "core" % "0.0.5"
```

## Simple example

```scala

import org.validation.scala._
import org.validation.scala.matchers._

case class Person(name: String,
                  occupation: String,
                  age: Int,
                  address: Address,
                  phones: List[Phone],
                  favoriteNumbers: List[Int])

case class Address(address: String)

case class Phone(phoneNumber: String)


object validators {
  implicit val addressValidator = Validator { address: Address =>
    assure(address.address is notBlank) { "address cannot be empty" }
  }

  implicit val phoneValidator = Validator { phone: Phone =>
    assure(phone.phoneNumber is notBlank) { "phone number be null" } ~
    assure(phone.phoneNumber.length is higherOrEqualTo(8)) { s"phone number must have at least 8 numbers and ${phone.phoneNumber} has ${phone.phoneNumber.length}" }
  }

  implicit val personValidator = Validator { person: Person =>
    assure(person.name is notNull) { "name cannot be null" } ~
    assure(person.occupation is notBlank) { "occupation cannot be null" } ~
    assure(person.age is higherThan(0)) { "age cannot be lower than 1" } ~
    assure(person.favoriteNumbers contains 4) { "favorite numbers must contain number 4" } ~
    assureEntity { person.address } ~
    assureEntities { person.phones }
  }
}

object Main extends App {
  import validators._

  val phones = List(Phone("88888888"), Phone("999999999"), Phone("7777777"))
  val address = Address("")
  val person = Person(null, null, 1, address, phones, List(3, 2, 1))

  val result = validate(person)

  result.foreach { v =>
    println(v.message)
  }

  /** it will be printed:
    
     favorite numbers must contain number 4
     name cannot be null
     occupation cannot be null
     address cannot be empty
     phone number must have at least 8 numbers and 7777777 has 7
  */
}
```

## Validating with paths

If you need to, you can provide "paths" to help you find where the violation occurred.

Basically, the semantics are:

```scala
//for a single value
assure("path" ~> boolean) { violationMessage }
```
```scala
//for another entity
assureEntity { "path" ~> entity }
```

```scala
//for an iterable
assureEntities { "path" ~> entities }
```

```scala
//base path on declaring a validator
Validator("basePath") { entity: T => ... }
```

```scala
//or on validating:
validate("basePath" ~> entity)
```

### Example:

```scala
import org.validation.scala._
import org.validation.scala.matchers._

case class Person(name: String,
                  occupation: String,
                  age: Int,
                  address: Address,
                  phones: List[Phone],
                  favoriteNumbers: List[Int])

case class Address(address: String)

case class Phone(phoneNumber: String)


object validators {
  implicit val addressValidator = Validator { address: Address =>
    assure("address" ~> (address.address is notBlank)) { "address cannot be empty" }
  }

  implicit val phoneValidator = Validator { phone: Phone =>
    assure("phoneNumber" ~> (phone.phoneNumber is notBlank)) { "phone number be null" } ~
    assure("phoneNumber" ~> (phone.phoneNumber.length is higherOrEqualTo(8))) { s"phone number must have at least 8 numbers and ${phone.phoneNumber} has ${phone.phoneNumber.length}" }
  }

  implicit val personValidator = Validator { person: Person =>
    assure("name" ~> (person.name is notNull)) { "name cannot be null" } ~
    assure("occupation" ~> (person.occupation is notBlank)) { "occupation cannot be null" } ~
    assure("age" ~> (person.age is higherThan(0))) { "age cannot be lower than 1" } ~
    assure("favoriteNumbers" ~> (person.favoriteNumbers contains 4)) { "favorite numbers must contain number 4" } ~
    assureEntity { "address" ~> person.address } ~
    assureEntities { "phones" ~>  person.phones }
  }
}

object Main extends App {
  import validators._

  val phones = List(Phone("88888888"), Phone("999999999"), Phone("7777777"))
  val address = Address("")
  val person = Person(null, null, 1, address, phones, List(3, 2, 1))

  val result = validate("person" ~> person)

  result.foreach { v =>
    println(s"${v.path} - ${v.message}")
  }

  /** it will be printed:

      Some(person.favoriteNumbers) - favorite numbers must contain number 4
      Some(person.name) - name cannot be null
      Some(person.occupation) - occupation cannot be null
      Some(person.address.address) - address cannot be empty
      Some(person.phones[2].phoneNumber) - phone number must have at least 8 numbers and 7777777 has 7
  */
}
```