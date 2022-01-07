import java.io.File
import java.util.Scanner

/*
 * Part 2 of day 9: https://adventofcode.com/2021/day/9
 */

val file = File("day_9_input.txt")
val scanner = Scanner(file.inputStream())

data class Height(val value: Int, var visited: Boolean = false)

var numOfColumns = -1
val heightsMap = mutableListOf<MutableList<Height>>()

while (scanner.hasNext()) {
	val row = mutableListOf<Height>()
	scanner.nextLine().forEach { height ->
		val intHeight = height.toString().toInt()
		row.add(Height(intHeight))
		if (intHeight == 9) {
			discardedHeights++
		}
	}
	if (numOfColumns < 0) {
		numOfColumns = row.size
	}
	heightsMap.add(row)
}

val numOfHeights = numOfColumns * heightsMap.size
var discardedHeights = 0
val largestHeights = mutableListOf(0, 0, 0)

while (discardedHeights < numOfHeights) {
	val (row, column) = findFirstNonVisitedHeight()
	val basinSize = calculateBasin(row, column)
	updateLargestBasins(basinSize)
}

val basinProduct = largestHeights[0] * largestHeights[1] * largestHeights[2]
println("Three largest basins are: $largestHeights with a product of: $basinProduct")

fun calculateBasin(row: Int, column: Int): Int {
	heightsMap[row][column].visited = true
	discardedHeights++
	var basinSize = 1

	if (row - 1 >= 0) {
		val topCell = heightsMap[row - 1][column]
		if (!topCell.visited && topCell.value != 9) {
			basinSize += calculateBasin(row - 1, column)
		}
	}

	if (row + 1 < heightsMap.size) {
		val bottomCell = heightsMap[row + 1][column]
		if (!bottomCell.visited && bottomCell.value != 9) {
			basinSize += calculateBasin(row + 1, column)
		}
	}

	if (column - 1 >= 0) {
		val leftCell = heightsMap[row][column - 1]
		if (!leftCell.visited && leftCell.value != 9) {
			basinSize += calculateBasin(row, column - 1)
		}
	}

	if (column + 1 < numOfColumns) {
		val rightCell = heightsMap[row][column + 1]
		if (!rightCell.visited && rightCell.value != 9) {
			basinSize += calculateBasin(row, column + 1)
		}
	}

	return basinSize
}

fun findFirstNonVisitedHeight(): Pair<Int, Int> {
	var found = false
	var heightCoordinates: Pair<Int, Int>? = null
	var row = 0

	while (!found && row < heightsMap.size) {

		heightsMap[row].withIndex().firstOrNull { !it.value.visited && it.value.value != 9}?.let {
			heightCoordinates = row to it.index
			found = true
		} ?: row++
	}

	return heightCoordinates!!
}

fun updateLargestBasins(basinSize: Int) {
	if (basinSize > largestHeights[0]) {
		largestHeights[2] = largestHeights[1]
		largestHeights[1] = largestHeights[0]
		largestHeights[0] = basinSize
	} else if (basinSize > largestHeights[1]) {
		largestHeights[2] = largestHeights[1]
		largestHeights[1] = basinSize
	} else if (basinSize > largestHeights[2]) {
		largestHeights[2] = basinSize
	}
}
