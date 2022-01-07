#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 1 of day 1: https://adventofcode.com/2021/day/1
 */

val file = File("day_1_input.txt")
val scanner = Scanner(file.inputStream())

var lastMeasurement = -1
var moreMeasurements = true
var numOfLargerMeasurementsThatPreviousOne = -1

try {
	while (moreMeasurements) {
		try {
			val currentMeasurement = scanner.nextLine().toIntOrNull() ?: throw IllegalArgumentException()

			if (currentMeasurement > lastMeasurement) {
				numOfLargerMeasurementsThatPreviousOne++
			}

			lastMeasurement = currentMeasurement
		} catch (e: NoSuchElementException) {
			moreMeasurements = false
		}
	}

	println("Measurements larger than previous one: $numOfLargerMeasurementsThatPreviousOne")
} catch (e: IllegalArgumentException) {
	println("Invalid input")
}
