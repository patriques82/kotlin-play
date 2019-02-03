package K10coroutines

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main() {

    // The Job interface allows for cancellation of coroutines.
    runBlocking {
        val time = measureTimeMillis {
            val job: Job = launch {
                repeat(1000) {
                    println("Waiting ${it + 1} seconds before finishing.")
                    delay((it + 1) * 1000L)
                }
            }
            delay(4000L)
            job.cancelAndJoin()
        }
        println("Finished execution after $time")
    }
    // Waiting 1 seconds before finishing.
    // Waiting 2 seconds before finishing.
    // Waiting 3 seconds before finishing.
    // Finished execution after 4017

    // All the suspending functions in kotlinx.coroutines are cancellable. They check for the cancellation of coroutine
    // and throw a CancellationException when cancelled.
    runBlocking {
        val job = launch {
            try {
                repeat(1000) { i ->
                    println("I'm sleeping $i ...")
                    delay(500L)
                }
            } catch(ex: CancellationException) {
                println("exception: ${ex.message}")
            }
        }
        delay(1300L) // delay a bit
        println("I'm tired of waiting!")
        job.cancelAndJoin() // cancels the job and waits for its completion
    }
    // I'm sleeping 0 ...
    // I'm sleeping 1 ...
    // I'm sleeping 2 ...
    // I'm tired of waiting!
    // exception: Job was cancelled

    // Cancellation is always propagated through the coroutines hierarchy:
    suspend fun failedConcurrentSum(): Int = coroutineScope {
        val one: Deferred<Int> = async {
            try {
                delay(1000)
                1
            } finally {
                println("First child was cancelled")
            }
        }
        val two: Deferred<Int> = async {
            println("Second child throws an exception")
            throw ArithmeticException()
        }
        one.await() + two.await()
    }

    runBlocking<Unit> {
        try {
            failedConcurrentSum()
        } catch(e: ArithmeticException) {
            println("Computation failed with ArithmeticException")
        }
    }
    // Second child throws an exception
    // First child was cancelled
    // Computation failed with ConnectException

    // Often you want to cancel due to a timeout.
    runBlocking {
        try {
            withTimeout(1300L) {
                repeat(1000) { i ->
                    println("$i ...")
                    delay(500L)
                }
            }
        } catch (e: TimeoutCancellationException) {
            println(e.message)
        }
    }
    // 0 ...
    // 1 ...
    // 2 ...
    // Timed out waiting for 1300 ms

    // Children of parent coroutine scopes will be cancelled too when parent is cancelled, unless in global scope.
    runBlocking {
        val job = launch {
            GlobalScope.launch {
                println("global scope says, Hi!")
                delay(2000)
                println("global scope says, Got some milk?")
            }
            launch {
                println("child scope says, Hi!")
                delay(3000)
                println("child scope says, Only beer dude.")
            }
        }
        delay(1000)
        job.cancel()
        println("parent scope says, I have cancelled all child scope jobs...")
        delay(2000) // delay a second to see what happens
    }
    // global scope says, Hi!
    // child scope says, Hi!
    // parent scope says, I have cancelled all child scope jobs...
    // global scope says, Got some milk?

}


