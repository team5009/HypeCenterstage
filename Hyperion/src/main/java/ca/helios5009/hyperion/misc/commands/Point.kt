package ca.helios5009.hyperion.misc.commands

class Point(var x: Double, var y: Double, var rot: Double, val event: EventCall = EventCall("nothing")) {
    var useError = false
    var relative = false

    fun useError() : Point {
        useError = true
        return this
    }

    fun useRelative() : Point {
        relative = true
        return this
    }
}
