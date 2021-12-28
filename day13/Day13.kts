#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Dday 13: https://adventofcode.com/2021/day/13
 * Code for day 13 is the same for part 1 and part 2.
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

var keepFolding = true

do {
	scanner.next() // read "along" token

	val foldInfo = scanner.next().split("=")
	val foldOrientation = foldInfo[0]
	val foldPosition = foldInfo[1].toInt()

	if (foldOrientation == "x") {
		foldHorizontally(foldPosition)
	} else {
		foldVertically(foldPosition)
	}

	println("Number of dots after step is ${dotCoordinates.size}")

	if (scanner.hasNext()) {
		scanner.next() // read "fold" token
	} else {
		keepFolding = false
	}
} while (keepFolding)

// The content of the sheet (the shapes drawn by the dots) tells you the solution
// 8 capital letters
printSheet()

fun foldHorizontally(foldPosition: Int) {
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
		width -= foldPosition
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
		width = foldPosition
	}
	val dotsInFold = dotCoordinates
			.asSequence()
			.filter { it.x == foldPosition }
			.toList()
	dotsInFold.forEach { dot ->
		dotCoordinates.remove(dot)
	}
}

fun foldVertically(foldPosition: Int) {
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
		height -= foldPosition
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
		height = foldPosition
	}

	val dotsInFold = dotCoordinates
			.asSequence()
			.filter { it.y == foldPosition }
			.toList()
	dotsInFold.forEach { dot ->
		dotCoordinates.remove(dot)
	}
}

fun printSheet() {
	for (y in 0 until height) {
		for (x in 0 until width) {
			if (dotCoordinates.contains(Dot(x, y))) {
				print("#")
			} else {
				print(".")
			}
		}
		println()
	}
}
