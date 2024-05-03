package org.firstinspires.ftc.teamcode.autonomous

import android.health.connect.datatypes.SleepSessionRecord
import ca.helios5009.hyperion.misc.Camera
import ca.helios5009.hyperion.misc.Position
import ca.helios5009.hyperion.core.HyperionPath
import ca.helios5009.hyperion.core.Motors
import ca.helios5009.hyperion.core.Movement
import ca.helios5009.hyperion.core.Odometry
import ca.helios5009.hyperion.misc.Alliance
import ca.helios5009.hyperion.misc.events.Event
import ca.helios5009.hyperion.misc.events.EventListener
import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import com.sun.tools.javac.Main
import kotlinx.coroutines.delay
import org.firstinspires.ftc.teamcode.DriveConstants
import org.firstinspires.ftc.teamcode.OdometryValues
import org.firstinspires.ftc.teamcode.Robot
import org.firstinspires.ftc.teamcode.RotateConstants
import org.firstinspires.ftc.teamcode.StrafeConstants
import org.firstinspires.ftc.teamcode.misc.MenuPathSelector
import org.firstinspires.ftc.teamcode.misc.CYCLE
import org.firstinspires.ftc.teamcode.misc.PATH
import org.firstinspires.ftc.teamcode.misc.SIDE
import org.firstinspires.ftc.teamcode.processors.ColorProcessor
import kotlin.io.path.Path

@Autonomous(name = "MainA", group = "Production")
class MainAuto: LinearOpMode() {
	public var pos = 0
	private val listener = EventListener()

	override fun runOpMode() {

		//val autonPaths = AutonPaths()
		val bot = Robot(this)
//		val instance = MainAutonomous(this, listener)
		val dash = FtcDashboard.getInstance()
		telemetry = MultipleTelemetry(telemetry, dash.telemetry)
		val menu = MenuPathSelector()
		val path = HyperionPath(this, listener)
		val motors = Motors(bot.fl, bot.fr, bot.bl, bot.br)
		path.odometry = Odometry(bot.leftEncoder, bot.rightEncoder, bot.backEncoder)
		path.odometry?.setConstants(OdometryValues.distanceBack, OdometryValues.distanceLeftRight)
		path.movement = Movement(listener, motors, path.odometry!!, this)
		path.movement?.setControllerConstants(
			doubleArrayOf(DriveConstants.GainSpeed, DriveConstants.AccelerationLimit, DriveConstants.DefaultOutputLimit, DriveConstants.Tolerance, DriveConstants.Deadband),
			doubleArrayOf(StrafeConstants.GainSpeed, StrafeConstants.AccelerationLimit, StrafeConstants.DefaultOutputLimit, StrafeConstants.Tolerance, StrafeConstants.Deadband),
			doubleArrayOf(RotateConstants.GainSpeed, RotateConstants.AccelerationLimit, RotateConstants.DefaultOutputLimit, RotateConstants.Tolerance, RotateConstants.Deadband)
		)
		listener.Subscribe(SlowDownMovement(motors))
		listener.Subscribe(NormalizeMovement(motors))
		listener.Subscribe(SpeedMovement(motors))
		listener.Subscribe(MaxMovement(motors))
		listener.Subscribe(ScoreYellowPixel(bot, listener, this))
		listener.Subscribe(BringBackFlapper(bot, listener, this))
		listener.Subscribe(TopStack(bot))

		val camProcessor = ColorProcessor(Alliance.BLUE, false)
		val cam = Camera(hardwareMap, false).addProcessor(camProcessor).build()
		val positionChangeTimer = ElapsedTime()

		var path1 = PATH.MAIN
		var path2 = CYCLE.STAGEDOOR
		var position = Position.NONE
		telemetry = MultipleTelemetry(telemetry, dash.telemetry)
//		val pathExecutor = CommandExecute(this, listener)
//		pathExecutor.motors = instance.motors
//		pathExecutor.odometry = instance.odometry
//		val movementClass = Movement(listener, pathExecutor.motors!!, pathExecutor.odometry!!, this)
//		movementClass.setControllerConstants(
//			doubleArrayOf(DriveConstants.GainSpeed, DriveConstants.AccelerationLimit, DriveConstants.DefaultOutputLimit, DriveConstants.Tolerance, DriveConstants.Deadband),
//			doubleArrayOf(StrafeConstants.GainSpeed, StrafeConstants.AccelerationLimit, StrafeConstants.DefaultOutputLimit, StrafeConstants.Tolerance, StrafeConstants.Deadband),
//			doubleArrayOf(RotateConstants.GainSpeed, RotateConstants.AccelerationLimit, RotateConstants.DefaultOutputLimit, RotateConstants.Tolerance, RotateConstants.Deadband)
//		)
//		pathExecutor.movement = movementClass
		var cameraSetup = false
		var selection : StartPosition = BlueLeft(bot, this, path, listener)

		while (opModeInInit()) {
			menu.run(this)
			if (menu.ready && !cameraSetup) {
				camProcessor.alliance = menu.allianceOption
				cameraSetup = true
				path1 = menu.travelOption
				path2 = menu.cycleOption
				if (menu.allianceOption == Alliance.RED && menu.sideOption == SIDE.LEFT) {
					selection = RedLeft(bot, this, path, listener)
				} else if (menu.allianceOption == Alliance.RED && menu.sideOption == SIDE.RIGHT) {
					selection = RedRight(bot, this, path, listener)
				} else if (menu.allianceOption == Alliance.BLUE && menu.sideOption == SIDE.LEFT) {
					selection = BlueLeft(bot, this, path, listener)
				} else {
					selection = BlueRight(bot, this, path, listener)
				}
			} else if (!menu.ready && cameraSetup) {
				cameraSetup = false
			}


			if (cameraSetup) {
				if (camProcessor.position != position) {
					if (positionChangeTimer.milliseconds() > 1000.0) {
						position = camProcessor.position
					}
				} else {
					positionChangeTimer.reset()
				}
				path.odometry?.calculate()
				telemetry.addData("Alliance", menu.allianceOption)
				telemetry.addData("Side", menu.sideOption)
				telemetry.addData("Path", menu.travelOption)
				telemetry.addData("Cycle", menu.cycleOption)
				telemetry.addData("Cycle amount", menu.cycleamount)
				telemetry.addData("wait time", menu.waittime)
				telemetry.addData("Selected code", selection)
				telemetry.addData("Position", camProcessor.position)
				telemetry.addData("pos x", path.odometry?.getLocation()!!.x)
				telemetry.addData("pos y", path.odometry?.getLocation()!!.y)
				telemetry.addData("pos rot", path.odometry?.getLocation()!!.rot)
			}
			telemetry.update()
//			val storedPaths = autonPaths.getAllPaths()
		}

		waitForStart()
		var w = menu.waittime
		if (opModeIsActive()) {

			//cam.stopStreaming()
 			if(path1 == PATH.MAIN) {
				 selection.mainPath(position, w)
			} else {
				selection.subPath(position, w)
			}

			if(path2 == CYCLE.STAGEDOOR) {
				selection.stageDoorPath(menu.cycleamount)
			} else {
				selection.trussPath(menu.cycleamount)
			}
		// 		pathExecutor.readPath(autonPaths.getAllPaths()[seletectedPath])
//			pathExecutor.execute()
			//cam.close()
		}
	}
	class ScoreYellowPixel(private val bot: Robot, private val listener: EventListener, val ma : MainAuto): Event("Yellow_outtake") {

		override suspend fun run() {

			if (ma.pos == 0) {
				bot.flapper.position = 0.92
				delay(1000)
				ma.pos = 1
			} else if (ma.pos == 1) {
				bot.flapper.position = 0.97
				delay(600)
				listener.call("yellow_outtake_finish")
				ma.pos = 2
			} else {
				bot.flapper.position = 0.0
				delay(1000)
				ma.pos = 0
			}
		}
	}

	class BringBackFlapper(private val bot: Robot, private val listener: EventListener, val ma: MainAuto): Event("bring_back_flapper") {
		override suspend fun run() {
			if (ma.pos != 0) {
				bot.flapper.position = 0.0
				delay(1000)
				ma.pos = 0
			}
		}
	}
	class TopStack(private val bot: Robot) : Event("top_stack"){
		var open = false
		override suspend fun run() {
			if(open) {
				bot.topstack.position = 0.5
				open = false
			} else {
				bot.topstack.position = 0.0
				open = true
			}
			delay(450)
		}
	}


	class SlowDownMovement(private val motors: Motors) : Event("Slow_Down_Movement") {
		override suspend fun run() {
			motors.powerRatio.set(0.4)
		}
	}

	class NormalizeMovement(private val motors: Motors) : Event("Normalize_Movement") {
		override suspend fun run() {
			motors.powerRatio.set(0.6)
		}
	}

	class SpeedMovement(private val motors: Motors) : Event("Speed_up_movement") {
		override suspend fun run() {
			motors.powerRatio.set(0.8)
		}
	}

	class MaxMovement(private  val motors: Motors) : Event("max_out_movement") {
		override suspend fun run() {
			motors.powerRatio.set(1.0)
		}
	}

	class Sleep(private val opMode: LinearOpMode, private val wait : Long) : Event("sleep") {
		override suspend fun run() {
			opMode.sleep(wait)
		}
	}
}

