#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 1 of day 12: https://adventofcode.com/2021/day/12
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

fun findPaths(currentCave: String, alreadyVisitedCaves: MutableSet<String>): Int {
	var paths = 0

	caveGraph[currentCave]?.forEach { adjacentCave ->
		when {
			adjacentCave.isEnd() -> paths += 1
			!alreadyVisitedCaves.contains(adjacentCave) -> {
				if (!currentCave.isABigCave()) {
					alreadyVisitedCaves.add(currentCave)
				}
				paths += findPaths(adjacentCave, alreadyVisitedCaves)
				if (!currentCave.isABigCave()) {
					alreadyVisitedCaves.remove(currentCave)
				}
			}
		}
	}

	return paths
}


fun String.isABigCave(): Boolean =
	this[0].isUpperCase()

fun String.isEnd(): Boolean =
	this ==  "end"

println("Num of paths: ${findPaths("start", mutableSetOf())}")
