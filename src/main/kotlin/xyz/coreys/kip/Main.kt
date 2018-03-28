package xyz.coreys.kip

import spark.kotlin.*

fun main(args: Array<String>) {
	
	val http: Http = ignite().port(80)
	
	http.get("/") {
		"This is a test"
	}

}