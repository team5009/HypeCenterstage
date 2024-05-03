package org.firstinspires.ftc.teamcode.teleop

import ca.helios5009.hyperion.core.HyperionPath
import ca.helios5009.hyperion.core.Motors
import ca.helios5009.hyperion.core.Movement
import ca.helios5009.hyperion.core.Odometry
import ca.helios5009.hyperion.misc.events.EventListener
import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.DriveConstants
import org.firstinspires.ftc.teamcode.OdometryValues
import org.firstinspires.ftc.teamcode.Robot
import org.firstinspires.ftc.teamcode.RotateConstants
import org.firstinspires.ftc.teamcode.StrafeConstants
import org.firstinspires.ftc.teamcode.misc.MenuPathSelector

@TeleOp(name="OdoTest", group="Test")
class OdometryTest() : LinearOpMode() {
    val listener = EventListener()
    override fun runOpMode() {
        val bot = Robot(this)
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

        telemetry.addLine("${OdometryValues.distanceLeftRight}")
        telemetry.update()
        waitForStart()

        path.wait("Nothingness")
    }
}