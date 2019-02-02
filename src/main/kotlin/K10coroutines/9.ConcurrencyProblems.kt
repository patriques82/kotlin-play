package K10coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.*

// Concepts (Good to know):
// Racecondition - Two threads read the same value (before the other thread has updated the value), then both update wrongly.
// Deadlock - Two or more threads holds a lock while they wait infinitely for eachother to complete.
// Livelock - Two or more threads, instead of waiting, gives each other opportunity infinitely to complete.
// Starvation - A low priority thread does not get access to the resources it needs.

suspend fun CoroutineScope.massiveRun(action: suspend () -> Unit) {
    val n = 100  // number of coroutines to launch
    val k = 1000 // times an action is repeated by each coroutine
    val time = measureTimeMillis {
        val jobs = List(n) {
            launch {
                repeat(k) { action() }
            }
        }
        jobs.forEach { it.join() }
    }
    println("Completed ${n * k} actions in $time ms")
}

fun main() {

    // This will not work since all coroutines (on different threads) will increment counter without synchronization
    var counter = 0
    runBlocking<Unit> {
        GlobalScope.massiveRun {
            counter++ // Race condition
        }
        println("Counter = $counter")
    }
    // Completed 100000 actions in 18 ms
    // Counter = 57301

    // We could use AtomicInteger class to which has an atomic incrementAndGet method
    val atomicCounter = AtomicInteger(0)
    runBlocking<Unit> {
        GlobalScope.massiveRun {
            atomicCounter.incrementAndGet()
        }
        println("Counter = $atomicCounter")
    }
    // Completed 100000 actions in 7 ms
    // Counter = 100000

    // This works well but it does not scale to complex state or to complex operations. Mutual exclusion is the usal
    // solution, i.e protect all modifications of the shared state with a critical section that is never executed
    // concurrently. Coroutines alternative to this is called Mutex that has a 'lock' and 'unlock' function to delimit a
    // critical section.
    var count = 0
    val mutex = Mutex()
    runBlocking<Unit> {
        GlobalScope.massiveRun {
            mutex.withLock { // withLock translates to mutex.lock(); try { ... } finally { mutex.unlock() }
                count++
            }
        }
        println("Count = $count")
    }
    // Completed 100000 actions in 155 ms
    // Count = 100000

    // The locking in this example is fine-grained, so it pays the price.

}
