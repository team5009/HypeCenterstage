package org.firstinspires.ftc.teamcode.autonomous

import ca.helios5009.hyperion.core.HyperionPath
import ca.helios5009.hyperion.misc.Position
import ca.helios5009.hyperion.misc.commands.EventCall
import ca.helios5009.hyperion.misc.commands.Point
import ca.helios5009.hyperion.misc.events.EventListener
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.Camera
import org.firstinspires.ftc.teamcode.Robot

class RedRight(val bot: Robot, val opMode: LinearOpMode, val path: HyperionPath, val listener: EventListener) : StartPosition() {
    init {
        path.start(Point(64.0, 12.0, 0.0))
    }
    val cam = Camera(1)
    override fun stageDoorPath(ca : Int) {
        opMode.telemetry.addLine("StageDoor Happened?")
        opMode.telemetry.update()
        opMode.sleep(1000)
    }

    override fun trussPath(ca : Int) {
    }

    override fun mainPath(pos: Position, wait : Long) {
        if (pos == Position.CENTER) {
            path.continuousLine(arrayListOf(
                Point(-15.0, -7.0, 0.0).useRelative(),
                Point(-0.4, 0.9, -0.7, EventCall("constant_move|100.0|nothing")),
                Point(9.0, 45.0, 101.0)
                ,Point(30.0, 59.0, 97.0).useError()
                ,Point(30.0, 53.0, 97.0).useError()
            ))
        } else if(pos == Position.LEFT) {
            path.continuousLine(arrayListOf(
                Point(-18.0, -6.0, 0.0).useRelative()
                ,Point(-0.4, -0.7, -0.4, EventCall("constant_move|100.0|nothing"))
                ,Point(12.0, 36.0, 90.0)
                ,Point(50.0, 52.0, 75.0, EventCall("slow_down_movement"))
            ))
        } else if(pos == Position.RIGHT) {
            path.continuousLine(arrayListOf(
                Point(-15.0, 10.0, 0.0).useRelative()
                ,Point(0.4, -0.17, -0.2, EventCall("constant_move|-30.0|yellow_outtake"))
                , Point(43.0, 46.0, 93.0, EventCall("yellow_outtake"))
                , Point(43.0, 47.0, 93.0, EventCall("slow_down_movement"))
                , Point(0.0, 0.0, 1.2, EventCall("constant_move|93.0|yellow_outtake"))
                , Point(40.0, 47.0, 97.0, EventCall("normalize_movement"))
                , Point(50.0, 40.0, 97.0, EventCall("yellow_outtake"))
                , Point(60.0, 57.0, 97.0)
                ,Point(55.0, 50.0, 95.0)
            ))
        }
        path.line(Point(62.0,48.0, 102.0))
        path.end(EventCall("done"))
    }

    override fun subPath(pos: Position, wait : Long) {
        if (pos == Position.CENTER) {
            path.continuousLine(arrayListOf(
                Point(-15.0, -7.0, 0.0).useRelative(),
                Point(-0.4, 0.9, -0.7, EventCall("constant_move|100.0|nothing")),
                Point(9.0, 45.0, 101.0)
                ,Point(30.0, 59.0, 97.0).useError()
                ,Point(30.0, 53.0, 97.0).useError()
            ))
        } else if(pos == Position.LEFT) {
            path.continuousLine(arrayListOf(
                Point(-18.0, -6.0, 0.0).useRelative()
                ,Point(-0.4, -0.7, -0.4, EventCall("constant_move|100.0|nothing"))
                ,Point(12.0, 36.0, 90.0)
                ,Point(50.0, 52.0, 75.0, EventCall("slow_down_movement"))
            ))
        } else if(pos == Position.RIGHT) {
            path.continuousLine(arrayListOf(
                Point(-15.0, 10.0, 0.0).useRelative()
                ,Point(0.4, -0.17, -0.2, EventCall("constant_move|-30.0|yellow_outtake"))
                , Point(43.0, 46.0, 93.0, EventCall("yellow_outtake"))
                , Point(43.0, 47.0, 93.0, EventCall("slow_down_movement"))
                , Point(0.0, 0.0, 1.2, EventCall("constant_move|93.0|yellow_outtake"))
                , Point(40.0, 47.0, 97.0, EventCall("normalize_movement"))
                , Point(50.0, 40.0, 97.0, EventCall("yellow_outtake"))
                , Point(60.0, 57.0, 97.0)
                ,Point(55.0, 50.0, 95.0)
            ))
        }
        path.line(Point(62.0,48.0, 102.0))
        path.end(EventCall("done"))
    }

}