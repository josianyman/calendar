package io.utu.project.utils

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(value = [TestConfig::class])
@ContextConfiguration(initializers = [TestBase.ContainerInitializer::class])
abstract class TestBase {

    @Autowired
    protected lateinit var initializer: DataInitializer

    internal class ContainerInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            applicationContext.environment.propertySources.addFirst(
                POSTGRES_INSTANCE.getPropertySource()
            )
        }
    }

    @Test
    fun contextLoads() {
    }
}