#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner
import java.util.ArrayDeque

/*
 * Part 1 of day 10: https://adventofcode.com/2021/day/10
 */

val file = File("day_10_input.txt")
val scanner = Scanner(file.inputStream())

var totalScore = 0

while (scanner.hasNext()) {
	val input = scanner.next()

	val expectedClosingSymbols = ArrayDeque<Char>()
	var i = 0
	var stop = false
	while (i < input.length && !stop) {
		val symbol = input[i]
		if (isOpenSymbol(symbol)) {
			expectedClosingSymbols.push(getClosingSymbol(symbol))
		} else if (!expectedClosingSymbols.isEmpty()) {
			if (expectedClosingSymbols.peek() != symbol) {
				totalScore += calculateScore(symbol)
				stop = true
			} else {
				expectedClosingSymbols.pop()
			}
		}
		i++
	}
}

println("Total score $totalScore")

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

fun calculateScore(symbol: Char): Int =
	when (symbol) {
		')' -> 3
		']' -> 57
		'}' -> 1197
		'>' -> 25137
		else -> 0
	}
