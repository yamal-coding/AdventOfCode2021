#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner
import kotlin.math.abs

/*
 * Part 1 of day 17: https://adventofcode.com/2021/day/17
 */

val file = File("day_17_input.txt")
val scanner = Scanner(file.inputStream())

scanner.next() // "target" token
scanner.next() // "area:" token
scanner.next() // x range

val yRange = scanner.next().split("=")[1].split("..")
val deepestY = yRange[0].toInt()

val initialVelocity = abs(deepestY) - 1
var yVelocity = initialVelocity
var highestY = 0
do {
	highestY += yVelocity
	yVelocity--
} while (yVelocity > 0)

println("Highest y reached on the trajectory is $highestY with y velocity $initialVelocity")
