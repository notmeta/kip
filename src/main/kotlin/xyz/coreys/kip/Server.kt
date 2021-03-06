package xyz.coreys.kip

import spark.kotlin.*
import xyz.coreys.kip.utils.Config
import java.nio.file.Files
import java.nio.file.Paths
import javax.servlet.MultipartConfigElement

object Server {

	const val GENERATED_FILE_ID_RADIX = 36

	private val snowflakeGen = Snowflake(12)

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
			
			val originalFile = request.raw().getPart(Config.formName).submittedFileName
			val extension = originalFile.substring(originalFile.lastIndexOf('.') + 1, originalFile.length)
			val fileToSave = if (Config.keepFileName) originalFile else "${snowflakeGen.nextId.toString(GENERATED_FILE_ID_RADIX)}.$extension"

			request.raw().getPart(Config.formName).inputStream.use({ input ->
				Files.write(Paths.get(Config.uploadDirectory, fileToSave), input.readBytes())
			})
			
			val appendPort = if (Config.port != Constants.STANDARD_HTTP_PORT) ":${Config.port}" else ""
			val siteUrl = if (Config.siteUrl.endsWith("/")) {
				Config.siteUrl.removeSuffix("/") + appendPort + "/"
			} else {
				"${Config.siteUrl}:$appendPort/"
			}
			
			"$siteUrl$fileToSave"
		}
	}
	
}
