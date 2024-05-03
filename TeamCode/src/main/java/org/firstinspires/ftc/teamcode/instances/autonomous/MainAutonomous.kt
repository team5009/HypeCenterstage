package org.firstinspires.ftc.teamcode.instances.autonomous

import ca.helios5009.hyperion.core.Motors
import ca.helios5009.hyperion.core.Odometry
import ca.helios5009.hyperion.misc.events.Event
import ca.helios5009.hyperion.misc.events.EventListener
import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.OdometryValues
import org.firstinspires.ftc.teamcode.Robot

class MainAutonomous(private val instance: LinearOpMode, listener: EventListener) {
	private val bot = Robot(instance)
	val odometry = Odometry(bot.leftEncoder, bot.rightEncoder, bot.backEncoder)

	val motors = Motors(bot.fl, bot.fr, bot.bl, bot.br)

	init {
		odometry.setConstants(OdometryValues.distanceBack, OdometryValues.distanceLeftRight)
		listener.Subscribe(OutTakePixels(odometry, listener))
		listener.Subscribe(SlowDownMovement(motors))
		listener.Subscribe(NormalizeMovement(motors))

	}

	class OutTakePixels(private val odometry: Odometry, private val listener: EventListener) : Event("YELLOW_PIXEL") {
		override suspend fun run() {
			listener.call("YELLOW_PIXEL_FINISH")
		}
	}

	class SlowDownMovement(private val motors: Motors) : Event("Slow_Down_Movement") {
		override suspend fun run() {
			motors.powerRatio.set(0.5)
		}
	}

	class NormalizeMovement(private val motors: Motors) : Event("Normalize_Movement") {
		override suspend fun run() {
			motors.powerRatio.set(1.0)
		}
	}
}