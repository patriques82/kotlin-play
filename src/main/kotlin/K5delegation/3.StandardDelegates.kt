package K5delegation

import kotlin.properties.Delegates

class SqlResult

fun queryDb(query: String): SqlResult {
    // do some heavy db stuff...
    return SqlResult()
}

// only done once when variable is used and cached for further usage
val lazyValue: Lazy<SqlResult> = lazy {
    queryDb("Select * from DB.Users")
}

// actually not delegating but enabling observation of changes to var
var observableValue by Delegates.observable("Scala") {
    prop, old, new -> println("Changing ${prop.name} from $old to $new")
}

fun main(args: Array<String>) {
    println(lazyValue.isInitialized()) // false
    lazyValue.value
    println(lazyValue.isInitialized()) // true

    println(observableValue) // Scala
    observableValue = "Kotlin" // Changing observableValue from Scala to Kotlin

}