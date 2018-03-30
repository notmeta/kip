package xyz.coreys.kip.utils

import java.io.File
import java.util.*

fun generateFileName(extensionSuffix: String): String {
	fun fileExists(fileName: String): Boolean {
		return File(Config.uploadDirectory, fileName).exists()
	}
	
	val chars = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
	var name: String
	
	do {
		val random = Random()
		var temp = ""
		repeat(times = Config.fileCharLength) {
			temp += chars[random.nextInt(chars.length - 0) + 0]
		}
		name = temp + extensionSuffix
	} while (fileExists(name))
	
	return name
}