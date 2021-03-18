package io.utu.project

import io.utu.project.calendar.api.ReservationState
import io.utu.project.calendar.implementation.OwnerEmbeddable
import io.utu.project.calendar.implementation.entity.ReservationEntity
import io.utu.project.calendar.implementation.repository.ReservationRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.UUID

@SpringBootTest
class ProjectApplicationTests {
    @Autowired
    lateinit var repository: ReservationRepository

	@Test
	fun contextLoads() {
	}

    @Test
    fun reservationsSave() {
        repository.save(RESERVATION)
        assertThat(RESERVATION).usingRecursiveComparison()
            .ignoringFields("createdAt", "updatedAt")
            .isEqualTo(repository.getOne(RESERVATION.id))
    }

}

val RESERVATION = ReservationEntity(
    resourceId = "1",
    owner = OwnerEmbeddable(UUID.randomUUID(), "USER"),
    resourceQuantity = 2,
    name = "Test name",
    description = "Test description",
    start = LocalDateTime.of(2021,3,21,10,0),
    end = LocalDateTime.of(2021,3,21,17,0),
    color = null,
    state = ReservationState.CONFIRMED,
)
