package org.firstinspires.ftc.teamcode.instances.autonomous

import ca.helios5009.hyperion.core.Odometry
import ca.helios5009.hyperion.misc.commands.Point
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.robot.Robot
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

/*
class com : LinearOpMode() {

        val bot = Robot()
        val odo = Odometry()
        var isbigger: Boolean = false
        fun goToSpline(splines: List<Point>) {
            val points = splines
            val target = splines[0]
            for (point in points) {
                isbigger = Euclidean(point, target) < Euclidean(odo.location, target)
                goToNextPoint(point, odo.location, target, isbigger)
            }
            goToEndPoint(odo.location, target)
            moveRobot(0.0, 0.0, 0.0)
        }


        fun Euclidean(pointA: Point, pointB: Point): Double {
            return Math.sqrt(((pointB.x - pointA.x) * (pointB.x - pointA.x)) + ((pointB.y - pointA.y) * (pointB.y - pointA.y)))
        }

        fun goToNextPoint(point: Point, robot: Point, target: Point, isbig: Boolean) {

            while (isbig == Euclidean(point, target) < Euclidean(robot, target)) {
                val magnitude = Euclidean(point, robot)
                if (magnitude == 0.0) {
                    break
                }
                val speedFactor = Euclidean(robot, target)
                val dX = (point.x - robot.x) / magnitude * speedFactor
                val dY = (point.y - robot.y) / magnitude * speedFactor
                val theta = -robot.rot
                val drive = (dX * cos(theta) - dY * sin(theta))
                val strafe = (dX * sin(theta) + dY * cos(theta))
                val turn = point.rot - robot.rot
                moveRobot(drive, strafe, turn)
            }
        }

        fun goToEndPoint(robot: Point, target: Point) {
            val dX = target.x - robot.x
            val dY = target.y - robot.y
            val theta = robot.rot
            val drive = (dX * cos(theta) - dY * sin(theta))
            val strafe = (dX * sin(theta) + dY * cos(theta))
            val turn = target.x - robot.rot
            moveRobot(drive, strafe, turn)
        }


        fun moveRobot(drive: Double, strafe: Double, yaw: Double) {
            var lF = drive - strafe - yaw
            var rF = drive + strafe + yaw
            var lB = drive + strafe - yaw
            var rB = drive - strafe + yaw
            var max = max(abs(lF), abs(rF))
            max = max(max, abs(lB))
            max = max(max, abs(rB))

            //normalize the motor values
            if (max > 1.0) {
                lF /= max
                rF /= max
                lB /= max
                rB /= max
            }
            //send power to the motors
            bot.move(lF, rF, lB, rB)
            /*if (showTelemetry) {
            myOpMode.telemetry.addData("Axes D:S:Y", "${drive}, ${strafe}, ${yaw}")
            myOpMode.telemetry.addData("Wheels lf:rf:lb:rb", "${lF}, ${rF}, ${lB}, ${rB}")
            myOpMode.telemetry.update() //  Assume this is the last thing done in the loop.
        }*/
        }

} */