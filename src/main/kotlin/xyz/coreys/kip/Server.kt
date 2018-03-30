package xyz.coreys.kip

import spark.kotlin.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import javax.servlet.MultipartConfigElement

class Server private constructor() {
	
	private object Holder {
		val INSTANCE = Server()
	}
	
	companion object {
		val instance: Server by lazy { Holder.INSTANCE }
	}
	
	init {
		println("Created Kip Server singleton")
		// TODO load config file
	}
	
	private fun startServer() {
		port(80)
		ignite()
	}
	
	fun run() {
		startServer()
		
		// TODO replace upload directory with location from config file
		staticFiles.externalLocation(Paths.get("upload").toString())
		
		val uploadDir = File("upload")
		
		get("/upload") {
			halt(405)
		}
		
		get("/") {
			this.request.host()
		}
		
		post("/upload") {
			val tempFile = Files.createTempFile(uploadDir.toPath(), "", ".png")
			
			this.request.attribute("org.eclipse.jetty.multipartConfig", MultipartConfigElement("/temp"))
			
			this.request.raw().getPart("sharex").inputStream
				.use({ input ->
					Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING)
				})
			
			"""
				{
    				"status": 200,
    				"data": {
        			"link": ${this.request.host()}/${tempFile.fileName}
    				}
				}
				"""
		}
	}
	
}