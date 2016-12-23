package com.sumskoy.habr.template

import java.util
import javax.annotation.Resource

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.web.FilterChainProxy
import org.springframework.test.context.{TestContextManager, ContextConfiguration}
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

import scala.collection.JavaConverters._

@ContextConfiguration(value = Array("classpath:context/root.xml", "classpath:context/mvc.xml"))
@WebAppConfiguration
abstract class IntegrationTestSpec extends FlatSpec with ShouldMatchers with ScalaFutures {
  @Resource private val springSecurityFilterChain: java.util.List[FilterChainProxy] = new util.ArrayList[FilterChainProxy]()
  @Autowired private val wac: WebApplicationContext = null

  new TestContextManager(this.getClass).prepareTestInstance(this)

  var builder = MockMvcBuilders.webAppContextSetup(this.wac)
  for(filter <- springSecurityFilterChain.asScala) builder = builder.addFilters(filter)

  val mockMvc = builder.build()
  val md = MediaType.parseMediaType("application/json;charset=UTF-8")

  val objectMapper = new ObjectMapper() with ScalaObjectMapper
  objectMapper.registerModule(DefaultScalaModule)
}