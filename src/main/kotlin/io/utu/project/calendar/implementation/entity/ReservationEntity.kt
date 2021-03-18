package io.utu.project.calendar.implementation.entity

import io.utu.project.calendar.api.Reservation
import io.utu.project.calendar.api.ReservationState
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "reservation")
data class ReservationEntity(
    @Id
    override val id: UUID = UUID.randomUUID(),
    override val resourceId: UUID,
    override val name: String?,
    @Column(name = "start_time")
    override val start: LocalDateTime,
    @Column(name = "end_time")
    override val end: LocalDateTime,
    @Enumerated(EnumType.STRING)
    override val state: ReservationState,
    @Column(name = "quantity")
    override val resourceQuantity: Int,
    @CreationTimestamp
    override val createdAt: Instant = Instant.now(),
    @UpdateTimestamp
    override val updatedAt: Instant = Instant.now()
) : Reservation
