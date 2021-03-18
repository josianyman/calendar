package io.utu.project.calendar.implementation.entity

import io.utu.project.calendar.api.Resource
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "resource")
data class ResourceEntity(
    @Id
    override val id: UUID = UUID.randomUUID(),
    override val name: String,
    override val description: String,
    override val timezone: String,
    override val capacity: Int,
    @CreationTimestamp
    override val createdAt: Instant = Instant.now(),
    @UpdateTimestamp
    override val updatedAt: Instant = Instant.now(),
) : Resource