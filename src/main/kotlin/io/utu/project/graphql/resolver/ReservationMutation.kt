package io.utu.project.graphql.resolver

import graphql.kickstart.tools.GraphQLMutationResolver
import io.utu.project.calendar.api.Reservation
import io.utu.project.calendar.api.ReservationApi
import io.utu.project.calendar.api.ReservationCreate
import io.utu.project.calendar.api.ReservationSetName
import io.utu.project.calendar.api.ReservationSetResource
import io.utu.project.calendar.api.ReservationSetTimes
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Component
@Transactional
class ReservationMutation(
    private val service: ReservationApi,
) : GraphQLMutationResolver {
    fun reservationCreate(input: ReservationCreateInput): Reservation {
        return service.create(input)
    }

    fun reservationConfirm(id: UUID): Reservation {
        return service.confirm(id)
    }

    fun reservationCancel(id: UUID): Reservation {
        return service.cancel(id)
    }

    fun reservationSetTimes(input: ReservationSetTimesInput): Reservation {
        return service.setTimes(input)
    }

    fun reservationSetResource(input: ReservationSetResourceInput): Reservation {
        return service.setResource(input)
    }

    fun reservationSetName(input: ReservationSetNameInput): Reservation {
        return service.setName(input)
    }

    fun reservationDelete(id: UUID): ReservationDeletePayload {
        service.delete(id)
        return ReservationDeletePayload(id, true)
    }
}

data class ReservationCreateInput(
    override val resourceId: UUID,
    override val name: String?,
    override val start: LocalDateTime,
    override val end: LocalDateTime,
    override val resourceQuantity: Int?,
) : ReservationCreate

data class ReservationSetResourceInput(
    override val reservationId: UUID,
    override val resourceId: UUID,
    override val resourceQuantity: Int,
) : ReservationSetResource

data class ReservationSetNameInput(
    override val reservationId: UUID,
    override val name: String,
): ReservationSetName

data class ReservationSetTimesInput(
    override val reservationId: UUID,
    override val start: LocalDateTime,
    override val end: LocalDateTime,
): ReservationSetTimes

data class ReservationDeletePayload(
    val id: UUID,
    val deleted: Boolean,
)