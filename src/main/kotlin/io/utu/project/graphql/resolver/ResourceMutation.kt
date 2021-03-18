package io.utu.project.graphql.resolver

import graphql.kickstart.tools.GraphQLMutationResolver
import io.utu.project.calendar.api.Resource
import io.utu.project.calendar.api.ResourceApi
import io.utu.project.calendar.api.ResourceCreate
import io.utu.project.calendar.api.ResourceReaderApi
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class ResourceMutation(
    private val service: ResourceApi
): GraphQLMutationResolver {
    fun resourceCreate(input: ResourceCreateInput): Resource {
        return service.create(input)
    }
}

data class ResourceCreateInput(
    override val name: String,
    override val description: String,
    override val timezone: String,
    override val capacity: Int,
): ResourceCreate