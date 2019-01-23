package K8reflection

import kotlin.reflect.KParameter

class Human(var isAlive: Boolean = true)

class Terminator(var numberOfKills: Int)

fun terminate(terminator: Terminator, human: Human) {
    human.isAlive = false
    terminator.numberOfKills.dec()
}

// Parameter references: KParameter (represents function or property getter/setter parameters)

fun main(args: Array<String>) {
    val terminator = Terminator(0)
    val human = Human()

    val terminatorParam: KParameter = terminator::class.constructors.firstOrNull()!!.parameters[0]

    println(terminatorParam.name) // numberOfKills
    println(terminatorParam.type) // Kotlin.Int

    val terminateParam: KParameter = ::terminate.parameters[0]
    println(terminateParam.name) // terminator
    println(terminateParam.type) // K8reflection.Terminator

    val humanParam = ::terminate.parameters[1]
    println(humanParam.name) // human
    println(humanParam.type) // K8reflection.Human

}