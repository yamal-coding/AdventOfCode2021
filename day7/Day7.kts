#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner
import kotlin.math.abs

/*
 * Part 1 of day 7: https://adventofcode.com/2021/day/7
 */

val file = File("day_7_input.txt")
val scanner = Scanner(file.inputStream())

val horizontalPositions = scanner.nextLine().split(",").map { it.toInt() }
val maxPosition = horizontalPositions.maxOrNull() ?: throw IllegalArgumentException("no max horizontal position")
var consumedFuelPerPosition = mutableListOf<Int>()

horizontalPositions.forEach { crabPosition ->
	for (candidatePosition in 0..maxPosition) {
		val consumedFuel = abs(crabPosition - candidatePosition)
		if (consumedFuelPerPosition.size > candidatePosition) {
			consumedFuelPerPosition[candidatePosition] += consumedFuel
		} else {
			consumedFuelPerPosition.add(consumedFuel)
		}
	}
}

var minConsumedFuel = Int.MAX_VALUE
var indexOfMinValue = -1
consumedFuelPerPosition.forEachIndexed { position, consumedFuel ->
	if (consumedFuel < minConsumedFuel) {
		indexOfMinValue = position
		minConsumedFuel = consumedFuel
	}
}

println("Less fuel consumed ($minConsumedFuel) in position $indexOfMinValue")
