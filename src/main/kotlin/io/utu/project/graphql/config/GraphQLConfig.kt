package io.utu.project.graphql.config

import com.fasterxml.jackson.databind.ObjectMapper
import graphql.Scalars
import graphql.kickstart.spring.web.boot.GraphQLWebAutoConfiguration
import graphql.kickstart.tools.SchemaParserDictionary
import graphql.schema.GraphQLScalarType
import org.springframework.boot.LazyInitializationExcludeFilter
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.server.standard.ServerEndpointExporter
import org.springframework.web.socket.server.standard.ServerEndpointRegistration

@Configuration
@ImportAutoConfiguration(
    classes = [
        GraphQLWebAutoConfiguration::class,
    ]
)
class GraphQLConfig {

    @Bean
    fun bigDecimalScalarType(mapper: ObjectMapper): GraphQLScalarType = Scalars.GraphQLBigDecimal

    @Bean
    fun timestampScalarType(mapper: ObjectMapper): GraphQLScalarType {
        return GraphQLScalarType.newScalar()
            .coercing(TimestampCoercing(mapper))
            .name("Timestamp")
            .description("Instant timestamp")
            .build()
    }

    @Bean
    fun graphQLWebSocketHandlerMappingLazyInitializationExcludeFilter(): LazyInitializationExcludeFilter {
        return LazyInitializationExcludeFilter.forBeanTypes(
            ServerEndpointRegistration::class.java, ServerEndpointExporter::class.java
        )
    }
}