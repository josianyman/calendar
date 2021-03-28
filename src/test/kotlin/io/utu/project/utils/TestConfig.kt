package io.utu.project.utils

import io.utu.project.ProjectApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@ComponentScan(basePackages = ["io.utu.project.utils"])
@Import(value = [ProjectApplication::class])
class TestConfig