package io.utu.project.utils

import io.utu.project.calendar.api.Reservation
import io.utu.project.calendar.api.ReservationApi
import io.utu.project.calendar.api.Resource
import io.utu.project.calendar.implementation.mapper.ReservationMapper
import io.utu.project.calendar.implementation.mapper.ResourceMapper
import io.utu.project.calendar.implementation.repository.ReservationRepository
import io.utu.project.calendar.implementation.repository.ResourceRepository
import io.utu.project.calendar.implementation.service.ResourceService
import io.utu.project.graphql.resolver.ReservationCreateInput
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
class DataInitializer(
    private val resourceService: ResourceService,
    private val reservationApi: ReservationApi,
    override val repository: ReservationRepository,
): ReservationMapper() {
    fun initResource(): Resource {
        return resourceService.create(CREATE_RESOURCE)
    }

    fun initReservation(): Reservation {
        val resource = initResource()
        return reservationApi.create(createReservationCommand(resource.id))
    }

    fun initReservation(resourceId: UUID, quantity: Int, start: LocalDateTime, end: LocalDateTime): Reservation {
        return reservationApi.create(ReservationCreateInput(
            resourceId, "Test", start, end, quantity
        ))
    }

    fun createInput(resourceId: UUID, quantity: Int, start: LocalDateTime, end: LocalDateTime) = ReservationCreateInput(
        resourceId, "Test", start, end, quantity
    )

    fun createAll(create: List<ReservationCreateInput>) {
        repository.saveAll(create.map { it.toEntity() })
    }
}