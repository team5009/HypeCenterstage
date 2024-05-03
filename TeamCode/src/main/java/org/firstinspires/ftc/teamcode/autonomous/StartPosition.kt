package org.firstinspires.ftc.teamcode.autonomous

import ca.helios5009.hyperion.misc.Position

abstract class StartPosition {
    abstract fun stageDoorPath(ca : Int)

    abstract fun trussPath(ca: Int)

    abstract fun mainPath(pos: Position, wait : Long)

    abstract fun subPath(pos: Position, wait: Long)
}