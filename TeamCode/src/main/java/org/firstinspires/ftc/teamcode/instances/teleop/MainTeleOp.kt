package org.firstinspires.ftc.teamcode.instances.teleop

import android.os.SystemClock
import ca.helios5009.hyperion.core.Odometry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry
import org.firstinspires.ftc.teamcode.OdometryValues
import org.firstinspires.ftc.teamcode.Robot
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin

class MainTeleOp(private val instance : LinearOpMode) {
	val bot = Robot(instance)
	val odometry = Odometry(bot.leftEncoder, bot.rightEncoder, bot.backEncoder)
	var intakeIsPressed: Boolean = false
	var upintakeIsPressed: Boolean = false
	var triggerIsPressed: Boolean = false
	var lefttriggerIsPressed: Boolean = false
	var isflapper = false

	init {
		odometry.setConstants(
			OdometryValues.distanceBack, OdometryValues.distanceLeftRight
		)
		if (bot.flapper.position > 0.3) {
			isflapper = true
		}
	}

	fun gamepadOne(gamepad: Gamepad) {
		theal(gamepad)
		if (!isflapper && gamepad.left_trigger > 0.1) {
			bot.flapper.position = 0.95
			isflapper = true
		} else if (isflapper && gamepad.left_trigger < 0.1) {
			isflapper = false
			bot.flapper.position = 0.0
		}
		planelaunch()
	}

	fun gamepadTwo(gamepad: Gamepad) {
		if (gamepad.ps) {
			bot.arm.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
			bot.arm.mode = DcMotor.RunMode.RUN_USING_ENCODER
		}
		//intake
		if (gamepad.dpad_down && !this.intakeIsPressed) {
			this.intakeIsPressed = true
			intake(-0.7) // intake pixel
			//telemetry.addLine("ABSORBING :O")
		} else if (!gamepad.dpad_down && this.intakeIsPressed) {
			this.intakeIsPressed = false
		}
		if (gamepad.dpad_up && !this.upintakeIsPressed) {
			this.upintakeIsPressed = true

			intake(0.7) // spit out pixel
			//telemetry.addLine("Ew no xp")
		} else if (!gamepad.dpad_up && this.upintakeIsPressed) {
			this.upintakeIsPressed = false
		}
		//arm
		if (gamepad.right_trigger > 0.1 && !triggerIsPressed) {
			pw = 0.5
			triggerIsPressed = true
		} else if (gamepad.right_trigger > 0.1 && triggerIsPressed) {
			armmoving()
		} else if (gamepad.right_trigger < 0.1 && triggerIsPressed) {
			triggerIsPressed = false
		}

		if (gamepad.left_trigger > 0.1 && !lefttriggerIsPressed) {
			bpw = -0.5
			lefttriggerIsPressed = true
		} else if (gamepad.left_trigger > 0.1 && lefttriggerIsPressed) {
			armbackwards()
		} else if (gamepad.left_trigger < 0.1 && lefttriggerIsPressed) {
			lefttriggerIsPressed = false
		}

		if (!lefttriggerIsPressed && !triggerIsPressed) {
			bot.arm.power = 0.0
		}
		//Lifting
		if (gamepad.x) {
			bot.lift.power = -1.0
			//telemetry.addLine("Arm extending ;)")
		} else if (gamepad.b) {
			bot.lift.power = 1.0
			//telemetry.addLine("Arm Retracting XD")
		} else {
			bot.lift.power = 0.0
		}
		//Dropping
		SCLC()
	}

	fun tili(gamepad: Gamepad) {
		val forward = -gamepad.left_stick_y.toDouble().pow(3) // * -sign(gamepad.left_stick_y.toDouble())
		val turn = (gamepad.right_stick_x.toDouble() * 0.8).pow(3) //* sign(gamepad.right_stick_x.toDouble())
		val strafe = (gamepad.left_stick_x.toDouble() * 1.1).pow(3) //* sign(gamepad.left_stick_x.toDouble())

		val totalPower = abs(forward) + abs(turn) + abs(strafe)

		val denominator = max(0.8, totalPower)

		bot.fl.power = (forward + strafe + turn) / denominator
		bot.fr.power = (forward - strafe - turn) / denominator
		bot.bl.power = (forward - strafe + turn) / denominator
		bot.br.power = (forward + strafe - turn) / denominator
	}

	fun theal(gamepad: Gamepad) {
		var drive = -gamepad.left_stick_y.toDouble().pow(3) // * -sign(gamepad.left_stick_y.toDouble())
		var turn = (gamepad.right_stick_x.toDouble() * 0.8).pow(3) //* sign(gamepad.right_stick_x.toDouble())
		var strafe = (gamepad.left_stick_x.toDouble() * 1.1).pow(3)

		if (gamepad.dpad_right) {
			strafe = -0.4
		}
		if (gamepad.dpad_left) {
			strafe = 0.4
		}
		if (gamepad.dpad_up) {
			drive = 0.4
		}
		if (gamepad.dpad_down) {
			drive = -0.4
		}
		if (gamepad.right_bumper) {
			turn = -0.4
		}
		if (gamepad.left_bumper) {
			turn = 0.4
		}
		val theta = atan2(drive, strafe)
		val pow = hypot(strafe, drive)
		val sin = sin(theta - Math.PI / 4)
		val cos = cos(theta - Math.PI / 4)
		val max = max(abs(sin), abs(cos))
		var fl = pow * cos / max + turn
		var fr = pow * sin / max - turn
		var bl = pow * sin / max + turn
		var br = pow * cos / max - turn

		if (pow + abs(turn) > 1.0) {
			fl /= pow + turn
			fr /= pow - turn
			bl /= pow + turn
			br /= pow - turn
		}

		if (abs(drive) > 0.1 || abs(strafe) > 0.1 || abs(turn) > 0.1) {
			bot.move(

				fl,
				fr,
				bl,
				br
			)
		} else {
			bot.move(0.0, 0.0, 0.0, 0.0)
		}
	}

	var bool = false
	var startime : Long = 0L
	var endtime : Long = 0L
	fun SCLC() {
		if (!this.bool && instance.gamepad2.a) {
			this.startime = SystemClock.uptimeMillis() + 270
			this.endtime = this.startime + 150
			this.bool = true
		} else if (SystemClock.uptimeMillis() >= this.endtime && (this.bool && !instance.gamepad2.a)) {
			bot.flap.position = 0.0
			this.startime = 0L
			this.endtime = 0L
			this.bool = false
		} else if ((this.startime <= SystemClock.uptimeMillis()) && this.bool) {
			//instance.telemetry.addLine("Opening")
			bot.flap.position = 0.5
		}

		/*if (this.endtime < SystemClock.uptimeMillis() && instance.gamepad2.a) {
			//instance.telemetry.addData("time held", SystemClock.uptimeMillis() - this.startime)
		} else if (instance.gamepad1.a) {
			//instance.telemetry.addData("End Time", this.endtime.toDouble() / 1000)
			//instance.telemetry.addData("Start Time", this.startime.toDouble() / 1000)
		}*/
	}

	var isintake = false
	fun intake(power: Double) {
		if (bot.bottom.power != 0.0) {
			bot.top.power = 0.0 //power off
			bot.bottom.power = 0.0
			bot.flap.position = 0.0 //flap closed
			bot.flap.position = 0.0
		} else if (bot.top.power == 0.0) {
			bot.flap.position = 0.1 //flap open
			bot.top.power = power //power on
			bot.bottom.power = power
			bot.flap.position = 0.5
		}
	}
	public var bpw = 0.0
	fun armbackwards() {
		//val limiter : Double = 80.0
		val maxvelocity = 0.4
		val minvelocity = 0.3
		// if (bot.arm.currentPosition < limiter) {
		if  (bot.arm.power < minvelocity) {
			this.pw += 0.05
		} else if (bot.arm.power > maxvelocity) {
			this.pw -= 0.05
		}
		/* } else {
                 this.pw = 0.0
             }*/
		bot.arm.power = this.pw
	}

	public var pw = 0.0
	fun armmoving() {
		//val limiter : Double = 10.0
		val minvelocity = -0.4
		val maxvelocity = -0.3
		//if (bot.arm.currentPosition > limiter) {
		if (bot.arm.power < minvelocity) {
			this.bpw += 0.05
		} else if (bot.arm.power > maxvelocity) {
			this.bpw -= 0.05
		}
		/*} else {
            this.bpw = 0.0
        }*/
		bot.arm.power = this.bpw
	}

	var refulingtime = 0L
	fun planelaunch() {
		if (instance.gamepad1.right_trigger > 0.5) {
			bot.plane.position = 0.0
			this.refulingtime = SystemClock.uptimeMillis() + 1000
		}

		if (SystemClock.uptimeMillis() >= this.refulingtime) {
			bot.plane.position = 0.5
			this.refulingtime = 0L
		}
	}

	fun square(number: Double) : Double {
		return number * abs(number)
	}

}

