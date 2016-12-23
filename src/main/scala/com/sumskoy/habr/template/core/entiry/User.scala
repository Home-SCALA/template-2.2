package com.sumskoy.habr.template.core.entiry

import com.sumskoy.habr.template.utils.BaseEntity
import org.squeryl.annotations.Column

case class User(username: String, password: String, enabled: Boolean, @Column("user_id") override val id: Int) extends BaseEntity {
  def this() = this("", "", false, 0)
}

