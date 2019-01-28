package K10coroutines

import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.system.measureTimeMillis

// Application contain 1+ process
// Process contains 1+ threads
// Thread contains 1+ coroutines

// Coroutines:
// Threads are expensive to create, and heavy to switch. Coroutines are "lightweight threads", computations or tasks that
// the actual threads can compute. A thread can stop executing a coroutine at some specific “suspension points”, and go
// do some other work. It can resume executing the coroutine later on, or another thread could even take over.

// Suspending functions:
// Functions that are marked with suspend may suspend the execution of the current coroutine without blocking the current
// thread, by suspending it allows the current thread to do other computations. Suspending functions are only asynchronous
// if they are explicitly used as such, i.e in a coroutine context. Suspending functions can only be invoked from other
// suspending functions or from coroutines.

// Coroutine builders:
// Coroutine builders are simple functions that create a new coroutine to run a given suspending function. They can be
// called from normal non-suspending functions because they are not suspending themselves, and thus act as a bridge
// between the normal and the suspending world.
//
// Examples:
// async() is used when a result is expected. It returns a Deferred<T> which either contains result or an exception.
// launch() is used when no result is expected. It returns a Job that can be used to cancel its excecution.
// runBlocking() is used to bridge blocking code into suspendable code.

fun main(args: Array<String>) {
    runBlocking { // this blocks main thread until computation is finished

        val sequentialTime = measureTimeMillis {
            val firstAge: Long = calculateAge(1982, 10, 23)
            val secondAge: Long = calculateAge(1987, 4, 11)
        }
        println("time for calculation: $sequentialTime ms") // time for calculation: 2085 ms

        val asyncTime = measureTimeMillis {
            val firstAgeAsync: Deferred<Long> = async { calculateAge(1982, 10, 23) }
            val secondAgeAsync: Deferred<Long> = async { calculateAge(1987, 4, 11) }
            firstAgeAsync.await()
            secondAgeAsync.await()
        }
        println("time for calculation: $asyncTime ms") // time for calculation: 1012 ms
    }
}

// suspending function
suspend fun calculateAge(year: Int, month: Int, day: Int): Long {
    val today = LocalDate.now();
    val dayOfBirth = LocalDate.of(year, month, day)
    val yearsOfDifference = ChronoUnit.YEARS.between(dayOfBirth, today)
    delay(1000) // delay is a suspending function that will suspend the current coroutine
    return yearsOfDifference
}
