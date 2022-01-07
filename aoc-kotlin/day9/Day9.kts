#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 1 of day 9: https://adventofcode.com/2021/day/9
 */

val file = File("day_9_input.txt")
val scanner = Scanner(file.inputStream())

var numOfColumns = -1
val heightsMap = mutableListOf<MutableList<Int>>()

while (scanner.hasNext()) {
	val row = mutableListOf<Int>()
	scanner.nextLine().forEach { height ->
		row.add(height.toString().toInt())
	}
	if (numOfColumns < 0) {
		numOfColumns = row.size
	}
	heightsMap.add(row)
}

var totalRiskLevel = 0

val numberOfRows = heightsMap.size
for (row in 0 until numberOfRows) {
	for (column in 0 until numOfColumns) {
		var isLowPoint = true
		val currentHeight = heightsMap[row][column]
		if (column - 1 >= 0 && currentHeight >= heightsMap[row][column - 1]) {
			isLowPoint = false
		}
		if (column + 1 < numOfColumns && currentHeight >= heightsMap[row][column + 1]) {
			isLowPoint = false
		}
		if (row - 1 >= 0 && currentHeight >= heightsMap[row - 1][column]) {
			isLowPoint = false
		}
		if (row + 1 < numberOfRows && currentHeight >= heightsMap[row + 1][column]) {
			isLowPoint = false
		}

		if (isLowPoint) {
			totalRiskLevel += 1 + currentHeight
		}
	}
}

println("Total risk level is $totalRiskLevel")
