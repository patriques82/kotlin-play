package K10coroutines

import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.system.measureTimeMillis

// Application contain 1+ process
// Process contains 1+ threads
// Thread contains 1+ coroutines

// Coroutines:
// Coroutines are "lightweight threads", computations that threads can compute. These are created with coroutine builders.
// A thread can stop executing a coroutine at some specific “suspension points” (defined by suspending functions) and go
// do some other work. It can resume executing the coroutine later on, or if available another thread could even take over.

// Suspending functions:
// Functions that are marked with suspend may suspend the execution of the current coroutine without blocking the current
// thread, by suspending it allows the current thread to execute other coroutines. Suspending functions are only
// asynchronous if they are explicitly used as such. Suspending functions can only be invoked from coroutines (within
// coroutine builders) or other suspending functions.

// Coroutine builders:
// Simple functions that create a new coroutine to run a given suspending function.
//
// Examples:
// async() - used when a result is expected. It returns a Deferred<T> which either contains result or an exception.
// launch() - used when no result is expected. It returns a Job that can be used to cancel its excecution.
// runBlocking() - used to blocks main thread until computation is finished. It bridges blocking and suspendable code.

fun main(args: Array<String>) {

    runBlocking { // Coroutine builder (creates a coroutine)
        val sequentialTime = measureTimeMillis {
            val firstAge: Long = calculateAge(1982, 10, 23) // Not used in a asynchronous way
            val secondAge: Long = calculateAge(1987, 4, 11)
        }
        println("time for calculation: $sequentialTime ms") // time for calculation: 2085 ms

        val asyncTime = measureTimeMillis {
            val firstAgeAsync: Deferred<Long> = async { calculateAge(1982, 10, 23) } // Coroutine builder
            val secondAgeAsync: Deferred<Long> = async { calculateAge(1987, 4, 11) }
            firstAgeAsync.await()
            secondAgeAsync.await()
        }
        println("time for calculation: $asyncTime ms") // time for calculation: 1012 ms
    }

    // Coroutines are lightweight. Here we create 100 000 coroutines that each print '.'
    runBlocking {
        // launches 100 000 coroutines
        repeat(100_000) {
            launch {
                delay(1000L)
                print(".") //......etc
            }
        }
    }
}

// suspending function
suspend fun calculateAge(year: Int, month: Int, day: Int): Long {
    val today = LocalDate.now();
    val dayOfBirth = LocalDate.of(year, month, day)
    val yearsOfDifference = ChronoUnit.YEARS.between(dayOfBirth, today)
    delay(1000L) // delay is a suspending function that will suspend the current coroutine
    return yearsOfDifference
}
