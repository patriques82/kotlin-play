package K8reflection

// Annotations: KAnnotatedElement (represents an annotated element and allows to obtain its annotations)
/**
 *   public interface KAnnotatedElement {
 *       public val annotations: List<Annotation>
 *   }
 **/

// Class references, callable references, and paramater references above, all implements this interface

@Example
@Example3(String::class, Int::class)
class ExampleStack

fun main(args: Array<String>) {
    val annotations = ExampleStack::class.annotations
    annotations.forEach { println(it.annotationClass.simpleName) }
    // Example
    // Example3
}
