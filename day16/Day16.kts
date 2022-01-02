#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner
import kotlin.math.pow

/*
 * Part 1 of day 16: https://adventofcode.com/2021/day/16
 */

val file = File("day_16_input.txt")
val scanner = Scanner(file.inputStream())

val input = scanner.nextLine().map { it }.flatMap { it.toBinary() }

var i = 0
var versionNumbersSum = 0

parsePackage()

println("Sum of versions is $versionNumbersSum")

fun parsePackage() {
	val versionNumber = listOf(input[i++], input[i++], input[i++]).toDecimal()
	versionNumbersSum += versionNumber

	val typeId = listOf(input[i++], input[i++], input[i++]).toDecimal()

	if (typeId == 4) {
		var packageLength = 6
		do {
			val nextBit = input[i++]
			// skip next 4 bits
			i += 4
			packageLength += 5
		} while (nextBit == 1)
	} else {
		var lengthTypeId = input[i++]
		when (lengthTypeId) {
			0 -> {
				val subpacketsLength = getLNextBitsDecimalNumber(15)
				val packageLimit = i + subpacketsLength
				while (i < packageLimit) {
					parsePackage()
				}
			}
			1 -> {
				val numOfSubpackets = getLNextBitsDecimalNumber(11)
				repeat(numOfSubpackets) {
					parsePackage()
				}
			}
			else -> {
				throw IllegalArgumentException("Invalid length Type Id bit")
			}
		}
	}
}

fun getLNextBitsDecimalNumber(numOfBits: Int): Int {
	val nextBits = mutableListOf<Int>()
	repeat(numOfBits) {
		nextBits.add(input[i++])
	}
	return nextBits.toDecimal()
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

fun List<Int>.toDecimal(): Int {
	var decimalValue = 0
	var bitPow = this.size - 1
	for (i in 0 until size) {
		if (this[i] == 1) {
			decimalValue += 2.0.pow(bitPow).toInt()
		}
		bitPow--
	}

	return decimalValue
}

