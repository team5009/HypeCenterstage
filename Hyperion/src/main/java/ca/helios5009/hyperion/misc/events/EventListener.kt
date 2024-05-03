package ca.helios5009.hyperion.misc.events

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicReference

class EventListener() {
	var value = AtomicReference("")
	private val triggerFunctions = mutableListOf<Event>()
	private val scopes = mutableListOf<Job>()

	fun Subscribe(function: Event) {
		triggerFunctions.add(function)
	}

	fun call(newValue: String) {
		if (newValue.lowercase() == "nothing" || newValue == "") {
			return
		}
		value.set(newValue.lowercase())
		triggerFunctions.forEach {
			if (it.event.lowercase() == newValue) {
				scopes.add(
					CoroutineScope(Dispatchers.Default).launch {
						it.run()
						return@launch
					}
				)
			}
		}
	}

	fun clearScopes() {
		scopes.forEach {
			it.cancel()
		}
		scopes.clear()
	}

}
