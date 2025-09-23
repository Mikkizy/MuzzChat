package com.mcu.muzz.domain.models

data class User(
    val id: String,
    val name: String,
    val avatarUrl: String? = null
)
