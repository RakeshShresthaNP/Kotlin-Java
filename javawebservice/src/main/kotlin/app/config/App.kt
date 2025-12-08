/**
# Copyright Rakesh Shrestha (rakesh.shrestha@gmail.com)
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#
# Redistributions must retain the above copyright notice.
 */

package app.config

import java.text.SimpleDateFormat

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

import io.javalin.Javalin
import io.javalin.json.JavalinJackson

import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory
import org.eclipse.jetty.http2.HTTP2Cipher
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory
import org.eclipse.jetty.server.*
import org.eclipse.jetty.util.ssl.SslContextFactory

import app.config.Modules.allModules
import app.ext.ErrorException
import app.Router

import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class App : KoinComponent {
	private val auth: Auth by inject()
	private val router: Router by inject()

	fun setup(): Javalin {

		startKoin {
			modules(allModules)
		}

		this.configureDateMapper()

		val app = Javalin.create { config ->
			config.server { createHttpServer() }
			config.plugins.enableCors { cors ->
				cors.addRule { it.anyHost() }
			}
			config.contextPath = "/api"
			/*config.plugins.enableDevLogging()*/
			config.plugins.enableWebjars()
			config.staticFiles.add("/public")
			config.spaRoot.addFile("/", "/public/index.html")
		}.events { event ->
			event.serverStopping {
				stopKoin()
			}
		}.start()

		auth.configure(app)
		router.register(app)
		ErrorException.register(app)

		return app
	}

	private fun createHttpServer(): Server {

		val alpn = ALPNServerConnectionFactory().apply {
			defaultProtocol = "h2"
		}

		val sslContextFactory = SslContextFactory.Server().apply {
			keyStorePath = this::class.java.getResource("/mykeystore.jks").toExternalForm()
			setKeyStorePassword("password")
			cipherComparator = HTTP2Cipher.COMPARATOR
			provider = "Conscrypt"
		}

		SslConnectionFactory(sslContextFactory, alpn.protocol)

		val httpsConfig = HttpConfiguration().apply {
			sendServerVersion = false
			secureScheme = "https"
			securePort = 8443
			addCustomizer(SecureRequestCustomizer())
		}

		HTTP2ServerConnectionFactory(httpsConfig)

		HttpConnectionFactory(httpsConfig)

		val server = Server()
		val httpConnector = ServerConnector(server).apply {
			port = 8080
		}
		val httpsConnector = ServerConnector(server, sslContextFactory).apply {
			port = 8443
		}
		server.connectors = arrayOf(httpConnector, httpsConnector)
		return server

	}

	private fun configureDateMapper() {
		val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
		JavalinJackson.configure(
			jacksonObjectMapper()
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			.setDateFormat(dateFormat)
			.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true)
		)
	}

}

