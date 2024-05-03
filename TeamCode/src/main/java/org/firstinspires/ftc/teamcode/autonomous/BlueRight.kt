package org.firstinspires.ftc.teamcode.autonomous

import ca.helios5009.hyperion.core.HyperionPath
import ca.helios5009.hyperion.misc.Position
import ca.helios5009.hyperion.misc.commands.EventCall
import ca.helios5009.hyperion.misc.commands.Point
import ca.helios5009.hyperion.misc.events.EventListener
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.Robot

class BlueRight (val bot: Robot, val opMode: LinearOpMode, val path: HyperionPath, val listener: EventListener) : StartPosition() {
    init {
        path.start(Point(-60.0, -30.0, 180.0))
    }

    override fun stageDoorPath(ca: Int) {
        var i = 0
        while(i < ca) {
            path.continuousLine(arrayListOf(
                Point(-30.0, 40.0, 90.0)
                ,Point(0.0, 30.0, 90.0, EventCall("speed_up_movement"))
                ,Point(-4.0, -24.0, 90.0, EventCall("normalize_movement"))
                ,Point(-64.0, -20.0, 90.0)
                ,Point(-64.0, -30.0, 90.0, EventCall("top_stack"))
                ,Point(-64.0, -32.0, 90.0, EventCall("top_stack"))
                ,Point(-55.0, -30.0, 90.0)
                ,Point(-50.0, -32.0, 90.0, EventCall("top_stack"))
                ,Point(-48.0, -32.0, 90.0)
            ))
            path.wait(100)
            path.continuousLine(arrayListOf(
                Point(-10.0, -24.0, 90.0, EventCall("speed_up_movement"))
                ,Point(-10.0, 30.0, 130.0, EventCall("normalize_movement"))
                ,Point(-20.0, 45.0, 180.0)
                ,Point(0.0, 0.0, -4.0, EventCall("constant_move|90|max_out_movement"))
            ))
            i ++
        }
        path.continuousLine(arrayListOf(
            //Point(10.0, -5.0, 0.0).useRelative()
            Point(-10.0, 50.0, 90.0)
        ))
    }

    override fun trussPath(ca : Int) {
        var i = 0
        while(i < ca) {
            i ++
        }
        path.continuousLine(arrayListOf(
            Point(10.0, -5.0, 0.0).useRelative()
            ,Point(-10.0, 50.0, 90.0)
        ))
    }

    override fun mainPath(pos: Position, wait : Long) {
        if (pos == Position.CENTER || pos == Position.NONE) {
            path.continuousLine(arrayListOf(
                Point(34.0, -37.0, 180.0, EventCall("normalize_movement"))
                /*,Point(-5.0, 0.0, 0.0).useRelative()
                ,Point(-14.0, -40.0, 90.0)*/
                ,Point(23.0, 40.0, 180.0)
                ))
                path.wait(wait)/*
                path.continuousLine(arrayListOf(
                    Point(-9.0,  -20.0, 88.0)
                    //,Point(-10.0, 30.0, 88.0, EventCall("speed_up_movement"))
                    //,Point(-24.0, 36.0, 88.0, EventCall("yellow_outtake"))
                    ,Point(-6.5, 59.0, 97.0, EventCall("normalize_movement"))
                    //,Point(-33.0, 35.0, 97.0, EventCall("bring_back_flapper")).useError()
            ))*/
            path.wait("yellow_outtake_finish", EventCall("bring_back_flapper"))
        } else if (pos == Position.LEFT){
            path.continuousLine(arrayListOf(
                Point(-28.0, -40.0, 140.0, EventCall("normalize_movement"))
                ,Point(-14.0, -40.0, 82.0)
                ,Point(-3.0,-40.0, 70.0)
                //purple pixel out taked
                ,Point(-5.0, -43.0, 90.0)
                ,Point(-5.0, 50.0, 90.0)
            ))
            path.wait(wait)/*
            path.continuousLine(arrayListOf(
                Point(-11.0, -30.0, 90.0)
                ,Point(-11.0, 10.0, 90.0, EventCall("speed_up_movement"))
                ,Point(-11.0, 30.0, 90.0)/*
                ,Point(-10.0, 50.0, 90.0, EventCall("yellow_outtake")).useError()
                ,Point(-48.0, 50.0, 93.0, EventCall("normalize_movement")).useError()
                ,Point(-48.0, 58.0, 95.0, EventCall("yellow_outtake")).useError()*/
            ))*/
        } else if (pos == Position.RIGHT) {
            path.continuousLine(arrayListOf(
                Point(21.0, -17.0, 0.0, EventCall("slow_down_movement")).useRelative().useError()
                //Point(-7.0, -16.0, 0.0, EventCall("normalize_movement")).useRelative()
                ,Point(-54.0, -45.0, 0.0)
            ))
            path.wait(wait)/*
            path.continuousLine(arrayListOf(
                Point(10.0, -5.0, 160.0)
                ,Point(0.0, 30.0, 90.0)
                ,Point(-10.0, 43.0, 90.0, EventCall("yellow_outtake"))
                ,Point(-40.0, 45.0, 90.0)
                ,Point(-40.0, 63.0, 90.0)
                ,Point(-55.0, 50.0, 90.0)
            ))
            path.wait("yellow_outtake_finish", EventCall("yellow_outtake"))*/
        }
        /*path.continuousLine(arrayListOf(
            Point(-55.0, 40.0, 90.0)
            ,Point(-60.0, 40.0, 90.0)
        ))
        opMode.sleep(300)
        bot.flapper.position = 0.0*/
    }

    override fun subPath(pos: Position, wait: Long) {
        if (pos == Position.CENTER || pos == Position.NONE) {
            path.continuousLine(
                arrayListOf(
                    Point(-28.0, -44.0, 140.0, EventCall("normalize_movement")),
                    Point(-14.0, -44.0, 82.0),
                    Point(0.0, -44.0, 70.0)
                )
            )
        } else if (pos == Position.RIGHT) {
            path.continuousLine(
                arrayListOf(
                    Point(19.0, -12.0, 0.0, EventCall("slow_down_movement")).useRelative().useError(),
                    Point(-17.0, 10.0, 0.0, EventCall("normalize_movement")).useRelative()
                )
            )
        } else if (pos == Position.LEFT){
            path.continuousLine(arrayListOf(
                Point(15.0, -6.0, 0.0).useRelative()
                ,Point(-0.4, -0.7, -0.4, EventCall("constant_move|100.0|nothing"))
                ,Point(-2.0, -23.0, 90.0)
            ))
        }
    }
}





/*code is for stacking
* //get pixels from stack
        listener.call("top_stack")
        opMode.sleep(1000)

        path.continuousLine(arrayListOf(
            Point(-64.0, -30.0, 180.0)
            ,Point(-64.0, -32.0, 180.0, EventCall("top_stack"))
            ,Point(-55.0, -30.0, 180.0)
            ,Point(-50.0, -32.0, 180.0, EventCall("top_stack"))
            ,Point(-48.0, -32.0, 180.0)
        ))
        //listener.call("top_stack")
        opMode.sleep(1000)*/