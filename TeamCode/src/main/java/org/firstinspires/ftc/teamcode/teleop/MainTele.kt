package org.firstinspires.ftc.teamcode.teleop

import ca.helios5009.hyperion.core.Odometry
import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.firstinspires.ftc.teamcode.instances.teleop.MainTeleOp

@TeleOp(name = "Main", group = "Production")
class MainTele: LinearOpMode(){

	override fun runOpMode() {
		val instance = MainTeleOp(this)
		val dashboard = FtcDashboard.getInstance()
		val odometry = Odometry(instance.bot.leftEncoder, instance.bot.rightEncoder, instance.bot.backEncoder)
		telemetry = MultipleTelemetry(telemetry, dashboard.telemetry)

		waitForStart()

		val gamepadOneCoroutine = CoroutineScope(Dispatchers.Default).launch {
			while(opModeIsActive()) {
				instance.gamepadOne(gamepad1)
			}
		}

		val gamepadTwoCoroutine = CoroutineScope(Dispatchers.Default).launch {
			while(opModeIsActive()) {
				instance.gamepadTwo(gamepad2)
			}
		}

		while(opModeIsActive()) {
			odometry.calculate()
			telemetry.addData("X", odometry.location.x)
			telemetry.addData("Y", odometry.location.y)
			telemetry.addData("Heading", odometry.getRotDegrees())
			telemetry.update()
		}

		gamepadOneCoroutine.cancel()
		gamepadTwoCoroutine.cancel()
	}

}