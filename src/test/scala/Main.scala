import Main.Person

import org.validation.scala._
import org.validation.scala.matchers._

object validators {
  implicit val personValidator = Validator { person: Person =>
    assure(person.name is notNull) {
      "name cannot be null"
    } ~ assure(person.occupation is notBlank) {
      "occupation cannot be null"
    } ~ assure(1 is higherThan(0)) {
      "age cannot be null"
    }
  }
}

object Main extends App {
  case class Person(name: String, occupation: String, age: Int)

  //needed to method validate find the right validator
  import validators.personValidator

  val result = validate(Person(null, null, 1))

  assure(List(1, 2, 3) contains 2) {

  }

  result.foreach { v =>
    println(v.message)
  }
}
