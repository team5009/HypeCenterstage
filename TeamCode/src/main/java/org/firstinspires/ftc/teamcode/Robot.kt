package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.hardware.bosch.BHI260IMU
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.IMU
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.robotcore.external.navigation.Orientation

class Robot(private val instance : LinearOpMode) {
	val fl: DcMotorEx = instance.hardwareMap.get(DcMotorEx::class.java, "FL")
	val fr: DcMotorEx = instance.hardwareMap.get(DcMotorEx::class.java, "FR")
	val br: DcMotorEx = instance.hardwareMap.get(DcMotorEx::class.java, "BR")
	val bl: DcMotorEx = instance.hardwareMap.get(DcMotorEx::class.java, "BL")

	val rightEncoder: DcMotorEx = instance.hardwareMap.get(DcMotorEx::class.java, "LIFT")
	val leftEncoder: DcMotorEx = instance.hardwareMap.get(DcMotorEx::class.java, "BL")
	val backEncoder: DcMotorEx = instance.hardwareMap.get(DcMotorEx::class.java, "FL")

	val lift: DcMotorEx = instance.hardwareMap.get(DcMotorEx::class.java, "LIFT")
	val arm: DcMotorEx = instance.hardwareMap.get(DcMotorEx::class.java, "ARM")
	val bottom: DcMotorEx = instance.hardwareMap.get(DcMotorEx::class.java, "UPPERINTAKE") //Bottom intake motor
	val top: DcMotorEx = instance.hardwareMap.get(DcMotorEx::class.java, "LOWERINTAKE") //top intake motor
	val flap: Servo = instance.hardwareMap.get(Servo::class.java,"flap")
	val plane : Servo = instance.hardwareMap.get(Servo::class.java,"plane")
	val flapper : Servo = instance.hardwareMap.get(Servo::class.java, "yellow_arm")
	val topstack : Servo = instance.hardwareMap.get(Servo::class.java, "topstack")

	val cam: Camera = Camera(1)
	val imu: BHI260IMU = instance.hardwareMap.get(BHI260IMU::class.java, "imu")


	init {
		fl.direction = DcMotorSimple.Direction.REVERSE
		fr.direction = DcMotorSimple.Direction.FORWARD
		bl.direction = DcMotorSimple.Direction.REVERSE
		br.direction = DcMotorSimple.Direction.FORWARD

		leftEncoder.direction = DcMotorSimple.Direction.REVERSE
		rightEncoder.direction = DcMotorSimple.Direction.FORWARD
		backEncoder.direction = DcMotorSimple.Direction.FORWARD

		lift.direction = DcMotorSimple.Direction.FORWARD
		arm.direction = DcMotorSimple.Direction.FORWARD
		bottom.direction = DcMotorSimple.Direction.FORWARD
		top.direction = DcMotorSimple.Direction.REVERSE

		leftEncoder.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
		rightEncoder.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
		backEncoder.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

		bottom.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
		bottom.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
		top.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
		top.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
		lift.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
		lift.mode = DcMotor.RunMode.RUN_USING_ENCODER
		arm.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
		arm.mode = DcMotor.RunMode.RUN_USING_ENCODER

		fl.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
		fr.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
		bl.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
		br.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

		fl.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
		fr.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
		bl.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
		br.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

		lift.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
		arm.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
		bottom.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
		top.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT

		val imuParameters : IMU.Parameters = IMU.Parameters(
			RevHubOrientationOnRobot(
				Orientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES, 0.0f, -45.0f, 0.0f, 0)
			)
		)

		imu.initialize(imuParameters)
		imu.resetYaw()

		plane.position = 1.0
		flap.position = 0.0
		flapper.position = 0.0
		topstack.position = 0.6
	}
	fun tics_per_inch(inches: Double): Double {
		return 384.5 / 4 / Math.PI * inches
	}

	fun tics_per_lift(inches: Double) :Double {
		return 1120 / 1.96 / Math.PI * inches
	}
	fun move(flPower: Double, frPower: Double, blPower: Double, brPower: Double) {
		fl.power = -flPower
		fr.power = frPower
		bl.power = blPower
		br.power = brPower
	}

}
@Config
object OdometryValues {
	@JvmField var distanceBack = 6.25;
	@JvmField var distanceLeftRight = 11.5652;
}


@Config
object DriveConstants {
	@JvmField var GainSpeed = 0.075 //0.15 og
	@JvmField var AccelerationLimit = 1.0
	@JvmField var DefaultOutputLimit = 0.8
	@JvmField var Tolerance = 3.0
	@JvmField var Deadband = 1.5
}

@Config
object StrafeConstants {
	@JvmField var GainSpeed = 0.0825 //1.0 og
	@JvmField var AccelerationLimit = 1.5
	@JvmField var DefaultOutputLimit = 1.0
	@JvmField var Tolerance = 3.0
	@JvmField var Deadband = 1.5
}

@Config
object RotateConstants {
	@JvmField var GainSpeed = 0.05
	@JvmField var AccelerationLimit = 1.0
	@JvmField var DefaultOutputLimit = 1.0
	@JvmField var Tolerance = 5.0
	@JvmField var Deadband = 3.0
}
