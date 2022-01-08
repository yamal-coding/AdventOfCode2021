#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 1 of day 2: https://adventofcode.com/2021/day/2
 */

val file = File("day_2_input.txt")
val scanner = Scanner(file.inputStream())

var x = 0
var y = 0

while (scanner.hasNext()) {
	val direction = scanner.next()
	val move = scanner.nextInt()

	when (direction) {
		"forward" -> x += move
		"up" -> y -= move
		"down" -> y += move
	}
}

println("X: $x; Y: $y; X * Y: ${x * y}")
