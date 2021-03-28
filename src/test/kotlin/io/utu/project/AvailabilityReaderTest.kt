package io.utu.project

import io.utu.project.calendar.api.ResourceAvailabilityApi
import io.utu.project.calendar.api.ResourceAvailabilityInput
import io.utu.project.utils.TestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Random

class AvailabilityReaderTest : TestBase() {
    @Autowired
    private lateinit var availabilityReader: ResourceAvailabilityApi

    @Test
    fun availability() {
        val resource = initializer.initResource()
        assertEquals(50, resource.capacity)
        val reservations = (1..5)
            .map { initializer.initReservation(resource.id, 10, start, end) }
        val input = ResourceAvailabilityInput(
            start = start.minusHours(1),
            end = end.plusHours(1),
            slotSize = 60
        )
        val capacity = availabilityReader.getAvailability(resource, input)
        // 7 - 8
        assertEquals(50, capacity[0].available)
        // 8 - 9
        assertEquals(0, capacity[1].available)
        // 9 - 10
        assertEquals(0, capacity[2].available)
        // 10 - 11
        assertEquals(50, capacity[3].available)
    }

    @Test
    @Disabled(
        "Run performance test only manually by removing this annotation" +
            "Test execution takes several minutes, because reservations are created a huge amount"
    )
    fun performance() {
        val resources100 = (1..100).map {
            initializer.initResource()
        }
        // Generate 100 000 reservations
        (1..10).forEach {
            println("Creating first ${it * 10000}...")
            initializer.createAll((1..10000).map {
                val start = randomStart()
                initializer.createInput(
                    resourceId = resources100.random().id,
                    quantity = randomQuantity(),
                    start = start,
                    end = start.plusMinutes(randomDuration().toLong())
                )
            })
        }
        (1..10).map {
            val before = Instant.now()
            availabilityReader.getAvailability(resources100.random(), input)
            val after = Instant.now()
            val duration = Duration.between(before, after).toMillis()
            println("Performance: $duration milliseconds")
            assertTrue(duration < 500)
        }
    }

    private val random = Random(10)

    fun randomQuantity() = random.nextInt(40) + 1
    fun randomStart() = start.plusHours((random.nextInt(24 * 80) - 24 * 40).toLong())
    fun randomDuration() = random.nextInt(30 * 24) + 15
    val start: LocalDateTime = LocalDateTime.of(2021, 3, 30, 8, 0)
    val end: LocalDateTime = LocalDateTime.of(2021, 3, 30, 10, 0)
    val input = ResourceAvailabilityInput(
        start = start.toLocalDate().atStartOfDay(),
        end = end.toLocalDate().atTime(LocalTime.MAX),
        slotSize = 15
    )
}