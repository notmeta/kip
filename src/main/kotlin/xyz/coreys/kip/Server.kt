package xyz.coreys.kip

import spark.kotlin.*
import xyz.coreys.kip.utils.Config
import xyz.coreys.kip.utils.generateFileName
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
			request.attribute("org.eclipse.jetty.multipartConfig", MultipartConfigElement("/temp"))
			
			val oldFileName = request.raw().getPart(Config.formName).submittedFileName
			val extension = oldFileName.substring(oldFileName.lastIndexOf('.') + 1, oldFileName.length)
			val newFileName = generateFileName(".$extension")
			
			request.raw().getPart(Config.formName).inputStream.use({ input ->
				Files.write(Paths.get(Config.uploadDirectory, newFileName), input.readBytes())
			})
			
			val site = if (Config.siteUrl.endsWith("/")) Config.siteUrl else Config.siteUrl + "/"
			
			"$site$newFileName"
		}
	}
	
}
