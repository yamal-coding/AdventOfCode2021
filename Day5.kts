#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 1 of day 5: https://adventofcode.com/2021/day/5
 */

val file = File("day_5_input.txt")
val scanner = Scanner(file.inputStream())

val map = mutableMapOf<Pair<Int, Int>, Int>()
var overlapedCoordinates = 0

while (scanner.hasNext()) {
	val (x1, y1) = readCoordinates()
	scanner.next() // read the arrow
	val (x2, y2) = readCoordinates()

	if (y1 == y2) {
		val xRange = if (x1 < x2) x1..x2 else x1 downTo x2
		for (x in xRange) {
			checkPositionInMap(x to y1)
		}
	} else if (x1 == x2) {
		val yRange = if (y1 < y2) y1..y2 else y1 downTo y2
		for (y in yRange) {
			checkPositionInMap(x1 to y)
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
