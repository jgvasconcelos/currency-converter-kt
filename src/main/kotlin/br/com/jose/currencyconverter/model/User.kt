package br.com.jose.currencyconverter.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class User(
    val id: Long,
    val username: String,
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var password: String,
    var registerDate: LocalDate,
    @JsonIgnore
    val active: Boolean
)
