package io.utu.project.calendar.implementation.reader

import io.utu.project.calendar.api.Resource
import io.utu.project.calendar.api.ResourceReaderApi
import io.utu.project.calendar.implementation.mapper.ResourceMapper
import io.utu.project.calendar.implementation.repository.ResourceRepository
import io.utu.project.util.RepositoryReader
import io.utu.project.util.RepositoryReaderDelegate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class ResourceReader(
    private val repository: ResourceRepository
) : ResourceReaderApi, RepositoryReader<Resource, UUID> by RepositoryReaderDelegate(repository)