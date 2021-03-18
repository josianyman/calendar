package io.utu.project.calendar.api

import io.utu.project.util.RepositoryReader
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

interface ReservationApi {
    fun create(create: ReservationCreate): Reservation
    fun setName(setName: ReservationSetName): Reservation
    fun setTimes(setTimes: ReservationSetTimes): Reservation
    fun setResource(setResource: ReservationSetResource): Reservation
    fun confirm(id: UUID): Reservation
    fun cancel(id: UUID): Reservation
    fun delete(id: UUID)
}

interface ReservationReaderApi : RepositoryReader<Reservation, UUID> {
    fun findByResourceIdAndDate(
        resourceId: UUID,
        date: LocalDate,
        includeCancelled: Boolean = false,
    ): List<Reservation>

    fun findAllByResourceIdAndInterval(
        resourceId: UUID,
        start: LocalDateTime,
        end: LocalDateTime,
        includeCancelled: Boolean = false
    ): List<Reservation>

    fun findOverlappingReservations(
        reservationId: UUID,
        resourceId: UUID,
        start: LocalDateTime,
        end: LocalDateTime,
        includeCancelled: Boolean = false
    ): List<Reservation> {
        return findAllByResourceIdAndInterval(resourceId, start, end, includeCancelled)
            .filter { it.id != reservationId }
    }
}

interface ResourceAvailabilityApi {
    fun getAvailability(resource: Resource, input: ResourceAvailabilityInput): List<CalendarResourceAvailabilitySlot>
    fun isAvailableFor(reservation: Reservation): Boolean
}

interface Reservation {
    val id: UUID
    val resourceId: UUID
    val name: String?
    val start: LocalDateTime // inclusive
    val end: LocalDateTime // exclusive
    val state: ReservationState
    val resourceQuantity: Int
    val createdAt: Instant
    val updatedAt: Instant
}

enum class ReservationState {
    DRAFT,
    CONFIRMED,
    CANCELLED,
}

// Commands

interface ReservationCreate {
    val resourceId: UUID
    val name: String?

    /**
     * Inclusive
     */
    val start: LocalDateTime

    /**
     * Exclusive
     */
    val end: LocalDateTime
    val quantity: Int?
}

interface ReservationSetResource {
    val reservationId: UUID
    val resourceId: UUID
    val quantity: Int
}

interface ReservationSetName {
    val reservationId: UUID
    val name: String
}

interface ReservationSetTimes {
    val reservationId: UUID

    /**
     * Inclusive
     */
    val start: LocalDateTime

    /**
     * Exclusive
     */
    val end: LocalDateTime
}

// Availability

data class ResourceAvailabilityInput(
    val end: LocalDateTime?,
    val slotSize: Int?,
    val start: LocalDateTime,
)

interface CalendarResourceAvailabilitySlot {
    val available: Int
    val end: LocalDateTime
    val offset: Int
    val start: LocalDateTime
}