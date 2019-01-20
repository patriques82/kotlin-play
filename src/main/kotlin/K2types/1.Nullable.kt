package K2types

class Person(val name: String, val email: String?)

fun emailUserName(p: Person): String? = p.email?.substringBefore('@') // safe call

fun hasGmailAccount(p: Person): Boolean {
    val domain = p.email?.substringAfter('@') ?: return false // elvis operator
    return domain == "gmail.com"
}

fun isObjectContactable(o: Any?): Boolean {
    val person = o as? Person ?: return false // safe cast
    return person.email != null
}

fun sendToEmail(p: Person) = p.email?.let { email -> /* sending to email */ } // let function

fun main(args: Array<String>) {
    val p = Person("Patrik", null)
    val k = Person("Kristoffer", "brightkris@gmail.com")

    println(emailUserName(k)) // brightkris
    println(emailUserName(p)) // null

    println(hasGmailAccount(k)) // true
    println(hasGmailAccount(p)) // false

    println(isObjectContactable(k)) // true
    println(isObjectContactable(Unit)) // false
}