package io.utu.project.calendar.implementation.service

import io.utu.project.calendar.api.Reservation
import io.utu.project.calendar.api.ReservationApi
import io.utu.project.calendar.api.ReservationCreate
import io.utu.project.calendar.api.ReservationSetName
import io.utu.project.calendar.api.ReservationSetResource
import io.utu.project.calendar.api.ReservationSetTimes
import io.utu.project.calendar.implementation.mapper.ReservationMapper
import io.utu.project.calendar.implementation.repository.ReservationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class ReservationService(
    override val repository: ReservationRepository,
) : ReservationApi, ReservationMapper() {
    override fun create(create: ReservationCreate): Reservation {
        return create
            .toEntity()
            .save()
    }

    override fun setName(setName: ReservationSetName): Reservation {
        return get(setName.reservationId)
            .requireNotCancelled()
            .update(setName)
            .save()
    }

    override fun setTimes(setTimes: ReservationSetTimes): Reservation {
        return get(setTimes.reservationId)
            .requireNotCancelled()
            .update(setTimes)
            .save()
    }

    override fun setResource(setResource: ReservationSetResource): Reservation {
        return get(setResource.reservationId)
            .requireNotCancelled()
            .update(setResource)
            .save()
    }

    override fun confirm(id: UUID): Reservation {
        return get(id)
            .requireDraftState()
            .confirm()
            .save()
    }

    override fun cancel(id: UUID): Reservation {
        return get(id)
            .requireNotCancelled()
            .cancel()
            .save()
    }

    override fun delete(id: UUID) {
        repository.deleteById(id)
    }
}

