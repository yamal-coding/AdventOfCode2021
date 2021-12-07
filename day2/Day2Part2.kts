#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 2 of day 2: https://adventofcode.com/2021/day/2
 */

val file = File("day_2_input.txt")
val scanner = Scanner(file.inputStream())

var moreMeasurements = true
var x = 0
var y = 0
var aim = 0

while (moreMeasurements) {
	try {
		val direction = scanner.next()
		val move = scanner.nextInt()

		when (direction) {
			"forward" -> {
				x += move
				y += aim * move
			}
			"up" -> aim -= move
			"down" -> aim += move
		}
	} catch (e: NoSuchElementException) {
		moreMeasurements = false
	}
}

println("X: $x; Y: $y; X * Y: ${x * y}")
