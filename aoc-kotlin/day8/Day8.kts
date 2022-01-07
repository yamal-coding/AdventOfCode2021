#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 1 of day 8: https://adventofcode.com/2021/day/8
 */

val file = File("day_8_input.txt")
val scanner = Scanner(file.inputStream())
var digitsWithUniqueNumberOfSegmens = 0

while (scanner.hasNext()) {
	for (i in 1..10) {
		scanner.next()
	}
	
	scanner.next() // read delimiter

	for (i in 1..4) {
		val output = scanner.next()
		when (output.length) {
			2, // 1
			4, // 4
			3, // 7
			7, // 8
			-> digitsWithUniqueNumberOfSegmens++
		}
	}
}

println("Digits with unique number of segments: $digitsWithUniqueNumberOfSegmens")
