package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
)

func main() {
	file, openError := os.Open("day_2_input.txt")
	if openError != nil {
		panic(openError)
	}
	scanner := bufio.NewScanner(file)
	scanner.Split(bufio.ScanWords)

	x := 0
	y := 0
	for scanner.Scan() {
		direction := scanner.Text()
		if !scanner.Scan() {
			panic("there should be more file content to read")
		}

		increase, parseError := strconv.Atoi(scanner.Text())
		if parseError != nil {
			panic("increase value should be an integer")
		}

		switch direction {
		case "forward":
			x = x + increase
		case "down":
			y = y + increase
		case "up":
			y = y - increase
		default:
			panic("Invalid direction value")
		}
	}

	fmt.Printf("X: %d * Y: %d -> %d\n", x, y, x*y)
}
