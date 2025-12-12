package kr.kro.chzzk.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChzzkServerApplication

fun main(args: Array<String>) {
    runApplication<ChzzkServerApplication>(*args)
}