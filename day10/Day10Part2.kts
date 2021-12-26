#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner
import java.util.ArrayDeque

/*
 * Part 2 of day 10: https://adventofcode.com/2021/day/10
 */

val file = File("day_10_input.txt")
val scanner = Scanner(file.inputStream())

val incompleteInputsScores = mutableListOf<Long>()

while (scanner.hasNext()) {
	val input = scanner.next()

	val expectedClosingSymbols = ArrayDeque<Char>()
	var i = 0
	var isCorruptedInput = false
	while (i < input.length && !isCorruptedInput) {
		val symbol = input[i]
		if (isOpenSymbol(symbol)) {
			expectedClosingSymbols.push(getClosingSymbol(symbol))
		} else if (!expectedClosingSymbols.isEmpty()) {
			if (expectedClosingSymbols.pop() != symbol) {
				isCorruptedInput = true
			}
		}
		i++
	}
	var totalScore = 0L
	if (!isCorruptedInput) {
		while (!expectedClosingSymbols.isEmpty()) {
			val closingSymbolPoints = calculatePoints(expectedClosingSymbols.pop())
			totalScore = totalScore * 5L + closingSymbolPoints
		}
		incompleteInputsScores.add(totalScore)
	}
}

incompleteInputsScores.sort()
val middleScore = incompleteInputsScores[incompleteInputsScores.size / 2]
println("Middle score: $middleScore")

fun isOpenSymbol(symbol: Char): Boolean =
	when (symbol) {
		'(', '[', '{', '<' -> true
		else -> false
	}

fun getClosingSymbol(symbol: Char): Char =
	when (symbol) {
		'(' -> ')'
		'[' -> ']'
		'{' -> '}'
		'<' -> '>'
		else -> throw IllegalArgumentException("Invalid symbol")
	}

fun calculatePoints(symbol: Char): Int =
	when (symbol) {
		')' -> 1
		']' -> 2
		'}' -> 3
		'>' -> 4
		else -> throw IllegalArgumentException("Invalid symbol")
	}
