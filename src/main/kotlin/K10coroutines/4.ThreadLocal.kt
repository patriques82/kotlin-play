package K10coroutines

import kotlinx.coroutines.*

// Sometime we want to pass a thread local data, but since coroutines are not bound to any thread this is hard.
// ThreadLocal.asContextElement creates an additional context element and keeps track of the value given to it when
// switching threads.

fun main() {
    runBlocking {
        val threadLocal = ThreadLocal<String>()
        val contextElement = threadLocal.asContextElement(value = "launch")
        println("main start: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
        val job = launch(Dispatchers.Default + contextElement) {
            println("launch start: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
            yield()
            println("launch end: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
        }
        job.join()
        println("main end: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
    }
}

// main start: Thread[main,5,main], thread local value: 'null'
// launch start: Thread[DefaultDispatcher-worker-1,5,main], thread local value: 'launch'
// launch end: Thread[DefaultDispatcher-worker-3,5,main], thread local value: 'launch'
// main end: Thread[main,5,main], thread local value: 'null'