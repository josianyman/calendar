package io.utu.project.graphql.resolver

import graphql.kickstart.tools.GraphQLResolver
import io.utu.project.calendar.api.Reservation
import io.utu.project.calendar.api.Resource
import io.utu.project.calendar.api.ResourceReaderApi
import org.springframework.data.jpa.domain.AbstractPersistable_
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Component
@Transactional(readOnly = true)
class ReservationResolver(
    private val resourceReader: ResourceReaderApi
) : GraphQLResolver<Reservation> {
    fun resource(reservation: Reservation): Resource {
        return resourceReader.getOne(reservation.resourceId)
    }

    fun auditLog(reservation: Reservation): AuditLog {
        return AuditLog(reservation.createdAt, reservation.updatedAt)
    }
}

data class AuditLog(
    val createdAt: Instant,
    val updatedAt: Instant
)