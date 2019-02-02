package K10coroutines

import kotlinx.coroutines.*

// Coroutine context and dispatchers:
// Coroutines always execute in some context, represented by the value of CoroutineContext type. The coroutine context is
// a set of various elements. The main elements are the Job (which also Deferred<T> extends), and its dispatcher. The
// dispatcher determines what thread or threads the corresponding coroutine uses for its execution. Coroutine dispatcher
// can confine the execution to a specific thread, dispatch it to a thread pool, or let it run unconfined. All coroutine
// builders like launch and async accept an optional CoroutineContext parameter that can be used to explicitly specify
// the dispatcher for new coroutine and other context elements.
//
// Examples of Coroutine contexts:
// Default - used when coroutines are launched in GlobalScope, currently uses a thread pool
// Unconfined - starts the coroutine in the current thread, but will resume on any thread. No thread policy is used.
//
// newSingleThreadContext() builds a dispatcher with a single thread.
// newFixedThreadPoolContext(size: Int) creates a dispatcher with a pool of threads of the given size.

fun main(args: Array<String>) {

//  public fun <T> runBlocking(
//      context: CoroutineContext = EmptyCoroutineContext,
//      block: suspend CoroutineScope.() -> T
//  ): T
    runBlocking { // this: CoroutineScope (EmptyCoroutineContext)

        // When launch is used without parameters, it inherits the context (and thus dispatcher) from the CoroutineScope
        // that it is being launched from.
        launch {
            println("Inherited: ${Thread.currentThread().name}")
        }
        launch(Dispatchers.Default) {
            println("Default: ${Thread.currentThread().name}")
        }
        launch(Dispatchers.Unconfined) {
            println("Unconfined: ${Thread.currentThread().name}")
        }
    }
    // Inherited: main
    // Unconfined: main
    // Default: DefaultDispatcher-worker-1

    val foooDispatcher: ExecutorCoroutineDispatcher = newSingleThreadContext(name = "Foo")
    val barDispatcher: ExecutorCoroutineDispatcher = newSingleThreadContext(name = "Bar")

    runBlocking {
        val job: Job = launch(foooDispatcher) {
            println("I'm working in thread: ${Thread.currentThread().name}") // I'm working in thread: Foo
            launch(barDispatcher) {
                println("I'm working in thread: ${Thread.currentThread().name}") // I'm working in thread: Bar
            }
        }
        job.join()
    }

    // The Unconfined dispatcher resumes coroutines in the thread that was used for the most recent suspension point.
    // Unconfined dispatcher is appropriate when coroutines does not updates any shared data (like UI) that should be
    // confined to a specific thread.

    runBlocking {
        launch(Dispatchers.Unconfined) {
            println("Unconfined before   : ${Thread.currentThread().name}")
            delay(500)
            println("Unconfined after    : ${Thread.currentThread().name}")
        }
        launch {
            println("inherit runBlocking : ${Thread.currentThread().name}")
            delay(1000)
            println("inherit runBlocking : ${Thread.currentThread().name}")
        }
    }
    // Unconfined before   : main
    // inherit runBlocking : main
    // Unconfined after    : kotlinx.coroutines.DefaultExecutor
    // inherit runBlocking : main
}