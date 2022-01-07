#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 1 of day 18: https://adventofcode.com/2021/day/18
 */

val file = File("day_18_input.txt")
val scanner = Scanner(file.inputStream())
var acc: SnailfishNumber? = null

while (scanner.hasNext()) {
	val rawNumber = scanner.nextLine()

	var i = 0
	val number = parseRawNumber(rawNumber, { i }, increaseIndex = { i++ })

	acc = acc?.let {
		it + number
	} ?: number

	acc?.let {
		it.reduce()
	}
}

println("Magnitude of total addition is ${acc?.getMagnitude() ?: 0}")

fun parseRawNumber(rawNumber: String, fromIndex: () -> Int, increaseIndex: () -> Int): SnailfishNumber {
	var snailfishNumber: SnailfishNumber? = null
	var leftNumber: SnailfishNumber? = null
	var rightNumber: SnailfishNumber? = null

	while (fromIndex() < rawNumber.length && snailfishNumber == null) {
		val token = rawNumber[fromIndex()]
		when (token) {
			'[' -> {
				increaseIndex()
				leftNumber = parseRawNumber(rawNumber, fromIndex, increaseIndex)
				leftNumber.isLeft = true
			}
			',' -> {
				increaseIndex()
				rightNumber = parseRawNumber(rawNumber, fromIndex, increaseIndex)
				rightNumber.isLeft = false
			}
			']' -> {
				snailfishNumber = SnailfishNumber.Pair(
					left = leftNumber ?: throw IllegalStateException("There should be a correct left number parsed."),
					right = rightNumber ?: throw IllegalStateException("There should be a correct right number parsed."),
					parent = null
				)
				leftNumber.parent = snailfishNumber
				rightNumber.parent = snailfishNumber

				increaseIndex()
			}
			else -> {
				snailfishNumber = SnailfishNumber.RegularNumber(
					parent = null,
					value = Character.getNumericValue(token).takeIf { it >= 0 } ?: throw IllegalStateException("The value parsed should be a numnber")
				)
				increaseIndex()
			}
		}
	}

	return snailfishNumber ?: throw IllegalStateException("There should be a correct number parsed.")
}

fun SnailfishNumber.reduce() {
	do {
		var reduced = tryExplode(currentNumber = this, depth = 0)
		if (!reduced) {
			reduced = trySplit(currentNumber = this)
		}
	} while (reduced)
}

fun trySplit(currentNumber: SnailfishNumber): Boolean =
	when (currentNumber) {
		is SnailfishNumber.RegularNumber -> {
			if (currentNumber.value >= 10) {
				splitNumber(currentNumber)
				true
			} else {
				false
			}
		}
		is SnailfishNumber.Pair -> {
			var splitted = trySplit(currentNumber.left)
			if (!splitted) {
				splitted = trySplit(currentNumber.right)
			}
			splitted
		}
	}

fun tryExplode(currentNumber: SnailfishNumber, depth: Int): Boolean =
	when (currentNumber) {
		is SnailfishNumber.RegularNumber -> false
		is SnailfishNumber.Pair -> {
			if (depth == 4) {
				explodeNumber(currentNumber)
				true
			} else {
				var exploded = tryExplode(currentNumber.left, depth + 1)
				if (!exploded) {
					exploded = tryExplode(currentNumber.right, depth + 1)
				}
				exploded
			}
		}
	}

fun splitNumber(currentNumber: SnailfishNumber.RegularNumber) {
	currentNumber.parent?.let { parent ->
		val value = currentNumber.value
		val leftValue = value / 2
		val rightValue = Math.round(value.toDouble() / 2.0).toInt()

		val left = SnailfishNumber.RegularNumber(
			value = leftValue,
			isLeft = true,
			parent = null
		)
		val right = SnailfishNumber.RegularNumber(
			value = rightValue,
			isLeft = false,
			parent = null
		)

		val splittedNumber = SnailfishNumber.Pair(
			parent = parent,
			isLeft = currentNumber.isLeft,
			left = left,
			right = right
		)

		left.parent = splittedNumber
		right.parent = splittedNumber

		if (currentNumber.isLeft == true) {
			parent.left = splittedNumber
		} else {
			parent.right = splittedNumber
		}
	} ?: throw IllegalStateException("Numeric value not expected to be the root")
}

fun explodeNumber(currentNumber: SnailfishNumber.Pair) {
	val left = currentNumber.left as? SnailfishNumber.RegularNumber 
		?: throw IllegalStateException("Left side of pair with depth 4 should be a regular number")
	val leftValue = left.value

	val right = currentNumber.right as? SnailfishNumber.RegularNumber 
		?: throw IllegalStateException("Right side of pair with depth 4 should be a regular number")
	val rightValue = right.value

	if (currentNumber.isLeft == true) {
		explodeNumbersOfALeftChild(leftValue = leftValue, rightValue = rightValue, parent = currentNumber.parent)
	} else {
		explodeNumbersOfRightChild(leftValue = leftValue, rightValue = rightValue, parent = currentNumber.parent)
	}

	currentNumber.parent?.let { parent ->
		val zeroRegularNumber = SnailfishNumber.RegularNumber(
			parent = parent, 
			isLeft = currentNumber.isLeft,
			value = 0
		)
		if (currentNumber.isLeft == true) {
			parent.left = zeroRegularNumber
		} else {
			parent.right = zeroRegularNumber
		}
	} ?: throw IllegalStateException("A root pair shouldn't be exploded")
}

fun explodeNumbersOfALeftChild(leftValue: Int, rightValue: Int, parent: SnailfishNumber.Pair?) {
	var firstParentWhichIsRightChild: SnailfishNumber.Pair? = parent
	while (firstParentWhichIsRightChild?.isLeft == true) {
		firstParentWhichIsRightChild = firstParentWhichIsRightChild.parent
	}
	if (firstParentWhichIsRightChild?.parent != null) {
		fun increaseFirstRegularNumberToTheRight(current: SnailfishNumber) {
			when (current) {
				is SnailfishNumber.RegularNumber ->
					current.value = current.value + leftValue
				is SnailfishNumber.Pair ->
					increaseFirstRegularNumberToTheRight(current.right) 
			}
		}

		val leftChild = firstParentWhichIsRightChild.parent?.left 
			?: throw IllegalStateException("Parent with right child should exist")

		increaseFirstRegularNumberToTheRight(leftChild)
	}

	var firstParentWithARightChild: SnailfishNumber.Pair? = parent
	if (firstParentWithARightChild != null) {
		fun increaseFirstRegularNumberToTheLeft(current: SnailfishNumber) {
			when (current) {
				is SnailfishNumber.RegularNumber ->
					current.value = current.value + rightValue
				is SnailfishNumber.Pair ->
					increaseFirstRegularNumberToTheLeft(current.left) 
			}
		}

		increaseFirstRegularNumberToTheLeft(firstParentWithARightChild.right)
	}
}

fun explodeNumbersOfRightChild(leftValue: Int, rightValue: Int, parent: SnailfishNumber.Pair?) {
	var firstParentWithALeftChild: SnailfishNumber.Pair? = parent
	if (firstParentWithALeftChild != null) {
		fun increaseFirstRegularNumberToTheRight(current: SnailfishNumber) {
			when (current) {
				is SnailfishNumber.RegularNumber ->
					current.value = current.value + leftValue
				is SnailfishNumber.Pair ->
					increaseFirstRegularNumberToTheRight(current.right) 
			}
		}

		increaseFirstRegularNumberToTheRight(firstParentWithALeftChild.left)
	}

	var firstParentWhichIsLeftChild: SnailfishNumber.Pair? = parent
	while (firstParentWhichIsLeftChild?.isLeft == false) {
		firstParentWhichIsLeftChild = firstParentWhichIsLeftChild.parent
	}
	if (firstParentWhichIsLeftChild?.parent != null) {
		fun increaseFirstRegularNumberToTheLeft(current: SnailfishNumber) {
			when (current) {
				is SnailfishNumber.RegularNumber ->
					current.value = current.value + rightValue
				is SnailfishNumber.Pair ->
					increaseFirstRegularNumberToTheLeft(current.left) 
			}
		}

		val rightChild = firstParentWhichIsLeftChild.parent?.right 
			?: throw IllegalStateException("Parent with left child should exist")

		increaseFirstRegularNumberToTheLeft(rightChild)
	}
}

operator fun SnailfishNumber.plus(other: SnailfishNumber): SnailfishNumber {
	val sum = SnailfishNumber.Pair(
		left = this,
		right = other,
		parent = null
	)
	this.parent = sum
	this.isLeft = true
	other.parent = sum
	other.isLeft = false

	return sum
}

fun SnailfishNumber.getMagnitude(): Int =
	when (this) {
		is SnailfishNumber.Pair -> {
			3 * left.getMagnitude() + 2 * right.getMagnitude()
		}
		is SnailfishNumber.RegularNumber -> value
	}

sealed class SnailfishNumber(
	open var parent: SnailfishNumber.Pair?,
	open var isLeft: Boolean?
) {
	data class Pair(
		override var parent: SnailfishNumber.Pair?,
		override var isLeft: Boolean? = null,
		var left: SnailfishNumber, 
		var right: SnailfishNumber
	) : SnailfishNumber(parent, isLeft)

	data class RegularNumber(
		override var isLeft: Boolean? = null,
		override var parent: SnailfishNumber.Pair?,
		var value: Int
	): SnailfishNumber(parent, isLeft)
}
