package com.example.jenkinstest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JenkinstestApplication

fun main(args: Array<String>) {
    runApplication<JenkinstestApplication>(*args)
}
