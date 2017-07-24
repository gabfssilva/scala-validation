import org.validation.scala._
import org.validation.scala.matchers._

case class Person(name: String,
                  occupation: String,
                  age: Int,
                  favoriteNumbers: List[Int])

object validators {
  implicit val personValidator = Validator { person: Person =>
    assure(person.name is notNull) {
      "name cannot be null"
    } ~ assure(person.occupation is notBlank) {
      "occupation cannot be null"
    } ~ assure(person.age is higherThan(0)) {
      "age cannot be lower than 1"
    } ~ assure(person.favoriteNumbers contains 4) {
      "favorite numbers must contain number 4"
    }
  }
}

object Main extends App {
  import validators.personValidator

  val result = validate(Person(null, null, 1, List(3, 2, 1)))

  result.foreach { v =>
    println(v.message)
  }
}
