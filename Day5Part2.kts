#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 2 of day 5: https://adventofcode.com/2021/day/5
 */

val file = File("day_5_input.txt")
val scanner = Scanner(file.inputStream())

val map = mutableMapOf<Pair<Int, Int>, Int>()
var overlapedCoordinates = 0

while (scanner.hasNext()) {
	var (x1, y1) = readCoordinates()
	scanner.next() // read the arrow
	var (x2, y2) = readCoordinates()

	val xCondition = if (x1 < x2) { { x1 <= x2 } } else { { x1 >= x2 } }
	val updateX1 = if (x1 < x2) { { x1++ } } else { { x1-- } }
	val yCondition = if (y1 < y2) { { y1 <= y2 } } else { { y1 >= y2 } }
	val updateY1 = if (y1 < y2) { { y1++ } } else { { y1-- } }

	var stop = false
	while (!stop && (xCondition() || yCondition())) {
		if (x1 == x2 && y1 == y2) {
			stop = true
		}

		checkPositionInMap(x1 to y1)

		if (x1 != x2) {
			updateX1()
		}
		if (y1 != y2) {
			updateY1()
		}
	}
}

println("Points overlaped by at least two segments: $overlapedCoordinates")

fun checkPositionInMap(coordinate: Pair<Int, Int>) {
	val positionInMap = map[coordinate]
	if (positionInMap != null) {
		if (positionInMap == 1) {
			map[coordinate] = 2
			overlapedCoordinates++
		}
	} else {
		map[coordinate] = 1
	}
}

fun readCoordinates(): Pair<Int, Int> {
	val coordinates = scanner.next().split(",")
	return coordinates[0].toInt() to coordinates[1].toInt()
}
