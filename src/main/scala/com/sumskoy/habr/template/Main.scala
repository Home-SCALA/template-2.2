package com.sumskoy.habr.template

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.{WebXmlConfiguration, WebAppContext}

object Main extends App {
  val server = new Server(8080)

  val webAppContext = new WebAppContext()
  webAppContext.setResourceBase("src/main/webapp")
  webAppContext.setContextPath("/")
  webAppContext.setParentLoaderPriority(true)
  webAppContext.setConfigurations(Array(
    new WebXmlConfiguration()
  ))

  server.setHandler(webAppContext)
  server.start()
  server.join()
}

