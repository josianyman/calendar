package io.utu.project.graphql.config

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import java.time.Instant

class TimestampCoercing(
    private val mapper: ObjectMapper
) : Coercing<Instant, Instant> {

    private fun convertImpl(input: Any?): Instant? {
        if (input is String) {
            return mapper.readValue(input, object : TypeReference<Instant>() {})
        }
        return null
    }

    override fun parseValue(input: Any?): Instant? {
        return convertImpl(input)
            ?: throw CoercingParseValueException("Invalid value '${input.toString()}' for Timestamp")
    }

    override fun parseLiteral(input: Any?): Instant? {
        if (input !is StringValue) return null
        val value: String = input.value
        return convertImpl(value)
    }

    override fun serialize(dataFetcherResult: Any?): Instant? {
        return if (dataFetcherResult is Instant) {
            dataFetcherResult
        } else {
            val result = convertImpl(dataFetcherResult)
                ?: throw CoercingSerializeException("Invalid value '$dataFetcherResult' for Instant")
            result
        }
    }
}