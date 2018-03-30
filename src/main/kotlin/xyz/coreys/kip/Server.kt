package xyz.coreys.kip

import spark.kotlin.*
import xyz.coreys.kip.utils.Config
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import javax.servlet.MultipartConfigElement

object Server {
	
	private fun startServer() {
		port(Config.port)
		ignite()
	}
	
	fun run() {
		startServer()
		
		staticFiles.externalLocation(Paths.get(Config.uploadDirectory).toString())
		
		val uploadDir = File(Config.uploadDirectory)
		
		get("/upload") {
			halt(405)
		}
		
		post("/upload") {
			val tempFile = Files.createTempFile(uploadDir.toPath(), "", ".png")
			
			request.attribute("org.eclipse.jetty.multipartConfig", MultipartConfigElement("/temp"))
			
			request.raw().getPart(Config.formName).inputStream.use({ input ->
				Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING)
			})
			
			"http://${this.request.host()}/${tempFile.fileName}"
		}
	}
	
}
