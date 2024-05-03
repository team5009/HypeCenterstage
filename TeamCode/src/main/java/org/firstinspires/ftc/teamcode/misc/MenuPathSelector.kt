package org.firstinspires.ftc.teamcode.misc

import ca.helios5009.hyperion.misc.Alliance
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

class MenuPathSelector {
	private var currentPage = MENU_PAGE.ALLIANCE_SELECT
	private var gamepadDownPressed = false
	private var gamepadUpPressed = false
	private var gamepadAPressed = false
	private var gamepadBPressed = false

	private var selectedAlliance = 0
	private var selectedSide = 0
	private var selectedPath = 0
	private var selectedCycle = 0

	private val allianceOptions = arrayOf(Alliance.RED, Alliance.BLUE)
	private val sideOptions = arrayOf(SIDE.LEFT, SIDE.RIGHT)
	private val travelOptions = arrayOf(PATH.MAIN, PATH.SUB)
	private val cycleOptions = arrayOf(CYCLE.STAGEDOOR, CYCLE.TRUSS)

	var allianceOption = Alliance.RED
	var sideOption = SIDE.LEFT
	var travelOption = PATH.MAIN
	var cycleOption = CYCLE.STAGEDOOR
	var cycleamount : Int = 0
	var waittime = 0L
	var ready = false
	fun run(instance: LinearOpMode) {
		val t = instance.telemetry
		val g1 = instance.gamepad1
		when (currentPage) {
			MENU_PAGE.ALLIANCE_SELECT -> {
				t.addLine("Alliance Select")
				t.addData(">", allianceOptions[selectedAlliance])
				t.update()

				if (g1.dpad_up && !gamepadUpPressed) {
					if (selectedAlliance == allianceOptions.size - 1) {
						selectedAlliance = 0
					} else {
						selectedAlliance++
					}
					gamepadUpPressed = true
				} else if (!g1.dpad_up && gamepadUpPressed) {
					gamepadUpPressed = false
				} else if (g1.dpad_down && !gamepadDownPressed) {
					if (selectedAlliance == 0) {
						selectedAlliance = allianceOptions.size - 1
					} else {
						selectedAlliance--
					}
					gamepadDownPressed = true
				} else if (!g1.dpad_down && gamepadDownPressed) {
					gamepadDownPressed = false
				}

				if (g1.a && !gamepadAPressed) {
					currentPage = MENU_PAGE.SIDE_SELECT
					allianceOption = allianceOptions[selectedAlliance]
					gamepadAPressed = true
				} else if (!g1.a && gamepadAPressed) {
					gamepadAPressed = false
				}
			}
			MENU_PAGE.SIDE_SELECT -> {
				t.addLine("Side Select")
				t.addData(">", sideOptions[selectedSide])
				t.update()

				if (g1.dpad_up && !gamepadUpPressed) {
					if (selectedSide == sideOptions.size - 1) {
						selectedSide = 0
					} else {
						selectedSide++
					}
					gamepadUpPressed = true
				} else if (!g1.dpad_up && gamepadUpPressed) {
					gamepadUpPressed = false
				} else if (g1.dpad_down && !gamepadDownPressed) {
					if (selectedSide == 0) {
						selectedSide = sideOptions.size - 1
					} else {
						selectedSide--
					}
					gamepadDownPressed = true
				} else if (!g1.dpad_down && gamepadDownPressed) {
					gamepadDownPressed = false
				}

				if (g1.a && !gamepadAPressed) {
					currentPage = MENU_PAGE.PATH_SELECT
					sideOption = sideOptions[selectedSide]
					gamepadAPressed = true
				} else if (!g1.a && gamepadAPressed) {
					gamepadAPressed = false
				}

				if (g1.b && !gamepadBPressed) {
					currentPage = MENU_PAGE.ALLIANCE_SELECT
					gamepadBPressed = true
				} else if (!g1.b && gamepadBPressed) {
					gamepadBPressed = false
				}
			}
			MENU_PAGE.PATH_SELECT -> {
				t.addLine("Path Select")
				t.addData(">", travelOptions[selectedPath])
				t.update()

				if (g1.dpad_up && !gamepadUpPressed) {
					if (selectedPath == travelOptions.size - 1) {
						selectedPath = 0
					} else {
						selectedPath++
					}
					gamepadUpPressed = true
				} else if (!g1.dpad_up && gamepadUpPressed) {
					gamepadUpPressed = false
				} else if (g1.dpad_down && !gamepadDownPressed) {
					if (selectedPath == 0) {
						selectedPath = travelOptions.size - 1
					} else {
						selectedPath--
					}
					gamepadDownPressed = true
				} else if (!g1.dpad_down && gamepadDownPressed) {
					gamepadDownPressed = false
				}

				if (g1.a && !gamepadAPressed) {
					currentPage = MENU_PAGE.CYCLE_SELECT
					travelOption = travelOptions[selectedPath]
					gamepadAPressed = true

				} else if (!g1.a && gamepadAPressed) {
					gamepadAPressed = false
				}

				if (g1.b && !gamepadBPressed) {
					currentPage = MENU_PAGE.SIDE_SELECT
					gamepadBPressed = true
				} else if (!g1.b && gamepadBPressed) {
					gamepadBPressed = false
				}
			}
			MENU_PAGE.CYCLE_SELECT -> {
				t.addLine("Cycle Select")
				t.addData(">", cycleOptions[selectedCycle])
				t.update()

				if (g1.dpad_up && !gamepadUpPressed) {
					if (selectedCycle == cycleOptions.size - 1) {
						selectedCycle = 0
					} else {
						selectedCycle++
					}
					gamepadUpPressed = true
				} else if (!g1.dpad_up && gamepadUpPressed) {
					gamepadUpPressed = false
				} else if (g1.dpad_down && !gamepadDownPressed) {
					if (selectedCycle == 0) {
						selectedCycle = cycleOptions.size - 1
					} else {
						selectedCycle--
					}
					gamepadDownPressed = true
				} else if (!g1.dpad_down && gamepadDownPressed) {
					gamepadDownPressed = false
				}

				if (g1.a && !gamepadAPressed) {
					currentPage = MENU_PAGE.AMOUNT_SELECT
					cycleOption = cycleOptions[selectedCycle]
					gamepadAPressed = true

				} else if (!g1.a && gamepadAPressed) {
					gamepadAPressed = false
				}

				if (g1.b && !gamepadBPressed) {
					currentPage = MENU_PAGE.PATH_SELECT
					gamepadBPressed = true
				} else if (!g1.b && gamepadBPressed) {
					gamepadBPressed = false
				}
			}
			MENU_PAGE.AMOUNT_SELECT -> {
				t.addLine("Cycle Amount Select")
				t.addData(">", cycleamount)
				t.update()
				if (g1.dpad_up && !gamepadUpPressed) {
					cycleamount ++
					gamepadUpPressed = true
				} else if (g1.dpad_down && !gamepadDownPressed) {
					if (cycleamount != 0) {
						cycleamount --
						gamepadDownPressed = true
					}
				} else if (!g1.dpad_up && !g1.dpad_down && (gamepadDownPressed || gamepadUpPressed)){
					gamepadDownPressed = false
					gamepadUpPressed = false
				}

				if (g1.b && !gamepadBPressed) {
					currentPage = MENU_PAGE.CYCLE_SELECT
					gamepadBPressed = true
				} else if (g1.a && !gamepadAPressed) {
					currentPage = MENU_PAGE.WAIT_SELECT
					gamepadAPressed = true
				} else if (!g1.a && !g1.b && (gamepadAPressed || gamepadBPressed)){
					gamepadAPressed = false
					gamepadBPressed = false
				}
			}
			MENU_PAGE.WAIT_SELECT -> {
				t.addLine("Wait time Select")
				t.addData(">", waittime)
				t.update()
				if(g1.dpad_up && !gamepadUpPressed) {
					waittime += 1000
					gamepadUpPressed = true
				} else if (g1.dpad_down && !gamepadDownPressed) {
					if (waittime != 0L) {
						waittime -= 1000
						gamepadDownPressed = true
					}
				} else if (!g1.dpad_up && !g1.dpad_down && (gamepadDownPressed || gamepadUpPressed)){
					gamepadDownPressed = false
					gamepadUpPressed = false
				}

				if (g1.b && !gamepadBPressed) {
					currentPage = MENU_PAGE.AMOUNT_SELECT
					gamepadBPressed = true
				} else if (g1.a && !gamepadAPressed) {
					currentPage = MENU_PAGE.CONFIRM
					gamepadAPressed = true
					ready = true
				} else if (!g1.a && !g1.b && (gamepadAPressed || gamepadBPressed)){
					gamepadAPressed = false
					gamepadBPressed = false
				}
			}
			MENU_PAGE.CONFIRM -> {
				if (g1.b && !gamepadBPressed) {
					currentPage = MENU_PAGE.WAIT_SELECT
					ready = false
					gamepadBPressed = true
				} else if (!g1.b && gamepadBPressed) {
					gamepadBPressed = false
				}
			}
		}
	}
}

enum class MENU_PAGE {
	ALLIANCE_SELECT,
	SIDE_SELECT,
	PATH_SELECT,
	CYCLE_SELECT,
	AMOUNT_SELECT,
	WAIT_SELECT,
	CONFIRM
}

enum class SIDE {
	LEFT, RIGHT
}

enum class PATH {
	MAIN, SUB
}

enum class CYCLE {
	STAGEDOOR, TRUSS
}