#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner
import kotlin.math.pow

/*
 * Part 2 of day 16: https://adventofcode.com/2021/day/16
 */

val file = File("day_16_input.txt")
val scanner = Scanner(file.inputStream())

val input = scanner.nextLine().map { it }.flatMap { it.toBinary() }

var i = 0

println("Package value is ${getPackageValue()}")

fun getPackageValue(): Long {
	// skip version bits
	i += 3

	val typeId = listOf(input[i++], input[i++], input[i++]).toDecimal().toInt()

	return when (typeId) {
		0 -> executeOperator { values -> values.sum() }
		1 -> executeOperator { values -> values.reduce { acc, next -> acc * next } }
		2 -> executeOperator { values -> values.minOrNull() ?: throw IllegalStateException("There should be at least one value") }
		3 -> executeOperator { values -> values.maxOrNull() ?: throw IllegalStateException("There should be at least one value") }
		4 -> calculateLiteral()
		5 -> executeOperator { values -> if (values[0] > values[1]) 1L else 0L }
		6 -> executeOperator { values -> if (values[0] < values[1]) 1L else 0L }
		7 -> executeOperator { values -> if (values[0] == values[1]) 1L else 0L }
		else -> throw IllegalArgumentException("Invalid type ID")
	}
}

fun calculateLiteral(): Long {
	val binaryNumberBits = mutableListOf<Int>()
	do {
		val nextBit = input[i++]
		repeat(4) {
			binaryNumberBits.add(input[i++])
		}
	} while (nextBit == 1)

	return binaryNumberBits.toDecimal()
}

fun executeOperator(operateSubpackages: (List<Long>) -> Long): Long {
	var lengthTypeId = input[i++]
	val subpackagesValues = mutableListOf<Long>()
	when (lengthTypeId) {
		0 -> {
			val subpacketsLength = getLNextBitsDecimalNumber(15)
			val packageLimit = i + subpacketsLength
			while (i < packageLimit) {
				subpackagesValues.add(getPackageValue())
			}
		}
		1 -> {
			val numOfSubpackets = getLNextBitsDecimalNumber(11)
			repeat(numOfSubpackets) {
				subpackagesValues.add(getPackageValue())
			}
		}
		else -> {
			throw IllegalArgumentException("Invalid length Type ID bit")
		}
	}
	return operateSubpackages(subpackagesValues)
}

fun getLNextBitsDecimalNumber(numOfBits: Int): Int {
	val nextBits = mutableListOf<Int>()
	repeat(numOfBits) {
		nextBits.add(input[i++])
	}
	return nextBits.toDecimal().toInt()
}

fun Char.toBinary(): List<Int> =
	when (this) {
		'0' -> listOf(0, 0, 0, 0)
		'1' -> listOf(0, 0, 0, 1)
		'2' -> listOf(0, 0, 1, 0)
		'3' -> listOf(0, 0, 1, 1)
		'4' -> listOf(0, 1, 0, 0)
		'5' -> listOf(0, 1, 0, 1)
		'6' -> listOf(0, 1, 1, 0)
		'7' -> listOf(0, 1, 1, 1)
		'8' -> listOf(1, 0, 0, 0)
		'9' -> listOf(1, 0, 0, 1)
		'A' -> listOf(1, 0, 1, 0)
		'B' -> listOf(1, 0, 1, 1)
		'C' -> listOf(1, 1, 0, 0)
		'D' -> listOf(1, 1, 0, 1)
		'E' -> listOf(1, 1, 1, 0)
		'F' -> listOf(1, 1, 1, 1)
		else -> throw IllegalArgumentException("Invalid Hex number")
	}

fun List<Int>.toDecimal(): Long {
	var decimalValue = 0L
	var bitPow = this.size - 1
	for (i in 0 until size) {
		if (this[i] == 1) {
			decimalValue += 2.0.pow(bitPow).toLong()
		}
		bitPow--
	}

	return decimalValue
}

