#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 1 of day 6: https://adventofcode.com/2021/day/6
 */

val file = File("day_6_input.txt")
val scanner = Scanner(file.inputStream())

var daysLeft = 80
val lanternFishes = scanner.nextLine().split(",").map { it.toInt() }.toMutableList()

while (daysLeft > 0) {
	val numOfFishes = lanternFishes.size

	for (i in 0 until numOfFishes) {
		val timer = lanternFishes[i] - 1
		if (timer < 0) {
			lanternFishes[i] = 6
			lanternFishes.add(8)
		} else {
			lanternFishes[i] = timer
		}
	}

	daysLeft--
}

println("Num of lantern fishes: ${lanternFishes.size}")
