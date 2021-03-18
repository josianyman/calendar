package io.utu.project.graphql.resolver

import graphql.kickstart.tools.GraphQLQueryResolver
import io.utu.project.calendar.api.Reservation
import io.utu.project.calendar.api.ReservationReaderApi
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.UUID

@Component
@Transactional(readOnly = true)
class ReservationQuery(
    private val reservationReader: ReservationReaderApi
) :GraphQLQueryResolver {
    fun reservation(id: UUID): Reservation {
        return reservationReader.getOne(id)
    }

    fun reservationsByDateAndResource(input: ReservationsByDateAndResourceInput): List<Reservation> {
        return reservationReader.findByResourceIdAndDate(input.resourceId, input.date)
    }
}

data class ReservationsByDateAndResourceInput(
    val date: LocalDate,
    val resourceId: UUID,
)

