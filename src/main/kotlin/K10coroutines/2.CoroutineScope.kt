package K10coroutines

import kotlinx.coroutines.*

// Coroutine scope and structured concurrency:
// Threads are always global but with coroutines the scope can be defined, enabling structured concurrency. All coroutine
// builders adds an instance of CoroutineScope to the scope of the block passed in. A coroutine scope does not terminate
// until all its children terminate but does not block the main thread like runBlocking does
//
// fun runBlocking(..., block: suspend CoroutineScope.() -> T): T
// fun CoroutineScope.launch(..., block: suspend CoroutineScope.() -> Unit): Job
// fun CoroutineScope.async(..., block: suspend CoroutineScope.() -> T): Deferred<T>
//
// As we can see only runBlocking can be called outside a CoroutineScope, which enables our code to enter the suspending
// world.

fun main(args: Array<String>) {

    // Launches coroutine in global scope like with threads.
    val great = GlobalScope.launch {
        delay(2000L)
        println("great!")
    } // main thread continues immediately

    runBlocking { // this: CoroutineScope
        val are = GlobalScope.launch { // Coroutine builder launches coroutine in global scope (not in outer scope).
            delay(1000L)
            println("are")
        }
        println("coroutines") // main thread continues here immediately
        // We have to wait until coroutines in global scope completes, otherwise the main thread continues directly and
        // and next runBlocking block continues, so that results get mixed up. A good rule of thumb is to always join or
        // cancel on jobs from launched in global scope.
        are.join()
        great.join()
    }
    // coroutines
    // 'waiting approx 1 second...'
    // are
    // 'waiting approx 1 second...'
    // great!

    // Instead of explicitly joining like we did, we can structure our concurrent code by using the same coroutine scope.
    runBlocking {
        launch { // launch new coroutine in outer scope
            delay(1000L)
            println("World!")
        }
        println("Hello,")
    }
    // Hello,
    // 'wait 1 second...'
    // World!

    // We can also create new coroutine scopes inside a coroutine scope, which does not complete until children complete.
    runBlocking {
        launch {
            delay(2000L)
            println("2")
        }

        coroutineScope { // Creates a new scope that works like runBlocking inside a coroutine scope
            launch {
                delay(3000L)
                println("3")
            }
            delay(1000L)
            println("1")
        }
        println("4") // This does not continue until the nested coroutine scope has finished.
    }
    // 1
    // 2
    // 3
    // 4

    // Parent coroutines always waits for the completion of all its children, and does not have to join them explicitly.
    runBlocking {
        val request = launch {
            repeat(3) { i ->
                launch {
                    delay(600 - i * 200L) // variable delay 600ms, 400ms, 200ms
                    println("Coroutine $i is done")
                }
            }
        }
        println("start")
        request.join() // wait for completion of the request, including all its children
        println("end")
    }
    // start
    // Coroutine 2 is done
    // Coroutine 1 is done
    // Coroutine 0 is done
    // end

}