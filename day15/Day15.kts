#!/usr/bin/env kotlin

import java.io.File
import java.util.LinkedList
import java.util.Scanner

/*
 * Part 1 of day 15: https://adventofcode.com/2021/day/15
 * Solved with A* algorithm :) (https://en.wikipedia.org/wiki/A*_search_algorithm)
 */

val file = File("day_15_input.txt")
val scanner = Scanner(file.inputStream())

val cavernMap = mutableListOf<List<Int>>()

while (scanner.hasNext()) {
	val cavernRow = scanner.nextLine().map {
		Character.getNumericValue(it)
	}
	cavernMap.add(cavernRow)
}

val WIDTH = cavernMap[0].size
val HEIGHT = cavernMap.size
val EXIT_X = WIDTH - 1
val EXIT_Y = HEIGHT - 1
val EXIT_COORDINATES = Coordinates(x = EXIT_X, y = EXIT_Y)


val cameFrom = mutableMapOf<Coordinates, Coordinates>()
val openPaths = LinkedList<Coordinates>()
val openPathsSet = mutableSetOf<Coordinates>()
val riskToCoordinates = mutableMapOf<Coordinates, Int>()
val estimatedRiskToExit = mutableMapOf<Coordinates, Int>()

val START_COORDINATES = Coordinates(x = 0, y = 0)
estimatedRiskToExit[START_COORDINATES] = estimateRiskToExit(START_COORDINATES)
insertOpenPath(START_COORDINATES)
riskToCoordinates[START_COORDINATES] = 0

var exitReached = false
while (!openPaths.isEmpty() && !exitReached) {
	val currentOpenPath = openPaths.pop()

	if (currentOpenPath == EXIT_COORDINATES) {
		exitReached = true
	} else {
		val x = currentOpenPath.x
		val y = currentOpenPath.y

		exploreNeighbour(currentOpenPath, x + 1, y)
		exploreNeighbour(currentOpenPath, x, y + 1)
		exploreNeighbour(currentOpenPath, x, y - 1)
		exploreNeighbour(currentOpenPath, x - 1, y)
	}
}

fun exploreNeighbour(previousCoordinates: Coordinates, x: Int, y: Int) {
	if (y in 0 until HEIGHT && x in 0 until WIDTH) {
		val accumulatedRisk = riskToCoordinates[previousCoordinates] ?: Int.MAX_VALUE
		val neighbourCoordinates = Coordinates(x = x, y = y)
		val estimatedRiskToExitFromNeighbour = accumulatedRisk + cavernMap[y][x]

		if (estimatedRiskToExitFromNeighbour < (riskToCoordinates[neighbourCoordinates] ?: Int.MAX_VALUE)) {
			cameFrom[neighbourCoordinates] = previousCoordinates
			riskToCoordinates[neighbourCoordinates] = estimatedRiskToExitFromNeighbour
			estimatedRiskToExit[neighbourCoordinates] = estimatedRiskToExitFromNeighbour + estimateRiskToExit(neighbourCoordinates)

			if (!openPathsSet.contains(neighbourCoordinates)) {
				insertOpenPath(neighbourCoordinates)
			}
		}
	}
}

println("Lowest risk found is ${calculateLowestRisk()}")

data class Coordinates(val x: Int, val y: Int)

fun insertOpenPath(coordinates: Coordinates) {
	val iterator = openPaths.listIterator()
	var inserted = false
	while (iterator.hasNext() && !inserted) {
		if ((estimatedRiskToExit[coordinates] ?: Int.MAX_VALUE) < (estimatedRiskToExit[iterator.next()] ?: Int.MAX_VALUE)) {
			if (iterator.hasPrevious()) {
				iterator.previous()
				iterator.add(coordinates)
			} else {
				iterator.add(coordinates)
			}
			inserted = true
		}
	}

	if (!inserted) {
		openPaths.addLast(coordinates)
		openPathsSet.add(coordinates)
	}
}

fun calculateLowestRisk(): Int {
	var risk = cavernMap[EXIT_Y][EXIT_X]

	var previousPoint = cameFrom[Coordinates(x = EXIT_X, y = EXIT_Y)]
	while (previousPoint != null && previousPoint != START_COORDINATES) {
		risk += cavernMap[previousPoint.y][previousPoint.x]

		previousPoint = cameFrom[previousPoint]
	}

	return risk
}

fun estimateRiskToExit(coordinates: Coordinates): Int =
	(EXIT_X - coordinates.x) + (EXIT_Y - coordinates.y)


