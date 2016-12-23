package com.sumskoy.habr.template.core

class ApiException(msg: String = "API Exception", cause: Throwable = null) extends RuntimeException(msg, cause)

class ObjectNotFound(msg: String = "Object not found") extends ApiException(msg)
