package K1classes

// generates hashcode, equals, toString, copy and componentN (destructuring functions)
data class User(val firstname: String, val lastname: String)
                // component1          // component2

fun main(args: Array<String>) {
    val u1 = User("Patrik", "Nygren")
    val u2 = u1.copy(firstname = "Kristoffer")

    println(u1) // User(firstname=Patrik, lastname=Nygren)
    println(u2) // User(firstname=Kristoffer, lastname=Nygren)

    val u3 = User("Patrik", "Nygren")
    println(u1 == u3) // true

    val (k, n) = u2
    println(k) // Kristoffer
}