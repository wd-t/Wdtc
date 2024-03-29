@file:JvmName("CoroutineUtils")

package org.wdt.wdtc.core.utils

import kotlinx.coroutines.*
import java.time.Instant
import java.util.*
import kotlin.concurrent.timer
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

val ioCoroutineScope = CoroutineScope(Dispatchers.IO)
val defaultCoroutineScope = CoroutineScope(Dispatchers.Default)
fun launchScope(
	name: String = "New coroutine",
	coroutineContext: CoroutineContext = EmptyCoroutineContext,
	block: suspend CoroutineScope.() -> Unit
): Job {
	return defaultCoroutineScope.launch(name.toCoroutineName() + coroutineContext, block = block)
}

fun CoroutineScope.launch(name: String, block: suspend CoroutineScope.() -> Unit): Job =
	launch(context = name.toCoroutineName(), block = block)

fun String.toCoroutineName(): CoroutineName = CoroutineName(this)

@OptIn(DelicateCoroutinesApi::class)
fun executorCoroutineScope(thread: Int = 64, name: String): CoroutineScope {
	return CoroutineScope(newFixedThreadPoolContext(thread, name))
}

inline fun timer(name: String? = null, period: Long, crossinline action: TimerTask.() -> Unit) {
	timer(name, true, Date.from(Instant.now()), period, action)
}