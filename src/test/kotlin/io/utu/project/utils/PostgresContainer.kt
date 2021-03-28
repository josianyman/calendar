package io.utu.project.utils

import org.springframework.core.env.MapPropertySource
import org.testcontainers.containers.PostgreSQLContainer


val POSTGRES_INSTANCE: PostgresContainer by lazy { PostgresContainer().apply { this.start() } }

const val POSTGRES_VERSION = "postgres:9.5"
class PostgresContainer: PostgreSQLContainer<PostgresContainer>(POSTGRES_VERSION) {

    fun getConnectionConfiguration(): Map<String, String> = mapOf(
        "spring.datasource.url" to this.jdbcUrl,
        "spring.datasource.username" to this.username,
        "spring.datasource.password" to  this.password
    )

    fun getPropertySource(name: String = "postgres") = MapPropertySource(
        name, getConnectionConfiguration()
    )
}