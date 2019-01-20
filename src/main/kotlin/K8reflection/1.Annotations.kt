package K8reflection

import kotlin.reflect.KClass

// Basics

annotation class Example

@Example
class Foo

// When you're annotating a property or a primary constructor parameter, there are multiple Java elements which are
// generated from the corresponding Kotlin element, therefore you can specify how the annotation should be generated

class FooBar(@field:Example val foo: String, // annotate Java field
             @get:Example val bar: Double,   // annotate Java getter
             @param:Example val quux: Int)   // annotate Java constructor parameter


// Meta-annotations are used to annotate annotations

// @Target: the possible elements which can be annotated with the annotation
//
// @Retention: annotation is stored in the compiled class files or whether it's visible through reflection at runtime
//
// @MustBeDocumented: should be included in the class or method signature shown in the generated API documentation.

@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.EXPRESSION,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.ANNOTATION_CLASS, // meta-annotation
    AnnotationTarget.FIELD,
    AnnotationTarget.FILE,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.TYPE_PARAMETER,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.TYPEALIAS,
    AnnotationTarget.LOCAL_VARIABLE
)
@Retention(AnnotationRetention.SOURCE) // SOURCE, BINARY, or RUNTIME (both SOURCE and BINARY, which is default)
@MustBeDocumented
annotation class Example2


// If you need to specify a class as an argument of an annotation, use a Kotlin class (KClass)

annotation class Example3(val clazz1: KClass<*>, val clazz2: KClass<out Any>)

@Example3(Int::class, String::class)
class Bar