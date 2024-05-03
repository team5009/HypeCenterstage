package org.firstinspires.ftc.teamcode

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration
import org.firstinspires.ftc.vision.VisionProcessor
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc


class Camera(alliance : Int) : VisionProcessor {
    enum class Position {
        Left, Right, Middle, None, Top, Bottom
    }
    var PosX: Position = Position.Middle
    var PosY: Position = Position.Middle
    var Area: Double = 0.0
    val contours = ArrayList<MatOfPoint>()
    private var biggestContour = MatOfPoint()
    var differenceX = 0.0
    var differenceY = 0.0
    object Center {
        var x = 0.0
        var y = 0.0
    }
    object Screen {
        var width = 0
        var height = 0
    }
    private val white = Scalar(255.0, 255.0, 255.0)

    private val color = alliance
    override fun init(width: Int, height: Int, calibration: CameraCalibration) {
    }

    override fun processFrame(frame: Mat, captureTimeNanos: Long): Any? {
        val centerX = frame.width() / 2.0
        val fifthY = frame.height() / 5.0
        val bottom = frame.height()
        Screen.height = frame.height()
        Screen.width = frame.width()

        val heightArray: DoubleArray = doubleArrayOf(0.0 * fifthY, 1.0 * fifthY, 2.0 * fifthY, 3.0 * fifthY, 4.0 * fifthY, 5.0 * fifthY)
        val widthArray: DoubleArray = doubleArrayOf(centerX - 15, centerX + 15)
        val mat = Mat()

        Imgproc.cvtColor(frame, mat, Imgproc.COLOR_RGB2HSV)
        if (mat.empty()) return frame

        val thresh = Mat()

        var lowHSV = Scalar(100.0, 90.0, 80.0)
        var highHSV = Scalar(130.0, 255.0, 110.0)
        if (color == 1) {
            lowHSV = Scalar(0.0, 10.0, 0.0)
            highHSV = Scalar(15.0, 255.0, 150.0)
        }
        Core.inRange(mat, lowHSV, highHSV, thresh)

        if (thresh.empty()) return frame

        val masked = Mat()
        Core.bitwise_and(mat,mat,masked,thresh)
        //calc avg HSV values of the white thresh values
        val avg = Core.mean(masked, thresh)

        //scale avg saturation to 150
        val scaledMask = Mat()
        masked.convertTo(scaledMask, -1, 150/avg.`val`[1], 0.0)

        val scaledThresh = Mat()
        val strictLowHSV = Scalar(0.0, 100.0, 18.0)
        val strictHighHSV = Scalar(255.0, 255.0, 255.0)

        Core.inRange(scaledMask, strictLowHSV, strictHighHSV, scaledThresh)

        val kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
            Size(5.0, 5.0)
        )

        val cleanup = Mat()
        Imgproc.morphologyEx(scaledThresh, cleanup, Imgproc.MORPH_OPEN, kernel)

        val finalCleanup = Mat()
        Imgproc.morphologyEx(cleanup, finalCleanup, Imgproc.MORPH_CLOSE, kernel)

        val contour = Mat()
        Imgproc.findContours(
            finalCleanup,
            contours,
            contour,
            Imgproc.RETR_LIST,
            Imgproc.CHAIN_APPROX_SIMPLE
        )

        val finalMask = Mat()
        Core.bitwise_and(mat,mat,finalMask,scaledThresh)


        if (contours.size != 0) {
            Imgproc.drawContours(
                frame,
                contours,
                -1,
                Scalar(0.0, 255.0, 0.0),
                2
            )
            biggestContour = contours.maxByOrNull { Imgproc.contourArea(it) }!!
            val boundRect = Imgproc.boundingRect(biggestContour)
            val M = Imgproc.moments(biggestContour)
            Center.x = (M._m10/M._m00)
            Center.y = boundRect.y.toDouble() + boundRect.height

            Imgproc.rectangle(
                frame,
                Point(boundRect.x.toDouble(), boundRect.y.toDouble()),
                Point(boundRect.x.toDouble() + boundRect.width, boundRect.y.toDouble() + boundRect.height),
                Scalar(255.0, 255.0, 0.0),
                2
            )
            Area = (boundRect.width * boundRect.height).toDouble()
        } else {
            Center.x = 0.0
            Center.y = 0.0
            Area = 0.0
        }

        Imgproc.circle(frame, Point(Center.x, Center.y), 3, white, -1)

//        input.release()
//        scaledThresh.copyTo(input)
//        Imgproc.cvtColor(finalMask, input, Imgproc.COLOR_HSV2RGB)
        mat.release()
        thresh.release()
        masked.release()
        scaledMask.release()
        scaledThresh.release()
        cleanup.release()
        finalCleanup.release()
        kernel.release()
        contour.release()
        contours.clear()

        if (Center.x <= widthArray[1] && Center.x >= widthArray[0]) {
            PosX = Position.Middle
            differenceX = 0.0
        } else if (Center.x > widthArray[1]) {
            PosX = Position.Right
            differenceX = (centerX - Center.x) / 100.0
        } else if (Center.x < widthArray[0] && Center.x != 0.0) {
            PosX = Position.Left
            differenceX = (centerX - Center.x) / 100.0
        } else {
            PosX = Position.None
            differenceX = 0.0
        }

        if (Center.y >= heightArray[3] && Center.y <= heightArray[4]) {
            PosY = Position.Middle
            differenceY = 0.0
        } else if (Center.y < heightArray[3] && Center.y != heightArray[0]) {
            PosY = Position.Top
            differenceY = (heightArray[3] - Center.y) / 100.0
        } else if (Center.y > heightArray[4]) {
            PosY = Position.Bottom
            differenceY = (heightArray[4] - Center.y) / 100.0
        } else {
            PosY = Position.None
            differenceY = 0.0
        }

        return frame
    }

    override fun onDrawFrame(canvas: Canvas, onscreenWidth: Int, onscreenHeight: Int, scaleBmpPxToCanvasPx: Float, scaleCanvasDensity: Float, userContext: Any) {
        val rectPaint = Paint()

        rectPaint.color = Color.RED
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = scaleCanvasDensity * 4
    }

    fun detectProp(alliance : Int) : Int {
        val centerX : Double = getCenter().x
        val size = getSize()
        if (alliance == 1) { // red side
            if (size > 1000) {
                if (centerX > 200 && centerX < 400) {
                    //telemetry.addData("Prop: ", "Center")
                    return 5
                } else if (centerX < 200) {
                    //telemetry.addData("Prop: ", "Left")
                    return 4
                }
            } else {
                //telemetry.addData("Prop: ", "Right")
                return 6
            }
        } else if (alliance == 2) { //blue side
            if (size > 1000) {
                if (centerX > 200 && centerX < 400) {
                    //telemetry.addData("Prop: ", "Center")
                    return 2
                } else if (centerX > 400) {
                    //telemetry.addData("Prop: ", "Right")
                    return 3
                }
            }
        }
        return (8 - (alliance * alliance * 2 - (alliance - 1)))

    }

    fun getCenter() : Center {
        return Center
    }
    fun getSize() : Double {
        return Area
    }
}