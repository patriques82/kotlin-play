package K5delegation

import kotlin.reflect.KProperty

class DelegateString() {
    private var current = "Java"
    // For a read-only property (i.e. a val), a delegate has to provide a function named getValue

    // thisRef — must be the same or a supertype of the property owner (for extension properties — the type being extended);
    // prop — must be of type KProperty<*> or its supertype.
    // this function must return the same type as property (or its subtype).
    operator fun getValue(thisRef: Caller, prop: KProperty<*>): String {
        println("${thisRef.javaClass.simpleName} is getting '${prop.name}' with value ($current)")
        return current
    }

    // For a mutable property (a var), a delegate has to additionally provide a function named setValue

    // thisRef — same as for getValue();
    // property — same as for getValue();
    // newValue — must be of the same type as a property or its supertype.
    operator fun setValue(thisRef: Caller, prop: KProperty<*>, newValue: String) {
        println("${thisRef.javaClass.simpleName} is setting ${prop.name} to $newValue")
        current = newValue
    }
}

class Caller {
    var proxyString: String by DelegateString()
}

fun main(args: Array<String>) {
    val c = Caller()
    println(c.proxyString)
    // Caller is getting 'proxyString' with value (Java)
    // Java
    c.proxyString = "Kotlin"
    // Caller is setting proxyString to Kotlin
    println(c.proxyString)
    // Caller is getting 'proxyString' with value (Kotlin)
    // Kotlin
}