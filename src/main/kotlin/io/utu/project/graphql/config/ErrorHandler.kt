package io.utu.project.graphql.config

import graphql.GraphQLError
import graphql.kickstart.execution.error.DefaultGraphQLErrorHandler
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Component
@Primary
class ErrorHandler : DefaultGraphQLErrorHandler() {
    /**
     * Expose all errors to schema to simplify development
     */
    override fun filterGraphQLErrors(errors: MutableList<GraphQLError>?): MutableList<GraphQLError> {
        return errors ?: mutableListOf()
    }
}