#!/usr/bin/env kotlin

import java.io.File
import java.util.LinkedList
import java.util.Scanner
import kotlin.text.single

/*
 * Day 14: https://adventofcode.com/2021/day/14
 * Code for day 14 is the same for part 1 and part 2.
 */

val file = File("day_14_input.txt")
val scanner = Scanner(file.inputStream())

val rules = mutableMapOf<String, Char>()

val template = scanner.next().map { it }
val globalCounters = mutableMapOf<Char, Long>()
val alreadyCalculatedTuplesWithCounter = mutableMapOf<Pair<Int, String>, Map<Char, Long>>()
template.forEach { element ->
	globalCounters[element] = (globalCounters[element] ?: 0L) + 1L
}

while (scanner.hasNext()) {
	val rulePattern = scanner.next()
	scanner.next() // read "->" token
	val ruleElement = scanner.next().single()

	rules[rulePattern] = ruleElement
}

val steps = 40
for (i in 0 until template.size - 1) {
	val firstElement = template[i]
	val secondElement = template[i + 1]

	val counters = generatePolymerFromTuple(steps, firstElement, secondElement)

	increaseCounter(globalCounters, counters)
}

fun generatePolymerFromTuple(step: Int, firstElement: Char, secondElement: Char): Map<Char, Long> {
	if (step == 0) {
		return emptyMap()
	}

	val counters = mutableMapOf<Char, Long>()

	val pattern = "$firstElement$secondElement"
	rules[pattern]?.let { newElement ->
		counters[newElement] = (counters[newElement] ?: 0L) + 1L

		val firstPattern = "$firstElement$newElement"
		if (alreadyCalculatedTuplesWithCounter[step - 1 to firstPattern] == null) {
			val tempCounters = generatePolymerFromTuple(step - 1, firstElement, newElement)
			increaseCounter(counters, tempCounters)
		} else {
			alreadyCalculatedTuplesWithCounter[step - 1 to firstPattern]?.let { alreadyCalculatedCounters ->
				increaseCounter(counters, alreadyCalculatedCounters)
			}
		}

		val secondPattern = "$newElement$secondElement"
		if (alreadyCalculatedTuplesWithCounter[step - 1 to secondPattern] == null) {
			val tempCounters = generatePolymerFromTuple(step - 1, newElement, secondElement)
			increaseCounter(counters, tempCounters)
		} else {
			alreadyCalculatedTuplesWithCounter[step - 1 to secondPattern]?.let { alreadyCalculatedCounters ->
				increaseCounter(counters, alreadyCalculatedCounters)
			}
		}

		alreadyCalculatedTuplesWithCounter[step to pattern] = counters.toMutableMap()
	}

	return counters
}

fun increaseCounter(base: MutableMap<Char, Long>, increase: Map<Char, Long>) {
	increase.forEach {
		base[it.key] = (base[it.key] ?: 0L) + it.value
	}
}

val maxRepetitions = globalCounters.maxByOrNull { it.value }!!.value
val minRepetitions = globalCounters.minByOrNull { it.value }!!.value

println("Repetitions of most common element minus repetitions of least common element: $maxRepetitions - $minRepetitions = ${maxRepetitions - minRepetitions}")
