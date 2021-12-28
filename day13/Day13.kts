#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 1 of day 13: https://adventofcode.com/2021/day/13
 */

val file = File("day_13_input.txt")
val scanner = Scanner(file.inputStream())

data class Dot(val x: Int, val y: Int)
val dotCoordinates = mutableSetOf<Dot>()

var firstFoldInstructionRead = false

var maxX = 0
var maxY = 0

while (!firstFoldInstructionRead) {
	val inputRead = scanner.next()

	if (inputRead != "fold") {
		val rawDotCoordinates = inputRead.split(",")

		val x = rawDotCoordinates[0].toInt()
		if (x > maxX) {
			maxX = x
		}

		val y = rawDotCoordinates[1].toInt()
		if (y > maxY) {
			maxY = y
		}

		dotCoordinates.add(Dot(x, y))
	} else {
		firstFoldInstructionRead = true
	}
}

var width = maxX++
var height = maxY++

scanner.next() // read "along" keyword

val foldInfo = scanner.next().split("=")
val foldOrientation = foldInfo[0]
val foldPosition = foldInfo[1].toInt()

if (foldOrientation == "x") {
	foldHorizontally()
} else {
	foldVertically()
}

println("num of dots after first fold: ${dotCoordinates.size}")

fun foldHorizontally() {
	if (foldPosition < width / 2) {
		val dotsToBeFolded = dotCoordinates
			.asSequence()
			.filter { it.x < foldPosition }
			.toList()

		dotsToBeFolded.forEach { dot ->
			val offset = foldPosition - dot.x

			dotCoordinates.remove(dot)
			dotCoordinates.add(dot.copy(x = foldPosition + offset))
		}
	} else {
		val dotsToBeFolded = dotCoordinates
			.asSequence()
			.filter { it.x > foldPosition }
			.toList()

		dotsToBeFolded.forEach { dot ->
			val offset = dot.x - foldPosition

			dotCoordinates.remove(dot)
			dotCoordinates.add(dot.copy(x = foldPosition - offset))
		}
	}
}

fun foldVertically() {
	if (foldPosition < height / 2) {
		val dotsToBeFolded = dotCoordinates
			.asSequence()
			.filter { it.y < foldPosition }
			.toList()

		dotsToBeFolded.forEach { dot ->
			val offset = foldPosition - dot.y

			dotCoordinates.remove(dot)
			dotCoordinates.add(dot.copy(y = foldPosition + offset))
		}
	} else {
		val dotsToBeFolded = dotCoordinates
			.asSequence()
			.filter { it.y > foldPosition }
			.toList()

		dotsToBeFolded.forEach { dot ->
			val offset = dot.y - foldPosition

			dotCoordinates.remove(dot)
			dotCoordinates.add(dot.copy(y = foldPosition - offset))
		}
	}
}
