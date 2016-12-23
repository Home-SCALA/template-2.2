package com.sumskoy.habr.template.core

import com.fasterxml.jackson.core.`type`.TypeReference
import com.sumskoy.habr.template.IntegrationTestSpec
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders._
import org.springframework.test.web.servlet.result.MockMvcResultMatchers._

class AuthTest extends IntegrationTestSpec {

  it should "Login as admin through oauth with default password" in {
    val resultActions =
      mockMvc.perform(
        get("/oauth/token").
          accept(md).
          param("grant_type", "password").
          param("client_id", "simple-client").
          param("client_secret", "simple-client-secret-key").
          param("username", "admin").
          param("password", "admin")).
        andExpect(status.isOk).
        andExpect(content.contentType(md)).
        andExpect(jsonPath("$.access_token").exists).
        andExpect(jsonPath("$.token_type").exists).
        andExpect(jsonPath("$.expires_in").exists)

    val contentAsString = resultActions.andReturn.getResponse.getContentAsString

    val map: Map[String, String] = objectMapper.readValue(contentAsString, new TypeReference[Map[String, String]] {})
    val access_token = map.get("access_token").get
    val token_type = map.get("token_type").get

    mockMvc.perform(
      get("/api/auth/check").
        accept(md).
        header("Authorization", token_type + " " + access_token)).
      andExpect(status.isOk).
      andExpect(content.contentType(md)).
      andExpect(jsonPath("$.username").value("admin")).
      andExpect(jsonPath("$.enabled").value(true))
  }

  it should "Can not get token with incorrect user" in {
    mockMvc.perform(
      get("/oauth/token").
        accept(md).
        param("grant_type", "password").
        param("client_id", "simple-client").
        param("client_secret", "simple-client-secret-key").
        param("username", "admin-wrong").
        param("password", "admin")).
      andExpect(status.isBadRequest)
  }

  it should "Login as admin through user form with default password" in {
    mockMvc.perform(
      post("/auth/j_spring_security_check").
        contentType(MediaType.APPLICATION_FORM_URLENCODED).
        param("j_username", "admin").
        param("j_password", "admin")).
      andExpect(status.is3xxRedirection()).
      andExpect(header().string("location", "/index.html"))
  }

  it should "Can not login as admin through worng user of password" in {
    mockMvc.perform(
      post("/auth/j_spring_security_check").
        contentType(MediaType.APPLICATION_FORM_URLENCODED).
        param("j_username", "admin-wong").
        param("j_password", "admin")).
      andExpect(status.is3xxRedirection()).
      andExpect(header().string("location", "/login.html?login_error=true"))
  }

  it should "Can not get private url by anonymous" in {
    mockMvc.perform(
      get("/api/auth/check").
        accept(md)).
      andExpect(status.isUnauthorized)
  }

}
