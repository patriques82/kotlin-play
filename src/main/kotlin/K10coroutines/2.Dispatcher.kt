package K10coroutines

import kotlinx.coroutines.*

// Coroutine context and dispatchers:
// Coroutines always execute in some context which is represented by the value of CoroutineContext type. Coroutine
// context includes a coroutine dispatcher that determines what thread or threads the corresponding coroutine uses for
// its execution. All coroutine builders like launch and async accept an optional CoroutineContext parameter that can be
// used to explicitly specify the dispatcher for new coroutine and other context elements.
//
// Examples:
// CommonPool executes and resumes the coroutines in a background pool of threads. Its default size equals processors.
// DefaultDispatcher is currently same ans CommonPool
// Unconfined starts the coroutine in the current thread, but will resume on any thread. No thread policy is used.
//
// newSingleThreadContext() builds a dispatcher with a single thread.
// newFixedThreadPoolContext(size: Int) creates a dispatcher with a pool of threads of the given size.

fun main(args: Array<String>) {

//  public fun <T> runBlocking(
//      context: CoroutineContext = EmptyCoroutineContext,
//      block: suspend CoroutineScope.() -> T
//  ): T
    runBlocking {

//      public fun CoroutineScope.launch(
//          context: CoroutineContext = EmptyCoroutineContext,
//          start: CoroutineStart = CoroutineStart.DEFAULT,
//          block: suspend CoroutineScope.() -> Unit
//      ): Job {
        val job: Job = launch {
            printCurrentThread() // The current thread: main
        }
        job.join()
    }

    val helloDispatcher: ExecutorCoroutineDispatcher = newSingleThreadContext(name = "Hello")
    val worldDispatcher: ExecutorCoroutineDispatcher = newSingleThreadContext(name = "World")

    runBlocking {
        val job: Job = launch(helloDispatcher) {
            printCurrentThread() // The current thread: Hello
            launch(worldDispatcher) {
                printCurrentThread() // The current thread: World
            }
        }
        job.join()
    }
}

fun printCurrentThread() {
    println("The current thread: ${Thread.currentThread().name}")
}
