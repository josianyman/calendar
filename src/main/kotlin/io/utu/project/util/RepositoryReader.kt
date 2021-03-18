package io.utu.project.util

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import java.io.Serializable
import java.util.Optional

@Transactional(readOnly = true)
interface RepositoryReader<T, ID> {
    fun findAll(): List<T>
    fun findAll(sort: Sort): List<T>
    fun findAllById(ids: Iterable<ID>): List<T>
    fun getOne(id: ID): T
    fun findAll(pageable: Pageable): Page<T>
    fun existsById(id: ID): Boolean
    fun count(): Long
    fun findById(id: ID): Optional<T>
}

@Transactional(readOnly = true)
class RepositoryReaderDelegate<T, ID : Serializable>(
    private val repository: JpaRepository<out T, ID>
) : RepositoryReader<T, ID> {

    // Delegate all calls to jpa repository
    override fun findAll(): List<T> = repository.findAll()
    override fun findAll(sort: Sort): List<T> = repository.findAll(sort)
    override fun findAllById(ids: Iterable<ID>): List<T> = repository.findAllById(ids)
    override fun getOne(id: ID): T = repository.getOne(id)
    override fun findAll(pageable: Pageable): Page<T> = repository.findAll(pageable).map { it.runCallback() }
    override fun existsById(id: ID): Boolean = repository.existsById(id)
    override fun count(): Long = repository.count()
    override fun findById(id: ID): Optional<T> = repository.findById(id).map { it.runCallback() }

    // Type conversion to supertype
    private fun T.runCallback(): T { return this }
}
