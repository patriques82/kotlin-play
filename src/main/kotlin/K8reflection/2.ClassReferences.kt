package K8reflection

import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

// Class reference: KClass<T : Any> (represents objects and classes)

val kotlinStringClass: KClass<String> = String::class // Kotlin class
val javaStringClass: Class<String> = kotlinStringClass.java // Java class
val kotlinIntClass = Int::class

fun main(args: Array<String>) {
    val kotlin = "Kotlin"

    println(kotlinStringClass.simpleName)
    // String
    kotlinStringClass.memberProperties.forEach {
        println(it) // val kotlin.String.length: kotlin.Int
        println(it.get(kotlin)) // 6
    }
    println(kotlinStringClass.javaPrimitiveType)
    // null

    javaStringClass.methods.forEach { println(it) }
    // public boolean java.lang.String.equals(java.lang.Object)
    // public java.lang.String java.lang.String.toString()
    // etc...
    println(kotlinIntClass.javaPrimitiveType)
    // int
}
