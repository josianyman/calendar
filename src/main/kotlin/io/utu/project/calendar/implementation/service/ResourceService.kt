package io.utu.project.calendar.implementation.service

import io.utu.project.calendar.api.Resource
import io.utu.project.calendar.api.ResourceApi
import io.utu.project.calendar.api.ResourceCreate
import io.utu.project.calendar.implementation.mapper.ResourceMapper
import io.utu.project.calendar.implementation.repository.ResourceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ResourceService(
    override val repository: ResourceRepository
) : ResourceApi, ResourceMapper() {
    override fun create(create: ResourceCreate): Resource {
        return repository.save(create.create())
    }
}

