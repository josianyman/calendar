package io.utu.project.calendar.implementation.mapper

import io.utu.project.calendar.api.ReservationCreate
import io.utu.project.calendar.api.ReservationSetName
import io.utu.project.calendar.api.ReservationSetResource
import io.utu.project.calendar.api.ReservationSetTimes
import io.utu.project.calendar.api.ReservationState
import io.utu.project.calendar.implementation.entity.ReservationEntity
import io.utu.project.calendar.implementation.repository.ReservationRepository
import java.util.UUID

abstract class ReservationMapper {
    protected abstract val repository: ReservationRepository

    protected fun ReservationCreate.toEntity(): ReservationEntity = ReservationEntity(
        resourceId = this.resourceId,
        state = ReservationState.DRAFT,
        start = this.start,
        end = this.end,
        resourceQuantity = this.quantity ?: 1,
        name = this.name,
    )

    protected fun ReservationEntity.update(update: ReservationSetTimes) = copy(
        start = update.start,
        end = update.end,
    )

    protected fun ReservationEntity.update(update: ReservationSetName) = copy(
        name = update.name,
    )

    protected fun ReservationEntity.update(update: ReservationSetResource) = copy(
        resourceId = update.resourceId,
        resourceQuantity = update.quantity,
    )

    protected fun ReservationEntity.cancel() = copy(
        state = ReservationState.CANCELLED
    )

    protected fun ReservationEntity.confirm() = copy(
        state = ReservationState.CONFIRMED
    )

    protected fun ReservationEntity.save() = repository.save(this)
    protected fun get(id: UUID) = repository.getOne(id)
}