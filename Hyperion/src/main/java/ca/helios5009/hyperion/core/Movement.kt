package ca.helios5009.hyperion.core

import ca.helios5009.hyperion.misc.euclideanDistance
import ca.helios5009.hyperion.misc.commands.Point
import ca.helios5009.hyperion.misc.events.EventListener
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.Telemetry
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class Movement(private val listener: EventListener, private val bot: Motors, private val odometry: Odometry, private val opMode: LinearOpMode) {

	private lateinit var driveController : ProportionalController
	private lateinit var strafeController : ProportionalController
	private lateinit var rotateController : ProportionalController
	var target: Point = Point(0.0, 0.0, 0.0)

	fun start(points: MutableList<Point>) {
		//opMode.telemetry.addLine("start")
		//opMode.telemetry.update()
		target = points.removeLast()
		for (i in 0 until points.size) {
			var point = points[i]
			if (point.event.message.startsWith("constant_move")) {
				val cmd = point.event.message.split("|")
				val angle = cmd[1].toDouble()
				val message = cmd[2]
				listener.call(message)
				while (opMode.opModeIsActive() && odometry.calculate() && abs(angle - odometry.getRotDegrees()) > 3.0) {
					bot.move(point.x, point.y, point.rot)
				}
				bot.stop()
			} else {
				listener.call(point.event.message)
				if (point.relative) {
					odometry.calculate()
					val newX = odometry.getLocation().x + point.x
					val newY = odometry.getLocation().y + point.y
					val newRot = odometry.getRotDegrees() + point.rot
					point = Point(newX, newY, newRot)
				}
				val isBigger =
					euclideanDistance(odometry.getLocation(), target) < euclideanDistance(
						point,
						target
					)
				goToPosition(point, isBigger)
				resetController()
			}
		}
		listener.call(target.event.message)
		goToEndPoint()
		bot.stop()
	}
	fun setControllerConstants(drive: DoubleArray, strafe: DoubleArray, rotate: DoubleArray) {
		driveController = ProportionalController(drive[0], drive[1], drive[2], drive[3], drive[4])
		strafeController = ProportionalController(strafe[0], strafe[1], strafe[2], strafe[3], strafe[4])
		rotateController = ProportionalController(rotate[0], rotate[1], rotate[2], rotate[3], rotate[4], true)
	}

	fun goToPosition(nextPoint: Point, isBigger: Boolean? = null) {
		//opMode.telemetry.addLine("gotoPosition")
		//opMode.telemetry.update()
		while(opMode.opModeIsActive() && odometry.calculate() && (isBigger == null || isBigger == euclideanDistance(odometry.getLocation(), target) < euclideanDistance(nextPoint, target))) {

			val magnitude = euclideanDistance(nextPoint, odometry.getLocation())
			if (abs(magnitude) < 1.0) {
				println("magnitude broke loop")
				break
			}

			var speedFactor = euclideanDistance(odometry.getLocation(), target)
			if (nextPoint.useError) {
				speedFactor = euclideanDistance(odometry.getLocation(), nextPoint) * 2.0
			}

			val deltaX = (nextPoint.x - odometry.getLocation().x) / magnitude * speedFactor
			val deltaY = (nextPoint.y - odometry.getLocation().y) / magnitude * speedFactor
			val deltaRot = (nextPoint.rot - odometry.getLocation().rot * 180 / PI)

			val theta = odometry.getLocation().rot
			val dx = deltaX * cos(theta) - deltaY * sin(theta)
			val dy = deltaX * sin(theta) + deltaY * cos(theta)

			val drive = driveController.getOutput(dx)
			val strafe = strafeController.getOutput(dy)
			val rotate = rotateController.getOutput(deltaRot)
			val diffDistance = atan2(dy, dx)

//			if (t != null) {
////				t.addData("drivePosition", driveController.inPosition)
////				t.addData("strafePosition", strafeController.inPosition)
////				t.addData("turnPosition", rotateController.inPosition)
////				t.addLine("-----------------------------------------------")
////				t.addData("Next", "${nextPoint.x}, ${nextPoint.y}")
////				t.addData("Target", "${target.x}, ${target.y}")
			opMode.telemetry.addData("Current", "${odometry.getLocation().x}, ${odometry.getLocation().y}")
////				t.addData("Bigger?", isBigger)
////				t.addData("Euclidean Difference", euclideanDistance(odometry.location, target) < euclideanDistance(nextPoint, target))
////				t.addData("magnitude", magnitude)
////				t.addData("current step", currentStep)
			opMode.telemetry.addData("drive power", drive)
			opMode.telemetry.addData("strafe power", strafe)
			opMode.telemetry.addData("turn", rotate)
			opMode.telemetry.addData("turn power cos",  odometry.getLocation().rot)
////				t.addData("Current X", odometry.location.x)
////				t.addData("Current Y", odometry.location.y)
			opMode.telemetry.addData("Next X", nextPoint.x)
			opMode.telemetry.addData("Next Y", nextPoint.y)
////				t.addData("Magnitude", magnitude)
////				t.addData("Speed Factor", speedFactor)
			opMode.telemetry.addData("X Error", dx)
			opMode.telemetry.addData("Y Error", dy)
////				t.addData("Distance X", deltaX)
////				t.addData("Distance Y", deltaY)
			opMode.telemetry.addData("Rot Error", deltaRot)
			opMode.telemetry.update()
//			}

			bot.move(drive, strafe, rotate)
//			bot.move(diffDistance, drive, strafe, rotate * 0.2)
			if (isBigger == null) {
				break
			}
		}
	}

	private fun goToEndPoint() {
		val timer = ElapsedTime()
		val timeout = ElapsedTime()
		resetController()
		while (opMode.opModeIsActive()) {
			goToPosition(target)

			if (driveController.inPosition && strafeController.inPosition && rotateController.inPosition) {
				if (timer.seconds() > 0.15) {
					break
				}
			} else if (timeout.milliseconds() > 1000.0) {
				break
			} else {
				timer.reset()
			}
		}
	}

	fun stopMovement() {
		bot.stop()
	}

	private fun resetController() {
		driveController.reset()
		strafeController.reset()
		rotateController.reset()
	}
}