#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner
import kotlin.math.abs

/*
 * Part 2 of day 17: https://adventofcode.com/2021/day/17
 */

val file = File("day_17_input.txt")
val scanner = Scanner(file.inputStream())

scanner.next() // "target" token
scanner.next() // "area:" token

val xRange = scanner.next().split("=")[1].let {
	it.substring(0, it.length - 1).split("..")
}
val nearestX = xRange[0].toInt()
val farthestX = xRange[1].toInt()

val yRange = scanner.next().split("=")[1].split("..")
val deepestY = yRange[0].toInt()
val leastDeepY = yRange[1].toInt()

println("x=$nearestX..$farthestX y=$deepestY..$leastDeepY")

// possiblepositive initial y velocity
var initialPositiveYVelocity = 0
var validYVelocities = mutableListOf<Int>()
while (initialPositiveYVelocity < abs(deepestY)) {
	var y = 0
	var yVelocity = -initialPositiveYVelocity

	do {
		yVelocity--
		y += yVelocity
	} while (y > leastDeepY)

	if (y >= deepestY) {
		validYVelocities.add(initialPositiveYVelocity)
	}
	initialPositiveYVelocity++
}

// possible negative initial y velocity
var initialNegativeYVelocity = -1
while (initialNegativeYVelocity >= deepestY) {
	var y = 0
	var yVelocity = initialNegativeYVelocity

	do {
		y += yVelocity
		yVelocity--
	} while (y > leastDeepY)

	if (y >= deepestY) {
		validYVelocities.add(initialNegativeYVelocity)
	}
	initialNegativeYVelocity--
}

// possible initial x velocities
var initialXVelocity = 1
var validXVelocities = mutableListOf<Int>()
while (initialXVelocity <= abs(farthestX)) {
	var x = 0
	var xVelocity = initialXVelocity

	do {
		x += xVelocity
		xVelocity--
	} while (x < nearestX || xVelocity == 0)

	if (x in nearestX..farthestX) {
		validXVelocities.add(initialXVelocity)
	}
	initialXVelocity++
}

var validVelocities = 0
validXVelocities.forEach { initialVX ->
	validYVelocities.forEach { initialVY ->
		var vx = initialVX
		var vy = initialVY
		var x = 0
		var y = 0

		do {
			x += vx
			if (vx > 0) {
				vx--
			}
			y += vy--
		} while (x < nearestX || y > leastDeepY)

		if (x in nearestX..farthestX && y in deepestY..leastDeepY) {
			validVelocities++
		}
	}
}

println("Num of initial velocities that reach specific area: $validVelocities")
