package me.inassar.kmp_playground

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform