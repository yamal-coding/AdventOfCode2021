#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Day 6: https://adventofcode.com/2021/day/6
 * Code for day 6 is the same for part 1 and part 2. 
 * This problem was solved using dynamic programming where I'm caching
 * already processed fishes with a specific timer for a specific numer of remaining days.
 */

val file = File("day_6_input.txt")
val scanner = Scanner(file.inputStream())

var DAYS = 256
val lanternFishes = scanner.nextLine().split(",").map { it.toInt() }
var total = 0L
val childrenOfAlreadyProcessedFishes = mutableMapOf<Pair<Int, Int>, Long>()

lanternFishes.forEach { initialTimer ->
	total += 1 + calculateChildren(DAYS, initialTimer)
}

fun calculateChildren(remainingDays: Int, timer: Int): Long {
	val childrenOfAlreadyProcessedFish = childrenOfAlreadyProcessedFishes[timer to remainingDays]
	if (childrenOfAlreadyProcessedFish != null) {
		return childrenOfAlreadyProcessedFish
	}

	var children = 0L

	if (remainingDays > timer) {
		children = 1L + ((remainingDays - timer - 1) / 7)
	}

	for (i in 0L until children) {
		children += calculateChildren(remainingDays - timer - i.toInt() * 7, 9)
	}

	childrenOfAlreadyProcessedFishes[timer to remainingDays] = children

	return children
}

println("Num of lantern fishes: $total")
