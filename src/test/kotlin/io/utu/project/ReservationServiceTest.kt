package io.utu.project

import io.utu.project.calendar.api.ReservationApi
import io.utu.project.calendar.api.ReservationReaderApi
import io.utu.project.calendar.api.ReservationState
import io.utu.project.utils.TestBase
import io.utu.project.utils.createReservationCommand
import io.utu.project.utils.setNameCommand
import io.utu.project.utils.setResourceCommand
import io.utu.project.utils.setTimesCommand
import junit.framework.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import java.lang.IllegalArgumentException

class ReservationServiceTest : TestBase() {
    @Autowired
    private lateinit var reservationService: ReservationApi

    @Autowired
    private lateinit var reservationReader: ReservationReaderApi

    @Test
    fun createReservation() {
        val resource = initializer.initResource()
        val command = createReservationCommand(resource.id)
        val reservation = reservationService.create(command)
        val read = reservationReader.getOne(reservation.id)
        assertEquals(reservation, read)
        assertEquals(reservation.resourceId, command.resourceId)
        assertEquals(reservation.resourceQuantity, command.resourceQuantity)
        assertEquals(reservation.name, command.name)
        assertEquals(reservation.start, command.start)
        assertEquals(reservation.end, command.end)
        assertEquals(reservation.state, ReservationState.DRAFT)
    }

    @Test
    fun setName() {
        val reservation = initializer.initReservation()
        val command = setNameCommand(reservation.id)
        val updated = reservationService.setName(command)
        assertEquals(command.name, updated.name)
    }

    @Test
    fun setResource() {
        val reservation = initializer.initReservation()
        val resource = initializer.initResource()
        val command = setResourceCommand(reservation.id, resource.id)
        val updated = reservationService.setResource(command)
        assertEquals(command.resourceId, updated.resourceId)
        assertEquals(command.resourceQuantity, updated.resourceQuantity)
    }

    @Test
    fun setTimes() {
        val reservation = initializer.initReservation()
        val command = setTimesCommand(reservation.id)
        val updated = reservationService.setTimes(command)
        assertEquals(command.start, updated.start)
        assertEquals(command.end, updated.end)
    }

    @Test
    fun cancelReservation() {
        val reservation = initializer.initReservation()
        val cancelled = reservationService.cancel(reservation.id)
        assertEquals(cancelled.state, ReservationState.CANCELLED)
        assertThrows<IllegalArgumentException> { reservationService.setName(setNameCommand(reservation.id)) }
        assertThrows<IllegalArgumentException> { reservationService.setTimes(setTimesCommand(reservation.id)) }
        assertThrows<IllegalArgumentException> { reservationService.setResource(setResourceCommand(reservation.id, reservation.resourceId)) }
        assertThrows<IllegalArgumentException> { reservationService.confirm(reservation.id) }
    }

    @Test
    fun confirmReservation() {
        val reservation = initializer.initReservation()
        val confirmed = reservationService.confirm(reservation.id)
        assertEquals(confirmed.state, ReservationState.CONFIRMED)
        assertThrows<IllegalArgumentException> { reservationService.confirm(reservation.id) }
    }

    @Test
    fun deleteReservation() {
        val reservation = initializer.initReservation()
        reservationService.delete(reservation.id)
        assertThrows<JpaObjectRetrievalFailureException> { reservationReader.getOne(reservation.id) }
    }
}