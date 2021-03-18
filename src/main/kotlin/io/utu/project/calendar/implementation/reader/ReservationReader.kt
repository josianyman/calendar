package io.utu.project.calendar.implementation.reader

import io.utu.project.calendar.api.Reservation
import io.utu.project.calendar.api.ReservationReaderApi
import io.utu.project.calendar.implementation.repository.ReservationRepository
import io.utu.project.util.RepositoryReader
import io.utu.project.util.RepositoryReaderDelegate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

@Service
@Transactional(readOnly = true)
class ReservationReader(
    private val repository: ReservationRepository
) : ReservationReaderApi, RepositoryReader<Reservation, UUID> by RepositoryReaderDelegate(repository) {
    override fun findByResourceIdAndDate(
        resourceId: UUID,
        date: LocalDate,
        includeCancelled: Boolean
    )
        : List<Reservation> {
        return findAllByResourceIdAndInterval(resourceId, date.atStartOfDay(), date.atTime(LocalTime.MAX), includeCancelled)
    }

    override fun findAllByResourceIdAndInterval(
        resourceId: UUID,
        start: LocalDateTime,
        end: LocalDateTime,
        includeCancelled: Boolean
    ): List<Reservation> {
        return if (includeCancelled) {
            repository.findAllByResourceIdAndInterval(resourceId, start, end)
        } else {
            repository.findAllByResourceIdAndIntervalAndStateNotCancelled(resourceId, start, end)
        }
    }
}