#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 2 of day 8: https://adventofcode.com/2021/day/8
 */

val file = File("day_8_input.txt")
val scanner = Scanner(file.inputStream())

val SEGMENTS_FOR_NUMBER_1 = listOf(3, 6)
val NOT_SEGMENTS_FOR_NUMBER_1 = listOf(1, 2, 4, 5, 7)
val SEGMENTS_FOR_NUMBER_4 = listOf(1, 3, 4, 6)
val NOT_SEGMENTS_FOR_NUMBER_4 = listOf(2, 5, 7)
val SEGMENTS_FOR_NUMBER_7 = listOf(2, 3, 6)
val NOT_SEGMENTS_FOR_NUMBER_7 = listOf(1, 4, 5, 7)

var segmentsAlreadyCleared = mutableSetOf<Int>()
var candidateSegmentsToSignalWire = mutableMapOf<Int, MutableSet<Char>>()
var candidateSignalWireToSegments = mutableMapOf<Char, MutableList<Int>>()

var totalSum = 0


while (scanner.hasNext()) {
	candidateSegmentsToSignalWire = initCandidatesInfo()
	segmentsAlreadyCleared.clear()

	var numberOnePatter: String? = null
	var numberFourPatter: String? = null
	var numberSevenPatter: String? = null

	for (i in 1..10) {
		val pattern = scanner.next()

		when (pattern.length) {
			2 /* 1 */ -> numberOnePatter = pattern
			4 /* 4 */ -> numberFourPatter = pattern
			3 /* 7 */ -> numberSevenPatter = pattern
		}
	}

	numberSevenPatter?.parse(segments = SEGMENTS_FOR_NUMBER_7, notSegments = NOT_SEGMENTS_FOR_NUMBER_7)
	numberFourPatter?.parse(segments = SEGMENTS_FOR_NUMBER_4, notSegments = NOT_SEGMENTS_FOR_NUMBER_4)
	numberOnePatter?.parse(segments = SEGMENTS_FOR_NUMBER_1, notSegments = NOT_SEGMENTS_FOR_NUMBER_1)

	candidateSignalWireToSegments = initSignalWireToSegmentMap()

	scanner.next() // read separator

	var number = ""
	for (i in 1..4) {
		val digitPattern = scanner.next()

		number += when (digitPattern.length) {
			2 -> 1
			4 -> 4
			3 -> 7
			7 -> 8
			else -> { // 5 or 6
				digitPattern.patternWithLength5or6ToDigit()
			}
		}.toString()
	}

	totalSum += number.toInt()
}

println("Total sum is $totalSum")

fun String.patternWithLength5or6ToDigit(): Int {
	val segmentsToSignals = mutableMapOf<Int, MutableList<Char>>(
		1 to mutableListOf(),
		2 to mutableListOf(),
		3 to mutableListOf(),
		4 to mutableListOf(),
		5 to mutableListOf(),
		6 to mutableListOf(),
		7 to mutableListOf()
	)

	val pattern = this
	pattern.forEach { signalWire ->
		candidateSignalWireToSegments[signalWire]?.let { segments ->
			segments.forEach { segment ->
				segmentsToSignals[segment]?.add(signalWire)
			}
		}
	}

	val notConfirmedSegments = mutableSetOf<Int>()
	segmentsToSignals.forEach { entry ->
		val (segment, signals) = entry
		if (signals.size == 1 && candidateSegmentsToSignalWire[segment]?.size != 1) {
			notConfirmedSegments.add(segment)
		}
	}

	return when (notConfirmedSegments) {
		setOf(3, 5, 6, 7) -> 5
		setOf(1, 4, 5, 7) -> 3
		setOf(1, 3, 4, 6) -> 2
		setOf(1, 4) -> 0
		setOf(5, 7) -> 9
		else -> 6
	}
}

fun initCandidatesInfo() = mutableMapOf<Int, MutableSet<Char>>(
		1 to mutableSetOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
		2 to mutableSetOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
		3 to mutableSetOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
		4 to mutableSetOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
		5 to mutableSetOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
		6 to mutableSetOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
		7 to mutableSetOf('a', 'b', 'c', 'd', 'e', 'f', 'g')
	)

fun initSignalWireToSegmentMap(): MutableMap<Char, MutableList<Int>> {
	val map = mutableMapOf<Char, MutableList<Int>>(
		'a' to mutableListOf(),
		'b' to mutableListOf(),
		'c' to mutableListOf(),
		'd' to mutableListOf(),
		'e' to mutableListOf(),
		'f' to mutableListOf(),
		'g' to mutableListOf()
	)

	candidateSegmentsToSignalWire.forEach { entry ->
		val (segment, signalWires) = entry

		signalWires.forEach { signalWire ->
			map[signalWire]?.add(segment)
		}
	}

	return map
}

fun String.parse(segments: List<Int>, notSegments: List<Int>) {
	segments.forEach { segment ->
		candidateSegmentsToSignalWire[segment]?.clear()
		forEach { signalWire ->
			candidateSegmentsToSignalWire[segment]?.add(signalWire)
		}
	}

	notSegments.forEach { notSegment ->
		forEach { signalWire ->
			candidateSegmentsToSignalWire[notSegment]?.remove(signalWire)
		}
	}
}
