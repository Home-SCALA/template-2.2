package com.sumskoy.habr.template.utils

import java.sql.Timestamp

import org.squeryl.KeyedEntity
import org.squeryl.annotations.Transient

class BaseEntity extends KeyedEntity[Int] {

  val id = 0

  @Transient var lastModified = new Timestamp(System.currentTimeMillis)

}
