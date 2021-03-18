package io.utu.project.graphql.resolver

import graphql.kickstart.tools.GraphQLResolver
import graphql.schema.DataFetchingEnvironment
import io.utu.project.calendar.api.CalendarResourceAvailabilitySlot
import io.utu.project.calendar.api.ReservationReaderApi
import io.utu.project.calendar.api.Resource
import io.utu.project.calendar.api.ResourceAvailabilityApi
import io.utu.project.calendar.api.ResourceAvailabilityInput
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class ResourceResolver(
    private val availabilityReader: ResourceAvailabilityApi
) : GraphQLResolver<Resource> {
    fun availability(resource: Resource, input: ResourceAvailabilityInput): List<CalendarResourceAvailabilitySlot> {
        return availabilityReader.getAvailability(resource, input)
    }

    fun auditLog(resource: Resource): AuditLog {
        return AuditLog(resource.createdAt, resource.updatedAt)
    }
}
