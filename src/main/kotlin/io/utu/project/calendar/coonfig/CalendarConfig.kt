package io.utu.project.calendar.coonfig

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EntityScan("io.utu.project.calendar.implementation.entity")
@EnableJpaRepositories("io.utu.project.calendar.implementation.repository")
@ComponentScan("io.utu.project.calendar.implementation.service", "io.utu.project.calendar.implementation.reader")
class CalendarConfig