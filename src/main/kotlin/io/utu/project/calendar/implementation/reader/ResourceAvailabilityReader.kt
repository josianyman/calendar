package io.utu.project.calendar.implementation.reader

import io.utu.project.calendar.api.CalendarResourceAvailabilitySlot
import io.utu.project.calendar.api.Reservation
import io.utu.project.calendar.api.ReservationReaderApi
import io.utu.project.calendar.api.Resource
import io.utu.project.calendar.api.ResourceAvailabilityApi
import io.utu.project.calendar.api.ResourceAvailabilityInput
import io.utu.project.calendar.api.ResourceReaderApi
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional(readOnly = true)
class ResourceAvailabilityService(
    private val reservationReader: ReservationReaderApi,
    private val resourceReader: ResourceReaderApi
) : ResourceAvailabilityApi {

    override fun getAvailability(
        resource: Resource,
        input: ResourceAvailabilityInput
    ): List<CalendarResourceAvailabilitySlot> {
        val start: LocalDateTime = input.start
        val end = input.end ?: start.plusDays(1)
        val capacity = resource.capacity
        val reservations = reservationReader.findAllByResourceIdAndInterval(resource.id, start, end)
        val timeSlots = timeSlots(start, end, input.slotSize ?: 15)
        return timeSlots.mapIndexed { idx, timeSlot ->
            val reservedForSlot = reservations
                .filter { reservation -> reservation.overlaps(timeSlot) }
            val reservedQuantity = maxOverlappingQuantity(reservedForSlot)
            CalendarResourceAvailabilitySlotImpl(
                start = timeSlot.start,
                end = timeSlot.end,
                offset = idx,
                available = capacity - reservedQuantity,
            )
        }
    }

    override fun isAvailableFor(reservation: Reservation): Boolean {
        val resourceId = reservation.resourceId
        val reservations = reservationReader.findOverlappingReservations(
            reservationId = reservation.id,
            resourceId = resourceId,
            start = reservation.start,
            end = reservation.end
        )
        val capacity = resourceReader.getOne(resourceId).capacity
        return maxOverlappingQuantity(reservations) + (reservation.resourceQuantity) <= capacity
    }

    /**
     * Returns maximal simultaneous reservation quantity of given reservations
     * Returns 0 for empty list
     *
     * Time complexity of the function is O(n^2) where n is reservations count
     * If all reservations end at the same time, complexity is O(n * log(n)) dominated by sorting of reservations
     */
    private fun maxOverlappingQuantity(reservations: List<Reservation>): Int {
        // Quantity intersections models the intersections of reservations with combined quantity
        // Field quantity is sum of reservation quantities belongs to intersection that ends at end
        // Overlapping quantities contains every different reservation intersection end after reservations for loop in executed
        // Maximal simultaneous quantity is one of intersections
        var quantityIntersections = mutableListOf<QuantityIntersection>()
        for (reservation in reservations.sortedBy { it.start }) {
            // If reservation ends after all others, new intersection will be added
            var reservationEndIntersectionExists = false
            // If reservation ends before any existing interval, new intersection will be added with closest existing intersection
            var reservationClosestOverlappingInterval: QuantityIntersection? = null

            // Update intersection quantity if reservation contributes to it
            // Add also new intersections the reservation introduces
            val updated = quantityIntersections.map { existingIntersection ->
                if (existingIntersection.end.isAfter(reservation.start)) {
                    // Reservation overlaps with the reservation intersection

                    if (reservation.end.isEqual(existingIntersection.end)) {
                        // Reservation ends exactly same time as intersection
                        // Quantity is combined to this intersection
                        reservationEndIntersectionExists = true
                        return@map existingIntersection.copy(
                            quantity = existingIntersection.quantity + reservation.resourceQuantity
                        )
                    }

                    if (reservation.end.isBefore(existingIntersection.end)) {
                        // Reservation ends before overlapping quantity
                        // Reservation introduces new intersection with this intersection but ends at reservation end
                        // existingIntersection is not altered:
                        // reservation does not affect quantity from reservation.end to existingIntersection.end
                        if (reservationClosestOverlappingInterval == null ||
                            existingIntersection.end.isBefore(reservationClosestOverlappingInterval!!.end)) {
                            reservationClosestOverlappingInterval = existingIntersection
                        }
                        return@map existingIntersection
                    }

                    // Reservation ends after overlapping quantity
                    // Whole existing interval quantity will increase by reservation quantity
                    return@map existingIntersection.copy(quantity = existingIntersection.quantity + reservation.resourceQuantity)
                } else {
                    existingIntersection
                }
            }

            // Update intersections
            quantityIntersections = updated.toMutableList()

            // Every reservation can add at most one intersection more
            // It is either inside closest existing or after all
            if (!reservationEndIntersectionExists) {
                if (reservationClosestOverlappingInterval != null) {
                    val cumulativeQuantity = reservationClosestOverlappingInterval!!.quantity + reservation.resourceQuantity
                    quantityIntersections.add(QuantityIntersection(reservation.end, cumulativeQuantity))
                }
                quantityIntersections.add(QuantityIntersection(reservation.end, reservation.resourceQuantity))
            }

        }
        return quantityIntersections.maxQuantity()
    }

    // Helpers

    private fun List<QuantityIntersection>.maxQuantity() = maxByOrNull { it.quantity }?.quantity ?: 0

    private fun timeSlots(start: LocalDateTime, end: LocalDateTime, slotSize: Int): List<TimeSlot> {
        var slotStart = start
        val slots = mutableListOf<TimeSlot>()
        while (
            slotStart.isBefore(end)
        ) {
            val slotEnd = slotStart.plusMinutes(slotSize.toLong())
            slots.add(TimeSlot(slotStart, slotEnd))

            slotStart = slotEnd
        }
        return slots
    }

    private fun Reservation.overlaps(timeSlot: TimeSlot): Boolean {
        return start.isBefore(timeSlot.end) && timeSlot.start.isBefore(end)
    }
}

internal data class QuantityIntersection(
    val end: LocalDateTime,
    val quantity: Int
)

internal data class TimeSlot(
    val start: LocalDateTime,
    val end: LocalDateTime
)

internal data class CalendarResourceAvailabilitySlotImpl(
    override val start: LocalDateTime, // Inclusive
    override val end: LocalDateTime, // Exclusive
    override val offset: Int,
    override val available: Int,
) : CalendarResourceAvailabilitySlot