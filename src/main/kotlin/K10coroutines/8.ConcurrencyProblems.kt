package K10coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.*

// Typical concurrency problems:
// Racecondition - Two threads read the same value (before the other thread has updated the value), then both update wrongly.
// Deadlock - Two or more threads holds a lock while they wait infinitely for eachother to complete.
// Livelock - Two or more threads, instead of waiting, gives each other opportunity infinitely to complete.
// Starvation - A low priority thread does not get access to the resources it needs.

suspend fun massiveRun(action: suspend () -> Unit) {
    val n = 100  // number of coroutines to launch
    val k = 1000 // times an action is repeated by each coroutine
    val time = measureTimeMillis {
        val jobs = List(n) {
            GlobalScope.launch {
                repeat(k) { action() }
            }
        }
        jobs.forEach { it.join() }
    }
    println("Completed ${n * k} actions in $time ms")
}

// Why dont we write massiveRun like a extension function. And use the structured concurrency that gives us?
fun CoroutineScope.altMassiveRun(action: suspend () -> Unit) {
    val n = 100  // number of coroutines to launch
    val k = 1000 // times an action is repeated by each coroutine
    val time = measureTimeMillis {
        repeat(n) {
            launch {
                repeat(k) { action() }
            }
        }
    }
    println("Completed ${n * k} actions in $time ms")
}
// The problem is that altMassiveRun cannot wait for all coroutines to finish and in our code we want to control the
// coroutines lifecycle, since that is what we want to measure. The convention is:
//
// suspending function - when the function takes long time to process (it is suspending)
// extension function  - when the function launches coroutines and does not wait for them.

fun main() {

    // This will not work since all coroutines (on different threads) will increment counter without synchronization
    var counter = 0
    runBlocking<Unit> {
        massiveRun {
            counter++ // Race condition
        }
        println("Counter = $counter")
    }
    // Completed 100000 actions in 18 ms
    // Counter = 57301

    // We could use an AtomicInteger which has an atomic incrementAndGet method
    val atomicCounter = AtomicInteger(0)
    runBlocking<Unit> {
        massiveRun {
            atomicCounter.incrementAndGet()
        }
        println("Counter = $atomicCounter")
    }
    // Completed 100000 actions in 7 ms
    // Counter = 100000

    // This works well but it does not scale to complex state or complex operations. Mutual exclusion is the usal
    // solution, i.e protect all modifications of the shared state with a critical section that is never executed
    // concurrently. Coroutines alternative to this is called Mutex that has a 'lock' and 'unlock' function to delimit a
    // critical section.
    var count = 0
    val mutex = Mutex()
    runBlocking<Unit> {
        massiveRun {
            mutex.withLock { // withLock translates to mutex.lock(); try { ... } finally { mutex.unlock() }
                count++
            }
        }
        println("Count = $count")
    }
    // Completed 100000 actions in 155 ms
    // Count = 100000

    // The locking in this example is fine-grained, so it requires longer execution time compared to AtomicInteger.

}
