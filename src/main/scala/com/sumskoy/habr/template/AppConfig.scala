package com.sumskoy.habr.template

import javax.sql.DataSource

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.sumskoy.habr.template.utils.LiquibaseDropAllSupport
import com.typesafe.config.ConfigFactory
import org.apache.commons.dbcp.BasicDataSource
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.squeryl.adapters.PostgreSqlAdapter
import org.squeryl.{Session, SessionFactory}


class AppConfig {

  val env = scala.util.Properties.propOrElse("spring.profiles.active", scala.util.Properties.envOrElse("ENV", "test"))
  val conf = ConfigFactory.load()
  val default = conf.getConfig("habr.template.default")
  val config = conf.getConfig("habr.template." + env).withFallback(default)

  def dataSource = {
    val ds = new BasicDataSource
    ds.setDriverClassName("org.postgresql.Driver")
    ds.setUsername(config.getString("db.user"))
    ds.setPassword(config.getString("db.password"))
    ds.setMaxActive(20)
    ds.setMaxIdle(10)
    ds.setInitialSize(10)
    ds.setUrl(config.getString("db.url"))
    ds
  }

  def db(dataSource: DataSource) = {
    SessionFactory.concreteFactory = Some(() => Session.create(dataSource.getConnection, new PostgreSqlAdapter()))
    SessionFactory.concreteFactory
  }

  def liquibase(dataSource: DataSource) = {
    val liquibase = new LiquibaseDropAllSupport()
    liquibase.setDataSource(dataSource)
    liquibase.setChangeLog("classpath:changelog/db.changelog-master.xml")
    liquibase.setContexts(env)
    liquibase.setShouldRun(true)
    liquibase.dropAllContexts += "test"
    liquibase
  }

  def converter() = {
    val messageConverter = new MappingJackson2HttpMessageConverter()

    val objectMapper = new ObjectMapper() with ScalaObjectMapper
    objectMapper.registerModule(DefaultScalaModule)
    messageConverter.setObjectMapper(objectMapper)

    messageConverter
  }
}
