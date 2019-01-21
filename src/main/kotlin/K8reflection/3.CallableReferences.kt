package K8reflection

import kotlin.reflect.*

val bar = 0
var foo = 0

fun isKotlin(programmingLanguage: String) = programmingLanguage == "Kotlin"

// Callable reference: KCallable<out R> (represents functions and properties)

fun main(args: Array<String>) {

    // - KFunction (represents functions)
    //   and is implemented by KFunction1, KFunction2, etc.. these classes are compiler generated and have no sourcecode

    val kotlinCallable: KCallable<Boolean> = ::isKotlin
    println(kotlinCallable.call("Clojure")) // false

    val kotlinFunction: KFunction1<String, Boolean> = ::isKotlin
    println(kotlinFunction.invoke("Kotlin")) // true


    // - KProperty (represents top-level properties and member properties)

    val barCallable: KCallable<Int> = ::bar
    println(barCallable.call()) // 0

    val barProperty: KProperty0<Int> = ::bar // top-level properties are represented by KProperty0
    println(barProperty.get()) // 0

    val fooProperty: KMutableProperty0<Int> = ::foo
    fooProperty.set(9)
    println(foo) // 9

    class Poop(val inches: Int)
    val poop = Poop(3)

    val memberProperty: KProperty1<Poop, Int> = Poop::inches // member properties are represented by KProperty1
    println(memberProperty.get(poop)) // 3
}

