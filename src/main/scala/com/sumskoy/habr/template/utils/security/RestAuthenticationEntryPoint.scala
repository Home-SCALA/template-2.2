package com.sumskoy.habr.template.utils.security

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component(value = "restAuthenticationEntryPoint")
class RestAuthenticationEntryPoint extends AuthenticationEntryPoint {
  def commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {
    var header = request.getHeader("Back-Redirect")
    if (header == null || header.isEmpty) {
      header = "/index.html"
    }
    request.getSession.setAttribute("login-success-url", header)
    if (request.getRequestURI.startsWith("/api")) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
    } else {
      response.sendRedirect("/login.html")
    }
  }
}

