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

	lastMeasurementWindow := 0
	measurementsLargerThanPreviousOne := -1
	windows := [3]int{0, 0, 0}
	windowInteration := 1

	for scanner.Scan() {
		newMeasurement, parseError := strconv.Atoi(scanner.Text())
		check(parseError)

		for i := 0; i < windowInteration; i++ {
			windows[i] = windows[i] + newMeasurement
		}

		if windowInteration == 3 {
			if windows[0] > lastMeasurementWindow {
				measurementsLargerThanPreviousOne++
			}
			lastMeasurementWindow = windows[0]
			windows[0] = windows[1]
			windows[1] = windows[2]
			windows[2] = 0
		} else {
			windowInteration++
		}
	}
	file.Close()
	fmt.Printf("Measurements larger than previous one: %d\n", measurementsLargerThanPreviousOne)
}

func check(e error) {
	if e != nil {
		panic(e)
	}
}
