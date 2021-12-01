#!/usr/bin/env kotlin

import java.io.File
import java.io.InputStreamReader
import java.util.Scanner


val file = File("day_1_input.txt")
val scanner = Scanner(file.inputStream())

var lastMeasurement = -1
var moreMeasurements = true
var numOfLargerMeasurementsThatPreviousOne = -1

try {
	while (moreMeasurements) {
		try {
			var currentMeasurement = scanner.nextLine().toIntOrNull() ?: throw IllegalArgumentException()

			if (currentMeasurement > lastMeasurement) {
				numOfLargerMeasurementsThatPreviousOne++
			}

			lastMeasurement = currentMeasurement
		} catch (e: NoSuchElementException) {
			moreMeasurements = false
		}
	}

	println("Measuremnts larger than previous one: $numOfLargerMeasurementsThatPreviousOne")
} catch (e: IllegalArgumentException) {
	println("Invalid input")
}
