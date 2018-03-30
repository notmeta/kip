package xyz.coreys.kip

import spark.kotlin.*
import xyz.coreys.kip.utils.Config
import xyz.coreys.kip.utils.generateFileName
import java.nio.file.Files
import java.nio.file.Paths


object Server {
	
	private fun startServer() {
		port(Config.port)
		ignite()
		staticFiles.externalLocation(Config.uploadDirectory)
	}
	
	fun run() {
		startServer()
		
		get("/${Config.uploadUrl}") {
			halt(405)
		}
		
		// TODO secret token/password
		
		post("/${Config.uploadUrl}") {
			var fileName = request.raw().getPart(Config.formName).submittedFileName
			val extension = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length)
			
			request.raw().getPart(Config.formName).inputStream.use({ input ->
				fileName = generateFileName(".$extension")
				Files.write(Paths.get(Config.uploadDirectory, fileName), input.readBytes())
			})
			
			"http://${this.request.host()}/$fileName"
		}
	}
	
}
