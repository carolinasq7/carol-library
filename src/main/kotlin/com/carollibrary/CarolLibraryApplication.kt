package com.carollibrary

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class CarolLibraryApplication

fun main(args: Array<String>) {
	runApplication<CarolLibraryApplication>(*args)
}
