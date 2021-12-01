#!/usr/bin/env kotlin

import java.io.File
import java.util.LinkedList
import java.util.Scanner

/*
 * Part 2 of day 1: https://adventofcode.com/2021/day/1
 */

val file = File("day_1_input.txt")
val scanner = Scanner(file.inputStream())

val WINDOW_SIZE = 3

val windows = LinkedList<Int>()
for (i in 1..WINDOW_SIZE) {
    windows.add(0)
}

var windowIteration = 1
var moreMeasurements = true
var numOfLargerMeasurementsThatPreviousOne = 0
var lastWindowMeasurement: Int? = null

try {
    while (moreMeasurements) {
        try {
            val currentMeasurement = scanner.nextLine().toIntOrNull() ?: throw IllegalArgumentException()

            val iterator = windows.listIterator()
            var i = 0
            while (i < windowIteration && iterator.hasNext()) {
                iterator.set(iterator.next() + currentMeasurement)
                i++
            }


	    lastWindowMeasurement?.let {
                if (windows.first > it) {
                    numOfLargerMeasurementsThatPreviousOne++
                }
                lastWindowMeasurement = null
            }

            if (windowIteration == WINDOW_SIZE) {
            	lastWindowMeasurement = windows.first
                windows.removeFirst()
                windows.add(0)
            } else {
                windowIteration++
            }
        } catch (e: NoSuchElementException) {
            moreMeasurements = false
        }
    }

    println("Measurements larger than previous one: $numOfLargerMeasurementsThatPreviousOne")
} catch (e: IllegalArgumentException) {
    println("Invalid input")
}
