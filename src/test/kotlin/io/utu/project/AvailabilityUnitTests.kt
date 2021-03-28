package io.utu.project

import io.utu.project.calendar.api.Reservation
import io.utu.project.calendar.api.ReservationReaderApi
import io.utu.project.calendar.api.ReservationState
import io.utu.project.calendar.api.ResourceReaderApi
import io.utu.project.calendar.implementation.entity.ReservationEntity
import io.utu.project.calendar.implementation.reader.ResourceAvailabilityService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import java.time.LocalDateTime
import java.util.UUID

class AvailabilityServiceTest {
    private val reservationReader: ReservationReaderApi = mock(ReservationReaderApi::class.java)
    private val resourceReader: ResourceReaderApi = mock(ResourceReaderApi::class.java)
    private val service = ResourceAvailabilityService(reservationReader, resourceReader)

    @Test
    fun testMaxOverlappingQuantityOneReservation() {
        val quantity = service.maxOverlappingQuantity(listOf(reservation(time_8_00, time_9_00, 10)))
        assertEquals(10, quantity)
    }

    @Test
    fun testMaxOverlappingEmptyQuantity() {
        val quantity = service.maxOverlappingQuantity(listOf())
        assertEquals(0, quantity)
    }

    @Test
    fun testMaxOverlappingQuantityMultipleReservations() {
        val reservations = (1..5).map { reservation(time_8_00, time_9_00, 10) }
        (0..5).forEach {
            val quantity = service.maxOverlappingQuantity(reservations.take(it))
            assertEquals(10 * it, quantity)
        }
    }

    @Test
    fun testMaxOverlappingQuantityNonOverlapping() {
        var start = time_8_00
        var end = time_9_00
        val reservations = (1..5).map {
            start = start.plusHours(1L)
            end = end.plusHours(1L)
            reservation(start, end, 10)
        }
        (1..5).forEach {
            val quantity = service.maxOverlappingQuantity(reservations.take(it))
            assertEquals(10, quantity)
        }
    }

    @Test
    fun testMaxOverlappingQuantityOverlapping() {
        var start = time_8_00
        var end = time_9_00
        val reservations = (1..5).map {
            start = start.plusMinutes(1L)
            end = end.plusMinutes(1L)
            reservation(start, end, 10)
        }
        (0..5).forEach {
            val quantity = service.maxOverlappingQuantity(reservations.take(it))
            assertEquals(10 * it, quantity)
        }
    }

    @Test
    fun testMaxOverlappingQuantityOverlappingPyramid() {
        var start = time_8_00
        var end = time_9_00
        val reservations = (1..5).map {
            start = start.plusMinutes(1L)
            end = end.minusMinutes(1L)
            reservation(start, end, 10)
        }
        (0..5).forEach {
            val quantity = service.maxOverlappingQuantity(reservations.take(it))
            assertEquals(10 * it, quantity)
        }
    }

    @Test
    fun testMaxOverlappingQuantityOverlappingDoublePyramid() {
        var start = time_8_00
        var end = time_9_00
        val reservationPyramid = (1..5).map {
            start = start.plusMinutes(1L)
            end = end.minusMinutes(1L)
            reservation(start, end, 10)
        }
        start = start.plusHours(2)
        end = end.plusHours(2)
        val doublePyramid = reservationPyramid + (1..5).map {
            start = start.plusMinutes(1L)
            end = end.minusMinutes(1L)
            reservation(start, end, 100)
        }
        (0..10).forEach {
            val quantity = service.maxOverlappingQuantity(doublePyramid.take(it))
            if (it <= 5) {
                assertEquals(10 * it, quantity)
            } else {
                assertEquals(100 * (it - 5), quantity)
            }

        }
    }

    @Test
    fun testMaxOverlappingQuantityOverlappingDoublePyramidBiggerFirst() {
        var start = time_8_00
        var end = time_9_00
        val reservationPyramid = (1..5).map {
            start = start.plusMinutes(1L)
            end = end.minusMinutes(1L)
            reservation(start, end, 100)
        }
        start = start.plusHours(2)
        end = end.plusHours(2)
        val doublePyramid = reservationPyramid + (1..5).map {
            start = start.plusMinutes(1L)
            end = end.minusMinutes(1L)
            reservation(start, end, 10)
        }
        (0..10).forEach {
            val quantity = service.maxOverlappingQuantity(doublePyramid.take(it))
            if (it <= 5) {
                assertEquals(100 * it, quantity)
            } else {
                assertEquals(500, quantity)
            }

        }
    }

    private val time_8_00 = LocalDateTime.of(2020, 5, 8, 8, 0)
    private val time_9_00 = time_8_00.plusHours(1)

    private fun reservation(start: LocalDateTime, end: LocalDateTime, quantity: Int): Reservation {
        return ReservationEntity(
            id = UUID.randomUUID(),
            name = "Test",
            start = start,
            end = end,
            resourceQuantity = quantity,
            state = ReservationState.CONFIRMED,
            resourceId = UUID.randomUUID(),
        )
    }
}
