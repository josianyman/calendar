package io.utu.project.utils

import io.utu.project.graphql.resolver.ReservationCreateInput
import io.utu.project.graphql.resolver.ReservationSetNameInput
import io.utu.project.graphql.resolver.ReservationSetResourceInput
import io.utu.project.graphql.resolver.ReservationSetTimesInput
import io.utu.project.graphql.resolver.ResourceCreateInput
import java.time.LocalDateTime
import java.util.UUID

val CREATE_RESOURCE = ResourceCreateInput(
    name = "Test name",
    description = "Test description",
    timezone =  "Europe/Helsinki",
    capacity = 50
)

fun createReservationCommand(resourceId: UUID) = ReservationCreateInput(
    resourceId = resourceId,
    resourceQuantity = 2,
    name = "Test name",
    start = LocalDateTime.of(2021,3,21,10,0),
    end = LocalDateTime.of(2021,3,21,17,0),
)

fun setResourceCommand(reservationId: UUID, resourceId: UUID) = ReservationSetResourceInput(
    reservationId = reservationId,
    resourceId = resourceId,
    resourceQuantity = 2,
)

fun  setTimesCommand(reservationId: UUID) = ReservationSetTimesInput(
    reservationId = reservationId,
    start = LocalDateTime.of(2021,3,29,15,0),
    end = LocalDateTime.of(2021,3,29,16,15),
)

fun  setNameCommand(reservationId: UUID) = ReservationSetNameInput(
    reservationId = reservationId,
    name = "Updated name",
)