#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 2 of day 4: https://adventofcode.com/2021/day/4
 */

val file = File("day_4_input.txt")
val scanner = Scanner(file.inputStream())

val randomNumbers = scanner.nextLine().split(",")
val rowsCounters = arrayOf(0, 0, 0, 0, 0, 0)
val columnsCounters = arrayOf(0, 0, 0, 0, 0, 0)

var maxStepsToWin = Int.MIN_VALUE
var lastWinningBoardScore = 0
var numberToWin = 0

while (scanner.hasNext()) {
	analyzeNextBoard()
	resetCounters()
}

println("Board with score $lastWinningBoardScore in $maxStepsToWin steps at number $numberToWin. Score * winning number: ${lastWinningBoardScore * numberToWin}")

fun resetCounters() {
	for (i in 0..4) {
		rowsCounters[i] = 0
		columnsCounters[i] = 0
	}
}

fun analyzeNextBoard() {
	var steps = 1
	val cellCoordinates = mutableMapOf<String, Pair<Int, Int>>()
	val unmarkedCells = mutableSetOf<String>()
	var hasWon = false
	var candidateNumberToWin = 0

	for (row in 0..4) {
		for (column in 0..4) {
			val cell = scanner.next()
			cellCoordinates[cell] = row to column
			unmarkedCells.add(cell)
		}
	}

	while (steps < randomNumbers.size && !hasWon) {
		val randomNumber = randomNumbers[steps - 1]

		val coordinates = cellCoordinates[randomNumber]
		if (coordinates != null) {
			val (row, column) = coordinates
			rowsCounters[row] += 1
			columnsCounters[column] += 1
			unmarkedCells.remove(randomNumber)

			if (rowsCounters[row] == 5 || columnsCounters[column] == 5) {
				hasWon = true
				candidateNumberToWin = randomNumber.toInt()
			}
		}

		steps++
	}

	if (hasWon && steps > maxStepsToWin) {
		numberToWin = candidateNumberToWin
		maxStepsToWin = steps
		lastWinningBoardScore = unmarkedCells.sumOf { it.toInt() }
	}
}
