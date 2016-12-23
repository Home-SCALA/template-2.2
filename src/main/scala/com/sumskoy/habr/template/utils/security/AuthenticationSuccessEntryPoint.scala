package com.sumskoy.habr.template.utils.security

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component(value = "authenticationSuccessEntryPoint")
class AuthenticationSuccessEntryPoint extends AuthenticationSuccessHandler {
  def onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
    val attribute = Option(request.getSession.getAttribute("login-success-url"))
    val redirect = attribute.getOrElse("/index.html").toString
    response.sendRedirect(redirect)
  }
}
