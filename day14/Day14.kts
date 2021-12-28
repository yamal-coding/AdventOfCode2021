#!/usr/bin/env kotlin

import java.io.File
import java.util.LinkedList
import java.util.Scanner

/*
 * Part 1 of day 14: https://adventofcode.com/2021/day/14
 */

val file = File("day_14_input.txt")
val scanner = Scanner(file.inputStream())

val rules = mutableMapOf<String, String>()

val template = scanner.next()
val polymer = LinkedList<String>()
val counters = mutableMapOf<String, Int>()
template.forEach {
	val element = it.toString()
	polymer.add(element)
	counters[element] = (counters[element] ?: 0) + 1
}

while (scanner.hasNext()) {
	val rulePattern = scanner.next()
	scanner.next() // read "->" token
	val ruleElement = scanner.next()

	rules[rulePattern] = ruleElement
}

val steps = 10
for (step in 1..steps) {
	val iterator = polymer.listIterator()
    while (iterator.hasNext()) {
		val firstElement = iterator.next()
		if (iterator.hasNext()) {
			val secondElement = iterator.next()
			val pattern = firstElement + secondElement

			rules[pattern]?.let { newElement ->
				iterator.previous()
				iterator.add(newElement)
				counters[newElement] = (counters[newElement] ?: 0) + 1
			}
		}
	}
}

val maxRepetitions = counters.maxByOrNull { it.value }!!.value
val minRepetitions = counters.minByOrNull { it.value }!!.value

println("Repetitions of most common element minus repetitions of least common element: $maxRepetitions - $minRepetitions = ${maxRepetitions - minRepetitions}")
