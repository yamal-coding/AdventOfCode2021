package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
)

func main() {
	zeroOccurencesPerPosition := [12]int{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
	oneOccurencesPerPosition := [12]int{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}

	file, openError := os.Open("day_3_input.txt")
	if openError != nil {
		panic(openError)
	}

	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		binaryNumberStr := scanner.Text()
		for i := 0; i < 12; i++ {
			switch binaryNumberStr[i] {
			case '0':
				zeroOccurencesPerPosition[i]++
			case '1':
				oneOccurencesPerPosition[i]++
			}
		}
	}

	epsilonBits := [12]rune{}
	gammaBits := [12]rune{}

	for i := 0; i < 12; i++ {
		if oneOccurencesPerPosition[i] >= zeroOccurencesPerPosition[i] {
			epsilonBits[i] = '1'
		} else {
			epsilonBits[i] = '0'
		}

		if oneOccurencesPerPosition[i] <= zeroOccurencesPerPosition[i] {
			gammaBits[i] = '1'
		} else {
			gammaBits[i] = '0'
		}
	}
	epsilon := toDecimal(epsilonBits[:])
	gamma := toDecimal(gammaBits[:])
	fmt.Printf("Epsilon %d * Gamma: %d = %d\n", epsilon, gamma, epsilon*gamma)
}

func toDecimal(bits []rune) int {
	decimalValue := 0
	bitPow := len(bits) - 1

	for i := 0; i < len(bits); i++ {
		if bits[i] == '1' {
			decimalValue += int(math.Pow(2, float64(bitPow)))
		}
		bitPow--
	}
	return decimalValue
}
