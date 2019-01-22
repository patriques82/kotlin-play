package K8reflection

import java.lang.StringBuilder
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

// Source: https://github.com/yole/jkid/blob/master/src/main/kotlin/serialization/Serializer.kt

fun serialize(obj: Any): String = buildString { serialize(obj) }

private fun StringBuilder.serialize(obj: Any) {
    val klass = obj.javaClass.kotlin
    val memberProperties = klass.memberProperties
    memberProperties.reversed().joinTo(this, prefix = "{", postfix = "}") {
        buildString { serializeProperty(it, obj) }
    }
}

private fun StringBuilder.serializeProperty(prop: KProperty1<Any, *>, receiver: Any) {
    serializeString(prop.name)
    append(": ")
    val value = prop.get(receiver)
    serializeValue(value)
}

private fun StringBuilder.serializeValue(value: Any?) {
    when(value) {
        null -> append("null")
        is String -> serializeString(value)
        is Number, is Boolean -> append(value.toString())
        else -> serialize(value)
    }
}

private fun StringBuilder.serializeString(s: String) {
    append('\"')
    append(s)
    append('\"')
}

data class Dream(val owner: String, val level: Int, val dream: Dream?)

fun main(args: Array<String>) {

    val cobbsDream = Dream("Saito", 3, null)
    val eamesDream = Dream("Eames", 2, cobbsDream)
    val arthursDream = Dream("Arthur", 1, eamesDream)
    val inception = Dream("Yusuf", 0, arthursDream)

    val json = serialize(inception)
    println(json)
    // {"owner": "Yusuf", "level": 0, "dream": {"owner": "Arthur", "level": 1, "dream": {"owner": "Eames", "level": 2, "dream": {"owner": "Saito", "level": 3, "dream": null}}}}
}