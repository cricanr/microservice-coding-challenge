case class Person(name: String, age: Int)

List(Person("joe",21), Person("fred",22), Person("joe", 20), Person("fred",22)).map(_.name).distinct
