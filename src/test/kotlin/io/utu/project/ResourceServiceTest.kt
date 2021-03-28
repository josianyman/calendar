package io.utu.project

import io.utu.project.calendar.api.ResourceApi
import io.utu.project.calendar.api.ResourceReaderApi
import io.utu.project.utils.CREATE_RESOURCE
import io.utu.project.utils.TestBase
import junit.framework.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ResourceServiceTest : TestBase() {
    @Autowired
    private lateinit var resourceService: ResourceApi

    @Autowired
    private lateinit var resourceReader: ResourceReaderApi

    @Test
    fun createResource() {
        val resource = resourceService.create(CREATE_RESOURCE)
        val read = resourceReader.getOne(resource.id)
        assertEquals(resource, read)
        assertEquals(resource.name, CREATE_RESOURCE.name)
        assertEquals(resource.description, CREATE_RESOURCE.description)
        assertEquals(resource.timezone, CREATE_RESOURCE.timezone)
        assertEquals(resource.capacity, CREATE_RESOURCE.capacity)
    }
}