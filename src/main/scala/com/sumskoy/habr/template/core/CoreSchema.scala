package com.sumskoy.habr.template.core

import org.squeryl._
import com.sumskoy.habr.template.core.entiry.User
import com.sumskoy.habr.template.utils.SquerylEntrypoint._

object CoreSchema extends Schema {
  val users = table[User]("users")

  on(users)(user => declare(
    user.id is autoIncremented,
    user.username is unique
  ))
}
