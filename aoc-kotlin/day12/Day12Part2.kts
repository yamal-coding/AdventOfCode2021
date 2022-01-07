#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 2 of day 12: https://adventofcode.com/2021/day/12
 */

val file = File("day_12_input.txt")
val scanner = Scanner(file.inputStream())

val caveGraph = mutableMapOf<String, MutableList<String>>()

while (scanner.hasNext()) {
	val segment = scanner.nextLine().split("-")
	val start = segment[0]
	val end = segment[1]

	if (end != "start" && start != "end") {
		if (caveGraph[start] == null) {
			caveGraph[start] = mutableListOf(end)
		} else {
			caveGraph[start]?.add(end)
		}
	}

	if (end != "end" && start != "start") {
		if (caveGraph[end] == null) {
			caveGraph[end] = mutableListOf(start)
		} else {
			caveGraph[end]?.add(start)
		}
	}
}

fun findPaths(currentCave: String, timesVisitedASmallCave: MutableMap<String, Int>, smallCaveVisitedTwice: Boolean): Int {

	fun markCurrentCaveAsVisited() {
		if (!currentCave.isABigCave()) {
				timesVisitedASmallCave[currentCave] = (timesVisitedASmallCave[currentCave] ?: 0) + 1
		}
	}

	fun unmarkCurrentCaveAsVisited() {
		if (!currentCave.isABigCave()) {
			timesVisitedASmallCave[currentCave] = (timesVisitedASmallCave[currentCave] ?: 1) - 1
		}
	}

	var paths = 0

	caveGraph[currentCave]?.forEach { adjacentCave ->
		when {
			adjacentCave.isEnd() -> paths += 1
			(timesVisitedASmallCave[adjacentCave] ?: 0) == 0 -> {
				markCurrentCaveAsVisited()

				paths += findPaths(adjacentCave, timesVisitedASmallCave, smallCaveVisitedTwice)

				unmarkCurrentCaveAsVisited()
			}
			timesVisitedASmallCave[adjacentCave] == 1 && !smallCaveVisitedTwice -> {
				markCurrentCaveAsVisited()

				paths += findPaths(adjacentCave, timesVisitedASmallCave, smallCaveVisitedTwice = true)

				unmarkCurrentCaveAsVisited()
			}
		}
	}

	return paths
}

fun String.isABigCave(): Boolean =
	this[0].isUpperCase()

fun String.isEnd(): Boolean =
	this ==  "end"

println("Num of paths: ${findPaths("start", mutableMapOf(), false)}")
