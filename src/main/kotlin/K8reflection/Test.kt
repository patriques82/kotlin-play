package K8reflection

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

annotation class Instances(val nr: Int)

annotation class Root

class ObjectCreationException(msg: String): RuntimeException(msg)

interface ObjectGraph<out T: Any> {
    fun print()
}

inline fun <reified T: Any> createObjectGraph(clazz: KClass<T>): ObjectGraph<T> {
    assertRootAnnotation(clazz)
    val constructor = clazz.primaryConstructor // TODO move to assert
    val constructorParams = constructor?.parameters
    val constructorValues = constructorParams!!.forEach {
        it.type.classifier
    }
    constructor.callBy(mapOf())
    //val ty = typeParams[0].javaClass.kotlin.objectInstance
    return object : ObjectGraph<T> {
        override fun print() {
            println(clazz.simpleName)
        }
    }
}

inline fun <reified T: Any> assertRootAnnotation(clazz: KClass<T>) {
    clazz.annotations.filter { it.annotationClass == Root::class }.firstOrNull() ?:
        throw ObjectCreationException("${clazz.simpleName} class is not annotated as Root")
}

@Instances(3)
class Dream(val dream: Dream?)

@Root
class Inception(val dream: Dream)


fun main(args: Array<String>) {
    val objectGraph = createObjectGraph(Inception::class)
    objectGraph.print()
    // Inception(dream)
    // -> Dream
    // Dream(dream)
    // -> Dream?
    // Dream(dream)
    // -> Dream?
    // Dream(dream)
    // -> Dream?
}