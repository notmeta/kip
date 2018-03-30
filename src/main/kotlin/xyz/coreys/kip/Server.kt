package xyz.coreys.kip

import spark.kotlin.*
import xyz.coreys.kip.utils.Config
import xyz.coreys.kip.utils.generateFileName
import java.net.URLConnection
import java.nio.file.Files
import java.nio.file.Paths
import javax.servlet.MultipartConfigElement

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
			val tempFile = Files.createTempFile(Paths.get(Config.uploadDirectory), "", ".png")
			
			request.attribute("org.eclipse.jetty.multipartConfig", MultipartConfigElement("/temp"))
			
			request.raw().getPart(Config.formName).inputStream.use({ input ->
				val fileExtension = URLConnection.guessContentTypeFromStream(input)
				Files.write(Paths.get(Config.uploadDirectory, generateFileName(".$fileExtension")), input.readBytes())
			})
			
			"http://${this.request.host()}/${tempFile.fileName}"
		}
	}
	
}
