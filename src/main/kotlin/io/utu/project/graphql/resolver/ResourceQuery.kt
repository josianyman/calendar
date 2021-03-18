package io.utu.project.graphql.resolver

import graphql.kickstart.tools.GraphQLQueryResolver
import io.utu.project.calendar.api.Resource
import io.utu.project.calendar.api.ResourceReaderApi
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
@Transactional(readOnly = true)
class ResourceQuery(
    private val reader: ResourceReaderApi,
): GraphQLQueryResolver {
    fun resource(id: UUID): Resource {
        return reader.getOne(id)
    }
}