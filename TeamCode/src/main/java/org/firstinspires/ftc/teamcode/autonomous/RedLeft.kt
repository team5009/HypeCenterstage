package org.firstinspires.ftc.teamcode.autonomous

import ca.helios5009.hyperion.core.HyperionPath
import ca.helios5009.hyperion.misc.Position
import ca.helios5009.hyperion.misc.commands.EventCall
import ca.helios5009.hyperion.misc.commands.Point
import ca.helios5009.hyperion.misc.events.EventListener
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.Robot

class RedLeft (val bot: Robot, val opMode: LinearOpMode, val path: HyperionPath, val listener: EventListener) : StartPosition() {
    init {
        path.start(Point(64.0, -36.0, 0.0))
    }

    override fun stageDoorPath(ca : Int) {
        var i = 0
        while(i < ca) {
            path.continuousLine(arrayListOf(
                Point(10.0, 40.0, 97.0 - (i * 20))
                ,Point(0.0, 30.0, 97.0 - (i * 20), EventCall("speed_up_movement"))
                //center point passed
                ,Point(0.0, -35.0, 97.0 - (i * 20), EventCall("top_stack"))
                ,Point(0.0, 0.0, -0.5, EventCall("constant_move|30|nothing"))
                ,Point(15.5, -58.5, 30.0 - (i * 20))
                ,Point(6.0, -64.0, 30.0 - (i * 20))
            ))
            path.continuousLine(arrayListOf(
                Point(-8.0, 2.0, 0.0, EventCall("top_stack")).useRelative()
                ,Point(2.0, 55.0, 40.0)
            ))
            opMode.sleep(1000)
            path.continuousLine(arrayListOf(
                //picked up
                Point(5.0, -50.0, 45.0 - (i * 20))
                ,Point(0.0, -52.0, 87.0 - (i * 20), EventCall("speed_up_movement"))
                ,Point(0.0, -24.0, 97.0 - (i * 20))
                ,Point(0.0, 30.0, 130.0 - (i * 20))
                ,Point(20.0, 45.0, 180.0 - (i * 20), EventCall("top_stack"))
                ,Point(0.0, 0.0, -4.0, EventCall("constant_move|90|max_out_movement"))
            ))
            i ++
        }
        path.continuousLine(arrayListOf(
            Point(-5.0, 0.0, 0.0).useRelative()
            ,Point(25.0, 50.0, 90.0)
        ))
    }

    override fun trussPath(ca : Int) {
        path.continuousLine(arrayListOf(
            Point(5.0, 0.0, 0.0).useRelative()
            ,Point(25.0, 50.0, 90.0)
        ))
    }

    override fun mainPath(pos: Position, wait : Long) {
        if (pos == Position.CENTER || pos == Position.NONE) {
            path.continuousLine(arrayListOf(
                Point(-29.0, -10.0, 0.0).useRelative().useError()
                ,Point(0.0, -0.2, 0.6, EventCall("constant_move|115.0|slow_down_movement"))
                ,Point(4.0, -36.0, 90.0)
            ))
            path.wait(wait + 100)
            path.continuousLine(arrayListOf(
                Point(9.0,  -20.0, 88.0)
                ,Point(10.0, 30.0, 88.0, EventCall("speed_up_movement"))
                ,Point(24.0, 36.0, 88.0, EventCall("yellow_outtake"))
                ,Point(36.5, 55.0, 97.0, EventCall("normalize_movement"))
                ,Point(33.0, 35.0, 97.0, EventCall("bring_back_flapper")).useError()
            ))
        } else if (pos == Position.RIGHT) {
            path.continuousLine(arrayListOf(
                Point(-40.0, -1.0, 45.0,EventCall("normalize_movement")).useRelative()
                ,Point(-2.0, 0.0, 0.8, EventCall("constant_move|120|nothing")).useError()
                ,Point(15.0, -30.0, 90.0)
            ))
            path.wait(wait)
            path.continuousLine(arrayListOf(
                Point(11.0, -30.0, 90.0)
                ,Point(11.0, 10.0, 90.0, EventCall("speed_up_movement"))
                ,Point(11.0, 30.0, 90.0)
                ,Point(20.0, 40.0, 90.0, EventCall("yellow_outtake")).useError()
                ,Point(38.0, 45.0, 93.0, EventCall("normalize_movement")).useError()
                ,Point(38.0, 50.0, 95.0).useError()
                ,Point(38.0, 58.0, 95.0, EventCall("yellow_outtake")).useError()
            ))
            opMode.sleep(100)
            bot.flapper.position = 0.0
            path.continuousLine(arrayListOf(
                Point(35.0, 50.0, 95.0, EventCall("normalize_movement")).useError()
                ,Point(20.0, 50.0, 95.0)
            ))
        } else if (pos == Position.LEFT) {
            path.continuousLine(arrayListOf(
                Point(-14.0, -30.0, 0.0,EventCall("normalize_movement")).useRelative().useError()
                ,Point(0.0, -0.15, 0.6, EventCall("constant_move|110.0|nothing")).useError()
                ,Point(24.0, -45.0, 90.0, EventCall("sleep"))
            ))
            path.wait(wait + 100)
            path.continuousLine(arrayListOf(
                Point(5.0, 10.0, 90.0, EventCall("Speed_up_movement"))
                ,Point(15.0, 20.0, 90.0, EventCall("yellow_outtake"))
                ,Point(26.0, 45.0, 90.0, EventCall("normalize_movement"))
                ,Point(26.0, 54.0, 90.0, EventCall("yellow_outtake"))
                ,Point(26.0, 54.0, 90.0)
                ,Point(50.0, 50.0, 90.0, EventCall("bring_back_flapper"))
            ))
        }
        opMode.sleep(300)
        bot.flapper.position = 0.0
        path.end(EventCall("done"))
    }

    override fun subPath(pos: Position, wait: Long) {
        TODO("Not yet implemented")
    }
}