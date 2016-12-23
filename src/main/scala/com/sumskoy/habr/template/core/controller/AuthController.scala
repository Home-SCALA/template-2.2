package com.sumskoy.habr.template.core.controller

import java.security.Principal

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, ResponseBody}

import com.sumskoy.habr.template.core.ObjectNotFound
import com.sumskoy.habr.template.core.repository.UserRepository

@Controller
@RequestMapping(Array("api/auth"))
class AuthController @Autowired()(private val userRepository: UserRepository) {

  @RequestMapping(Array("check"))
  @ResponseBody
  def checkTokenValid(principal: Principal): Map[String, Any] = {

    userRepository.findOne(principal.getName) match {
      case Some(user) => Map[String, Any]("username" -> user.username, "enabled" -> user.enabled)
      case _ => throw new ObjectNotFound()
    }

  }

}
