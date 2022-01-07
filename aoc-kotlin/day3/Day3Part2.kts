#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner
import kotlin.math.pow

/*
 * Part 2 of day 3: https://adventofcode.com/2021/day/3
 */

val file = File("day_3_input.txt")
val scanner = Scanner(file.inputStream())

var oxygenGeneratorRatingCandidates = listOf<String>()
var co2ScrubberRatingCandidates = listOf<String>()
var binaryValueSize = 0

iterateWith(
	iteration = 0,
	iterator = { block ->
		while (scanner.hasNext()) {
			val binaryValue = scanner.next()
			if (binaryValueSize == 0) {
				binaryValueSize = binaryValue.length
			}
			block(binaryValue)
		}
	},
	result = { withBitOneInCurrentPosition, withBitZeroInCurrentPosition ->
		updateOxygenGeneratorRatingCandidates(withBitOneInCurrentPosition, withBitZeroInCurrentPosition)
		updateCo2ScrubberRatingCandidates(withBitOneInCurrentPosition, withBitZeroInCurrentPosition)
	}
)

iterateRating(candidates = { oxygenGeneratorRatingCandidates }) { withBitOneInCurrentPosition, withBitZeroInCurrentPosition ->
	updateOxygenGeneratorRatingCandidates(withBitOneInCurrentPosition, withBitZeroInCurrentPosition)
}

iterateRating(candidates = { co2ScrubberRatingCandidates }) { withBitOneInCurrentPosition, withBitZeroInCurrentPosition ->
	updateCo2ScrubberRatingCandidates(withBitOneInCurrentPosition, withBitZeroInCurrentPosition)
}

val oxygenGeneratorRating = oxygenGeneratorRatingCandidates[0].toDecimal()
val co2ScrubberRating = co2ScrubberRatingCandidates[0].toDecimal()

println("Oxygen Generator Rating: $oxygenGeneratorRating; CO2 Scrubber Rating: $co2ScrubberRating; Oxygen*CO2: ${oxygenGeneratorRating * co2ScrubberRating}")

fun updateOxygenGeneratorRatingCandidates(withBitOneInCurrentPosition: List<String>, withBitZeroInCurrentPosition: List<String>) {
	oxygenGeneratorRatingCandidates = if (withBitOneInCurrentPosition.size >= withBitZeroInCurrentPosition.size) {
		withBitOneInCurrentPosition
	} else {
		withBitZeroInCurrentPosition
	}
}

fun updateCo2ScrubberRatingCandidates(withBitOneInCurrentPosition: List<String>, withBitZeroInCurrentPosition: List<String>) {
	co2ScrubberRatingCandidates = if (withBitOneInCurrentPosition.size < withBitZeroInCurrentPosition.size) {
		withBitOneInCurrentPosition
	} else {
		withBitZeroInCurrentPosition
	}
}

fun iterateRating(
	candidates: () -> List<String>,
	result: (List<String>, List<String>) -> Unit
) {
	var i = 1
	while (i < binaryValueSize && candidates().size > 1) {
		iterateWith(
			iteration = i,
			iterator = { block ->
				candidates().forEach { block(it) }
			},
			result = result
		)
		i++
	}
}

fun iterateWith(
	iteration: Int,
	iterator: ((String) -> Unit) -> Unit,
	result: (List<String>, List<String>) -> Unit
) {
	val withBitOneInCurrentPosition = arrayListOf<String>()
	val withBitZeroInCurrentPosition = arrayListOf<String>()

	iterator { binaryValue ->
		when (binaryValue[iteration]) {
			'1' -> withBitOneInCurrentPosition.add(binaryValue)
			'0' -> withBitZeroInCurrentPosition.add(binaryValue)
		}
	}

	result(withBitOneInCurrentPosition, withBitZeroInCurrentPosition)
}

fun String.toDecimal(): Int {
	var decimalValue = 0
	var bitPow = this.length - 1
	for (i in 0 until this.length) {
		if (this[i] == '1') {
			decimalValue += 2.0.pow(bitPow).toInt()
		}
		bitPow--
	}

	return decimalValue
}
