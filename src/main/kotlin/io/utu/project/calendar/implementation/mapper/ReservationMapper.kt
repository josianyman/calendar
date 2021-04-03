package io.utu.project.calendar.implementation.mapper

import io.utu.project.calendar.api.ReservationCreate
import io.utu.project.calendar.api.ReservationSetName
import io.utu.project.calendar.api.ReservationSetResource
import io.utu.project.calendar.api.ReservationSetTimes
import io.utu.project.calendar.api.ReservationState
import io.utu.project.calendar.api.ResourceAvailabilityApi
import io.utu.project.calendar.implementation.entity.ReservationEntity
import io.utu.project.calendar.implementation.repository.ReservationRepository
import java.util.UUID

abstract class ReservationMapper {
    protected abstract val repository: ReservationRepository
    protected abstract val availabilityReader: ResourceAvailabilityApi

    protected fun ReservationCreate.toEntity(): ReservationEntity = ReservationEntity(
        resourceId = this.resourceId,
        state = ReservationState.DRAFT,
        start = this.start,
        end = this.end,
        resourceQuantity = this.resourceQuantity ?: 1,
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
        resourceQuantity = update.resourceQuantity,
    )

    protected fun ReservationEntity.cancel() = copy(
        state = ReservationState.CANCELLED
    )

    protected fun ReservationEntity.confirm() = copy(
        state = ReservationState.CONFIRMED
    )

    protected fun ReservationEntity.requireNotCancelled(): ReservationEntity {
        require(state != ReservationState.CANCELLED) { "Cancelled reservation is not updatable!" }
        return this
    }

    protected fun ReservationEntity.requireDraftState(): ReservationEntity {
        require(state == ReservationState.DRAFT) { "Only draft reservation could be confirmed!" }
        return this
    }

    protected fun ReservationEntity.requireResourceAvailable(): ReservationEntity {
        require(availabilityReader.isAvailableFor(this)) {
            "Resource is already reserved!"
        }
        return this
    }

    protected fun ReservationEntity.save() = repository.save(this)
    protected fun get(id: UUID) = repository.getOne(id)
}