#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner
import kotlin.math.pow

/*
 * Part 1 of day 3: https://adventofcode.com/2021/day/3
 */

val file = File("day_3_input.txt")
val scanner = Scanner(file.inputStream())

val bitZeroOcurrences = arrayListOf<Int>()
val bitOneOcurrences = arrayListOf<Int>()

while (scanner.hasNext()) {
	val binaryValue = scanner.next()

	if (bitZeroOcurrences.size < binaryValue.length) {
		bitZeroOcurrences.initOcurrences(binaryValue.length)
		bitOneOcurrences.initOcurrences(binaryValue.length)
	}

	for (i in 0 until binaryValue.length) {
		when (binaryValue[i]) {
			'1' -> bitOneOcurrences[i] += 1
			'0' -> bitZeroOcurrences[i] += 1
		}
	}
}

fun ArrayList<Int>.initOcurrences(newSize: Int) {
	for (i in 0 until newSize) {
		add(0)
	}
}

var gammaBits = arrayListOf<Int>(0)
var epsilonBits = arrayListOf<Int>(0)

for (i in 0 until bitZeroOcurrences.size) {
	gammaBits.add(if (bitOneOcurrences[i] >= bitZeroOcurrences[i]) 1 else 0)
	epsilonBits.add(if (bitOneOcurrences[i] <= bitZeroOcurrences[i]) 1 else 0)
}

val gamma = gammaBits.toDecimal()
val epsilon = epsilonBits.toDecimal()
println("Gamma: $gamma; Epsilon: $epsilon; Gamma*Epsilon: ${gamma * epsilon}")

fun ArrayList<Int>.toDecimal(): Int {
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