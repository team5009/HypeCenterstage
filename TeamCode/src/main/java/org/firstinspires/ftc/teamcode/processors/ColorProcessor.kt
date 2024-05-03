package org.firstinspires.ftc.teamcode.processors

import android.graphics.Canvas
import ca.helios5009.hyperion.misc.Alliance
import ca.helios5009.hyperion.misc.Position
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration
import org.firstinspires.ftc.vision.VisionProcessor
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class ColorProcessor(var alliance: Alliance, private val optimized: Boolean = true): VisionProcessor {

	val center = Point()
	var position = Position.NONE
	override fun init(width: Int, height: Int, calibration: CameraCalibration?) {
		return
	}

	override fun processFrame(frame: Mat, captureTimeNanos: Long): Any {
		val contours = mutableListOf<MatOfPoint>()
		val tempFrame = Mat()
		Imgproc.cvtColor(frame, tempFrame, Imgproc.COLOR_RGB2HSV)
		if (tempFrame.empty()) {
			return frame
		}

		val thresh = Mat()

		val lowHSV = when(alliance) {
			Alliance.BLUE -> Scalar(100.0, 90.0, 100.0)
			Alliance.RED -> Scalar(0.0, 10.0, 0.0)
		}
		val highHSV = when(alliance) {
			Alliance.BLUE -> Scalar(140.0, 255.0, 255.0)
			Alliance.RED -> Scalar(15.0, 255.0, 150.0)
		}


		Core.inRange(tempFrame, lowHSV, highHSV, thresh)

		if (thresh.empty()) {
			return frame
		}

		val mask = Mat()
		Core.bitwise_and(tempFrame, tempFrame, mask, thresh)

		val avg = Core.mean(mask, thresh)

		val scaledMask = Mat()
		mask.convertTo(scaledMask, -1, 150/avg.`val`[1], 0.0)

		val scaledThresh = Mat()
		val strictLowHSV = Scalar(0.0, 100.0, 18.0)
		val strictHighHSV = Scalar(255.0, 255.0, 200.0)

		Core.inRange(scaledMask, strictLowHSV, strictHighHSV, scaledThresh)

		val kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, Size(5.0, 5.0))

		val cleanup = Mat()
		Imgproc.morphologyEx(scaledThresh, cleanup, Imgproc.MORPH_OPEN, kernel)
		Imgproc.morphologyEx(cleanup, cleanup, Imgproc.MORPH_CLOSE, kernel)

		val contour = Mat()
		Imgproc.findContours(cleanup, contours, contour, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE)

		val finalMask = Mat()
		Core.bitwise_and(tempFrame, tempFrame, finalMask, cleanup)

		if (contours.isNotEmpty()) {
			val biggest = contours.maxByOrNull { Imgproc.contourArea(it) }
			val moment = Imgproc.moments(biggest)
			val size = Imgproc.boundingRect(biggest)
			center.x = moment.m10 / moment.m00
			center.y = size.y.toDouble() + size.height
			if (!optimized) {
				Imgproc.drawContours(frame, contours, -1, Scalar(0.0, 255.0, 0.0), 3)
				Imgproc.rectangle(
					frame,
					Point(size.x.toDouble(), size.y.toDouble()),
					Point(size.x.toDouble() + size.width, size.y.toDouble() + size.height),
					Scalar(255.0, 255.0, 0.0),
					3
				)
				Imgproc.circle(frame, center, 3, Scalar(255.0, 0.0, 0.0), -1)
			}
		} else {
			center.x = -1.0
			center.y = -1.0
		}

		position = if (center.x < 0) {
			Position.NONE
		} else if (center.x < 200) {
			Position.RIGHT
		} else if (center.x > 200 && center.x < 400) {
			Position.CENTER
		} else {
			Position.LEFT
		}

		tempFrame.release()
		thresh.release()
		mask.release()
		scaledMask.release()
		scaledThresh.release()
		kernel.release()
		cleanup.release()
		contour.release()
		finalMask.release()

		return frame
	}

	override fun onDrawFrame(
		canvas: Canvas?,
		onscreenWidth: Int,
		onscreenHeight: Int,
		scaleBmpPxToCanvasPx: Float,
		scaleCanvasDensity: Float,
		userContext: Any?
	) {
		return
	}
}
