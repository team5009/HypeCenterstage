package org.firstinspires.ftc.teamcode.autonomous

import ca.helios5009.hyperion.core.HyperionPath
import ca.helios5009.hyperion.misc.Position
import ca.helios5009.hyperion.misc.commands.EventCall
import ca.helios5009.hyperion.misc.commands.Point
import ca.helios5009.hyperion.misc.events.EventListener
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.Robot

class BlueLeft (val bot: Robot, val opMode: LinearOpMode, val path: HyperionPath, val listener: EventListener) : StartPosition() {
    init {
        path.start(Point(-64.0, 12.0, 180.0))
    }
    override fun stageDoorPath(ca : Int) {
        var i = 0
        while(i < ca) {
            path.continuousLine(arrayListOf(
                Point(-30.0, 40.0, 97.0 + (i * 20))
                ,Point(0.0, 30.0, 97.0 + (i * 20), EventCall("speed_up_movement"))
                //center point passed
                ,Point(-9.0, -35.0, 97.0 + (i * 20), EventCall("top_stack"))
                ,Point(0.0, 0.0, -0.5, EventCall("constant_move|30|nothing"))
                ,Point(-15.5, -58.5, 30.0 + (i * 20))
                ,Point(-6.0, -64.0, 30.0 + (i * 20))
            ))
            path.continuousLine(arrayListOf(
                Point(8.0, 2.0, 0.0, EventCall("top_stack")).useRelative()
                ,Point(-2.0, 55.0, 40.0)
            ))
            opMode.sleep(1000)
            path.continuousLine(arrayListOf(
                //picked up
                Point(-5.0, -50.0, 45.0 + (i * 20))
                ,Point(0.0, -52.0, 87.0 + (i * 20), EventCall("speed_up_movement"))
                ,Point(0.0, -24.0, 97.0 + (i * 20))
                ,Point(0.0, 30.0, 130.0 + (i * 20))
                ,Point(-20.0, 45.0, 180.0 + (i * 20), EventCall("top_stack"))
                ,Point(0.0, 0.0, -4.0, EventCall("constant_move|90|max_out_movement"))
            ))
            i ++
        }
        path.continuousLine(arrayListOf(
            Point(0.0, 5.0, 0.0).useRelative()
            ,Point(0.0, 60.0, 90.0 + (i * 20))
        ))
    }

    override fun trussPath(ca : Int) {
        var i = 0
        while(i < ca) {
            i ++
        }
        path.continuousLine(arrayListOf(
            Point(-10.0, 5.0, 0.0).useRelative()
            ,Point(-60.0, 60.0, 90.0)
        ))
    }

    override fun mainPath(pos: Position, wait: Long) {
        if (pos == Position.CENTER || pos == Position.NONE) {
            path.continuousLine(arrayListOf(
                Point(22.0, 2.0, 0.0, EventCall("speed_up_movement")).useRelative().useError()
                ,Point(0.4, -0.9, 0.7, EventCall("constant_move|210.0|nothing"))
                ,Point(0.0, 8.0, 0.0).useRelative()
                //purple pixel dropped
                ,Point(-30.0,38.0, 77.0)
                ,Point(-38.0,38.0, 77.0, EventCall("yellow_outtake"))
                ,Point(-46.0, 52.0, 77.0, EventCall("slow_down_movement")).useError()
                ,Point(-20.0, 20.0, 77.0)
            ))
            opMode.sleep(300)
            bot.flapper.position = 0.6
            opMode.sleep(300)
            //yellow pixel dropped
            path.end(EventCall("done"))
        } else if (pos == Position.LEFT) {
            path.continuousLine(arrayListOf(
                Point(16.0, 7.0, 0.0, EventCall("normalize_movement")).useRelative()
                ,Point(0.4, -0.9, 0.7, EventCall("constant_move|210.0|nothing"))
                ,Point(0.0, 5.0, 0.0).useRelative()
                //Purple pixel dropped
                ,Point(-50.0, 40.0, 150.0)
                ,Point(-50.0, 40.0, 77.0, EventCall("yellow_outtake")) //yellow pixel dropped
                ,Point(-52.0, 48.0, 77.0, EventCall("bring_back_yellow_pixel"))
                ,Point(-52.0, 59.0, 77.0)
            ))
            path.end(EventCall("done"))
        } else if (pos == Position.RIGHT) {
            path.continuousLine(arrayListOf(
                Point(21.0,2.0, 45.0,EventCall("normalize_movement")).useRelative()
                ,Point(-0.9, 0.0, 0.8, EventCall("constant_move|270|nothing")).useError()
                //purple pixel dropped
                ,Point(-38.0, 38.0, 180.0)
                ,Point(0.0, 0.0, -4.0, EventCall("constant_move|77|yellow_outtake"))
                ,Point(-34.0, 40.0, 90.0, EventCall("normalize_movement"))
                ,Point(-34.0, 58.0, 90.0)
            ))
            path.wait("yellow_outtake_finish", EventCall("yellow_outtake"))
            //yellow pixel dropped
            path.end(EventCall("done"))
        }
        bot.flapper.position = 0.0
        /*path.continuousLine(arrayListOf(
            Point(0.0, -10.0, 0.0, EventCall("normalize_movement")).useRelative()
            ,Point(-15.0, -5.0, 0.0).useRelative()
            ,Point(-55.0, 40.0, 90.0)
            ,Point(-60.0, 40.0, 90.0)
        ))*/
        path.end(EventCall("done"))
    }

    override fun subPath(pos: Position, wait : Long) {
        if (pos == Position.CENTER || pos == Position.NONE) {
            path.continuousLine(arrayListOf(
                Point(25.0, 2.0, 0.0, EventCall("normalize_movement")).useRelative().useError()
                ,Point(0.4, -0.9, 0.7, EventCall("constant_move|210.0|nothing"))
                ,Point(0.0, 8.0, 0.0).useRelative()
                //purple pixel dropped
                ,Point(-30.0,38.0, 80.0)
                ,Point(-36.0,38.0, 77.0, EventCall("yellow_outtake"))
                ,Point(-44.0, 42.0, 69.0)
                ,Point(-44.0, 52.3, 69.0)
                ,Point(0.0, -1.0, 0.0).useRelative()
            ))
            opMode.sleep(300)
            bot.flapper.position = 0.6
            opMode.sleep(300)
            //yellow pixel dropped
            path.line(
                Point(-44.0, 48.0, 69.0, EventCall("speed_up_movement")).useError()
            )
            path.end(EventCall("done"))
        } else if (pos == Position.LEFT) {
            path.continuousLine(arrayListOf(
                Point(16.0, 6.0, 0.0, EventCall("normalize_movement")).useRelative()
                ,Point(0.4, -0.9, 0.7, EventCall("constant_move|210.0|nothing"))
                ,Point(0.0, 5.0, 0.0).useRelative()
                //Purple pixel dropped
                ,Point(-50.0, 40.0, 150.0)
                ,Point(-50.0, 40.0, 77.0, EventCall("yellow_outtake")) //yellow pixel dropped
                ,Point(-52.0, 48.0, 77.0, EventCall("slow_down_movement"))
                ,Point(-52.0, 59.0, 77.0)
            ))
            path.end(EventCall("done"))
        } else if (pos == Position.RIGHT) {
            path.continuousLine(arrayListOf(
                Point(21.0,2.0, 45.0,EventCall("normalize_movement")).useRelative()
                ,Point(-0.9, 0.0, 0.8, EventCall("constant_move|270|nothing")).useError()
                //purple pixel dropped
                ,Point(-38.0, 38.0, 180.0)
                ,Point(0.0, 0.0, -4.0, EventCall("constant_move|77|yellow_outtake"))
                ,Point(-34.0, 40.0, 75.0, EventCall("normalize_movement"))
                ,Point(-34.0, 55.0, 60.0)
            ))
            path.wait("yellow_outtake_finish", EventCall("yellow_outtake"))
            //yellow pixel dropped
            path.end(EventCall("done"))
        }
        bot.flapper.position = 0.0
        path.continuousLine(arrayListOf(
            Point(0.0, -5.0, 0.0, EventCall("normalize_movement")).useRelative()
            ,Point(-15.0, -5.0, 0.0).useRelative()
            ,Point(-55.0, 44.0, 90.0)
            ,Point(-60.0, 44.0, 90.0)
        ))
        path.end(EventCall("done"))
    }


}