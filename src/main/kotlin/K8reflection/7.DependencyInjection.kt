package K8reflection

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.starProjectedType

// Inversion of Control is a principle in software engineering by which the control of objects is transferred to a
// container or framework. In this case we explore Dependency Injection (DI) as mechanism to invert the control of
// object creation.

@Target(AnnotationTarget.FUNCTION)
annotation class Bean

class ConfigurationException(msg: String): RuntimeException(msg)

class DIContainer(config: Any) {
    val classMap: MutableMap<KType, Any?> = mutableMapOf()

    init {
        val functions = config::class.memberFunctions
        val beanFunctions = functions.filter { it.findAnnotation<Bean>() != null }
        if (beanFunctions.isEmpty()) {
            throw ConfigurationException("Missing instance creation methods annotated with @Bean")
        }
        beanFunctions.forEach {
            classMap[it.returnType] = it.call(config)
        }
    }

    inline operator fun <reified T: Any> get(kClass: KClass<T>): T? =
        classMap[kClass.starProjectedType] as? T
}

interface SuperHero {
    val name: String
    val catchPhrase: String
}

class HumanTorch: SuperHero {
    override val name = "Human Torch"
    override val catchPhrase = "Flame on!"
}

interface Mission {
    fun execute()
}

class SaveTheDay(val superHero: SuperHero): Mission {
    override fun execute() {
        println("${superHero.name} will save the day. ${superHero.catchPhrase}")
    }
}

class AppConfig {
    @Bean
    fun superHero(): SuperHero {
        return HumanTorch()
    }

    @Bean
    fun mission(): Mission {
        return SaveTheDay(superHero())
    }
}

fun main(args: Array<String>) {
    val diContainer = DIContainer(AppConfig())
    val mission = diContainer[Mission::class]
    mission?.execute() // Human Torch will save the day. Flame on!
}