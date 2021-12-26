#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 2 of day 11: https://adventofcode.com/2021/day/11
 */

val file = File("day_11_input.txt")
val scanner = Scanner(file.inputStream())

val octopuses = mutableListOf<MutableList<Int>>()
val WIDTH = 10
val HEIGHT = 10

while (scanner.hasNext()) {
	val rowOfOctopuses = scanner.nextLine()
	val row = mutableListOf<Int>()
	rowOfOctopuses.forEach { octopus ->
		row.add(Character.getNumericValue(octopus))
	}
	octopuses.add(row)
}

val octopusesAlreadyFlashed = mutableSetOf<Pair<Int, Int>>()
var numOfFlashes = 0
var syncStepFound = false
var numOfFlashesInASingleStep = 0
var step = 1

while (!syncStepFound) {
	for (row in 0 until HEIGHT) {
		for (column in 0 until WIDTH) {
			val currentOctopusValue = octopuses[row][column]

			if (!octopusesAlreadyFlashed.contains(row to column)) {
				val newValue = currentOctopusValue + 1

				if (newValue <= 9) {
					octopuses[row][column] = newValue
				} else {
					flash(row, column)
				}
			}
		}
	}
	octopusesAlreadyFlashed.clear()
	if (numOfFlashesInASingleStep == HEIGHT * WIDTH) {
		syncStepFound = true
	} else {
		numOfFlashesInASingleStep = 0
		step++
	}
}

println("Dumbo octopuses synchornized at step $step")

fun flash(row: Int, column: Int) {
	numOfFlashesInASingleStep++
	numOfFlashes++
	octopuses[row][column] = 0
	octopusesAlreadyFlashed.add(row to column)

	checkAdjacentCell(row - 1, column)
	checkAdjacentCell(row + 1, column)
	checkAdjacentCell(row, column - 1)
	checkAdjacentCell(row, column + 1)
	checkAdjacentCell(row - 1, column - 1)
	checkAdjacentCell(row - 1, column + 1)
	checkAdjacentCell(row + 1, column - 1)
	checkAdjacentCell(row + 1, column + 1)
}

fun checkAdjacentCell(row: Int, column: Int) {
	if (row in 0 until HEIGHT && column in 0 until WIDTH) {
		val adjacentValue = octopuses[row][column]
		if (adjacentValue != 0 || !octopusesAlreadyFlashed.contains(row to column)) {
			if (adjacentValue + 1 > 9) {
				flash(row, column)
			} else {
				octopuses[row][column] = adjacentValue + 1
			}
		}
	}
}
