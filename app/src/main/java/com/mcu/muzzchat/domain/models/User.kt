package com.mcu.muzzchat.domain.models

/*data class User(
    val id: String,
    val name: String,
    val avatarUrl: String? = null
)*/

enum class User {
    CURRENT_USER,
    OTHER_USER
}
