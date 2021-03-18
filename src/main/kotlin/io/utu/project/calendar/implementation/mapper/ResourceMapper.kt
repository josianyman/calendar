package io.utu.project.calendar.implementation.mapper

import io.utu.project.calendar.api.ResourceCreate
import io.utu.project.calendar.implementation.entity.ResourceEntity
import io.utu.project.calendar.implementation.repository.ResourceRepository

abstract class ResourceMapper {
    abstract val repository: ResourceRepository

    protected fun ResourceCreate.create(): ResourceEntity = ResourceEntity(
        name = name,
        description = description,
        timezone = timezone,
        capacity = capacity,
    )
}