package ca.helios5009.hyperion.core

import ca.helios5009.hyperion.misc.commands.Bezier
import ca.helios5009.hyperion.misc.commands.EventCall
import ca.helios5009.hyperion.misc.commands.Point
import ca.helios5009.hyperion.misc.events.EventListener
import ca.helios5009.hyperion.misc.generateBezier
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime

class HyperionPath(private val opMode: LinearOpMode, private val listener: EventListener) {
	var odometry: Odometry? = null
	var movement: Movement? = null

	fun start(origin: Point) {
		listener.call(origin.event.message)
		odometry?.setOrigin(origin.x, origin.y, origin.rot)
	}
	fun line(point: Point) {
		movement?.start(mutableListOf(point))
	}

	fun bezier(bezier: Bezier) {
		val generatedBezier = generateBezier(bezier.start, bezier.control[0], bezier.control[1], bezier.end)
		movement?.start(generatedBezier)
	}

	fun continuousLine(points: MutableList<Point>) {
		movement?.start(points)
	}

	fun end(event: EventCall) {
		listener.call(event.message)
		movement!!.stopMovement()
		listener.clearScopes()
	}

	fun wait(time: Long, event: EventCall = EventCall("")) {
		listener.call(event.message)
		val currentPosition = odometry!!.getLocation()
		val timer = ElapsedTime()
		while(opMode.opModeIsActive() && timer.milliseconds() < time) {
			movement?.goToPosition(currentPosition)
		}
	}

	fun wait(message: String, event: EventCall = EventCall("")) {
		listener.call(event.message)
		val currentPosition = odometry!!.getLocation()
		val stayPoint = Point(currentPosition.x, currentPosition.y, currentPosition.rot)

		val timer = ElapsedTime()
		var tempVar = ""
		while(opMode.opModeIsActive() && tempVar != message) {
			if (listener.value.get() == message) {
				tempVar = message
			}
			movement?.goToPosition(stayPoint)
			/*opMode.telemetry.addData("current x", stayPoint.x)
			opMode.telemetry.addData("current y",stayPoint.y)
			opMode.telemetry.addData("current x", odometry?.getLocation()!!.x)
			opMode.telemetry.addData("current y",odometry?.getLocation()!!.y)
			opMode.telemetry.addData("current y",odometry?.getLocation()!!.rot)
			opMode.telemetry.addLine("Waiting...")
			opMode.telemetry.update()*/
		}
	}

}