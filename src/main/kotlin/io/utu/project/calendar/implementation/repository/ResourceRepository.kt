package io.utu.project.calendar.implementation.repository

import io.utu.project.calendar.implementation.entity.ResourceEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ResourceRepository : JpaRepository<ResourceEntity, UUID>