package K8reflection

import org.reflections.Reflections
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.starProjectedType

// Inversion of Control is a principle in software engineering by which the control of objects is transferred to a
// container or framework. Inversion of Control can be achieved through various mechanisms such as: Strategy design
// pattern, Service Locator pattern, Factory pattern, and Dependency Injection (DI). In this case we explore Dependency
// Injection

@Target(AnnotationTarget.CLASS)
annotation class Configuration

@Target(AnnotationTarget.FUNCTION)
annotation class Bean

class ConfigurationException(msg: String): RuntimeException(msg)

class DIContainer(config: Any) {
    val classMap: MutableMap<KType, Any?> = mutableMapOf()

    init {
        val configClass = config::class
        if (configClass.findAnnotation<Configuration>() == null) {
            throw ConfigurationException("Class missing @Configuration annotation")
        }
        val functions = configClass.memberFunctions
        val beanFunctions = functions.filter { it.findAnnotation<Bean>() != null }
        beanFunctions.forEach {
            classMap[it.returnType] = it.call(config)
        }
    }

    inline operator fun <reified T: Any> get(kClass: KClass<T>): T? {
        return classMap[kClass.starProjectedType] as? T
    }
}

interface SuperHero {
    fun catchPhrase(): String
}

class HumanTorch : SuperHero {
    override fun catchPhrase()= "Flame on!"
}

@Configuration
class AppConfig {
    @Bean
    fun superHero(): SuperHero {
        return HumanTorch()
    }
}

fun main(args: Array<String>) {
    val config = AppConfig()
    val diContainer = DIContainer(config)
    val superHero = diContainer[SuperHero::class]
    println(superHero?.catchPhrase()) // Flame on!
}