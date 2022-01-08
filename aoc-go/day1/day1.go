package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
)

func main() {
	file, openError := os.Open("day_1_input.txt")
	check(openError)

	scanner := bufio.NewScanner(file)
	lastMeasurement := 0
	measurementsLargerThanPreviousOne := -1

	for scanner.Scan() {
		newMeasurementStr := scanner.Text()
		newMeasurement, parseError := strconv.Atoi(newMeasurementStr)
		check(parseError)

		if newMeasurement > lastMeasurement {
			measurementsLargerThanPreviousOne++
		}
		lastMeasurement = newMeasurement
	}

	file.Close()
	fmt.Printf("Measurements larger than previous one: %d\n", measurementsLargerThanPreviousOne)
}

func check(e error) {
	if e != nil {
		panic(e)
	}
}
