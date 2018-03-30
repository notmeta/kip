package xyz.coreys.kip.utils

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import xyz.coreys.kip.Constants
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Paths

object Config {
	
	var port: Int = Constants.DefaultConfiguration.PORT
	var uploadDirectory: String = Constants.DefaultConfiguration.UPLOAD_DIR
	var uploadUrl: String = Constants.DefaultConfiguration.UPLOAD_URL
	var formName: String = Constants.DefaultConfiguration.FORM_NAME
	var fileCharLength: Int = Constants.DefaultConfiguration.FILE_CHAR_LENGTH
	var siteUrl: String = Constants.DefaultConfiguration.SITE_URL
	
	init {
		val configFile = File(Paths.get(Constants.CONFIG_PATH).toUri())
		
		when {
			!configFile.exists() -> createConfig(configFile)
			else -> loadConfig(configFile)
		}
	}
	
	private fun createConfig(configFile: File) {
		val jsonObject = JsonObject()
		jsonObject.addProperty("port", port)
		jsonObject.addProperty("uploadDirectory", uploadDirectory)
		jsonObject.addProperty("uploadUrl", uploadUrl)
		jsonObject.addProperty("formName", formName)
		jsonObject.addProperty("fileCharLength", fileCharLength)
		jsonObject.addProperty("siteUrl", siteUrl)
		
		FileWriter(configFile.name).use { writer ->
			val gson = GsonBuilder().setPrettyPrinting().create()
			gson.toJson(jsonObject, writer)
		}
	}
	
	private fun loadConfig(configFile: File) {
		FileReader(configFile.name).use { reader ->
			val jsonObject = JsonParser().parse(reader) as JsonObject
			port = jsonObject.get("port").asInt
			uploadDirectory = jsonObject.get("uploadDirectory").asString
			uploadUrl = jsonObject.get("uploadUrl").asString
			formName = jsonObject.get("formName").asString
			fileCharLength = jsonObject.get("fileCharLength").asInt
			siteUrl = jsonObject.get("siteUrl").asString
		}
	}
	
}