package K8reflection

import kotlin.reflect.*


// Class references: KClass<T : Any> (represents objects and classes)

val kotlinStringClass: KClass<String> = String::class

val javaStringClass: Class<String> = kotlinStringClass.java // java class
val className = kotlinStringClass.simpleName // className == String

fun main(args: Array<String>) {
    //kotlinStringClass.memberProperties.forEach { println(it) }
    // val kotlin.String.length: kotlin.Int
    //javaStringClass.methods.forEach { println(it) }
    // public boolean java.lang.String.equals(java.lang.Object)
    // public java.lang.String java.lang.String.toString()
    // ....
    annotations.forEach { println(it.annotationClass) }
    //println(ex.annotationClass)
}


// Callable references: KCallable<out R> (represents functions and properties)

// - KFunction (represents functions)
//   and is implemented by KFunction1, KFunction2, etc.. these classes are compiler generated and have no sourcecode

fun isKotlin(programmingLanguage: String) = programmingLanguage == "Kotlin"

val kotlinCallable: KCallable<Boolean> = ::isKotlin
val clojure = kotlinCallable.call("Clojure") // false

val kotlinFunction: KFunction1<String, Boolean> = ::isKotlin
val kotlin = kotlinFunction.invoke("Kotlin") // true

// - KProperty (represents properties)

val bar = 0
var foo = 0

val barCallable: KCallable<Int> = ::bar
val zero = barCallable.call() // 0

val barProperty: KProperty0<Int> = ::bar // top-level properties are represented by KProperty0
val zeroAgain = barProperty.get() // 0

val fooProperty: KMutableProperty0<Int> = ::foo
val bogus: Unit = fooProperty.set(9) // foo is now 9

class Poop(val inches: Int)
val poop = Poop(3)

val memberProperty: KProperty1<Poop, Int> = Poop::inches // member-properties are represented by KProperty1
val inches = memberProperty.get(poop) // 3



// Parameter references: KParameter (represents function or property getter/setter parameters)

val poopParameter: KParameter = Poop::class.constructors.firstOrNull()!!.parameters[0]
val poopParameterName: String? = poopParameter.name // inches
val poopParameterType: KType = poopParameter.type // kotlin.Int

val isKotlinParam: KParameter = ::isKotlin.parameters[0]
val isKotlinParamName: String? = isKotlinParam.name // programmingLanguage
val isKotlinParamType: KType = isKotlinParam.type // kotlin.String



// Annotations: KAnnotatedElement (represents an annotated element and allows to obtain its annotations)
/**
 *   public interface KAnnotatedElement {
 *       public val annotations: List<Annotation>
 *   }
 **/

// All the classes above implements this interface

// KClass<T : Any>  : KAnnotatedElement
// KCallable<out R> : KAnnotatedElement
// KParameter       : KAnnotatedElement

@Example
@Example3(String::class, Int::class)
class ExampleStack

val annotations = ExampleStack::class.annotations // [Example, Example3]