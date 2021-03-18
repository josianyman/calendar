package io.utu.project.calendar.implementation.repository

import io.utu.project.calendar.implementation.entity.ReservationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime
import java.util.UUID

interface ReservationRepository : JpaRepository<ReservationEntity, UUID> {

    @Query(
        """SELECT * FROM reservation
              WHERE resource_id = :resourceId
              AND (start_time, end_time) OVERLAPS (:startTime, :endTime)
              ORDER BY start_time""",
        nativeQuery = true
    )
    fun findAllByResourceIdAndInterval(
        resourceId: UUID,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
    ): List<ReservationEntity>

    @Query(
        """SELECT * FROM reservation
              WHERE resource_id = :resourceId
              AND (start_time, end_time) OVERLAPS (:startTime, :endTime)
              AND state != 'CANCELLED'
              ORDER BY start_time""",
        nativeQuery = true
    )
    fun findAllByResourceIdAndIntervalAndStateNotCancelled(
        resourceId: UUID,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
    ): List<ReservationEntity>
}