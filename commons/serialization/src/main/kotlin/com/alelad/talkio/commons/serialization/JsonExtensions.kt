package com.alelad.talkio.commons.serialization

import com.alelad.talkio.commons.serialization.JsonExtensions.DEFAULT_OBJECT_MAPPER
import com.alelad.talkio.commons.serialization.JsonExtensions.javaType
import com.alelad.talkio.commons.serialization.JsonExtensions.listType
import com.alelad.talkio.commons.serialization.JsonExtensions.mapType
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.type.CollectionType
import com.fasterxml.jackson.databind.type.MapType
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.afterburner.AfterburnerModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule

object JsonExtensions {
    val DEFAULT_OBJECT_MAPPER = configureDefaultObjectMapper(
        ObjectMapper()
    )

    private fun configureDefaultObjectMapper(objectMapper: ObjectMapper = ObjectMapper()): ObjectMapper {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        objectMapper.enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)

        objectMapper.registerModule(Jdk8Module())
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.registerModule(ParameterNamesModule())
        objectMapper.registerModule(AfterburnerModule())
        objectMapper.registerKotlinModule()

        return objectMapper
    }

    fun javaType(objectMapper: ObjectMapper, clazz: Class<*>): JavaType =
        objectMapper.typeFactory.constructType(clazz)

    fun listType(objectMapper: ObjectMapper, clazz: Class<*>): CollectionType =
        objectMapper.typeFactory.constructCollectionType(ArrayList::class.java, clazz)

    fun mapType(objectMapper: ObjectMapper, keyClass: Class<*>, valueClass: Class<*>): MapType =
        objectMapper.typeFactory.constructMapType(HashMap::class.java, keyClass, valueClass)

}

// Serialization
fun Any.toJson(objectMapper: ObjectMapper): String = objectMapper.writeValueAsString(this)

fun Any.toJson(): String = this.toJson(DEFAULT_OBJECT_MAPPER)

// Deserialization
fun <T> String.fromJson(objectMapper: ObjectMapper, javaType: JavaType): T =
    objectMapper.readValue(this, javaType)

inline fun <reified T : Any> String.fromJson(objectMapper: ObjectMapper): T =
    this.fromJson(objectMapper, javaType(objectMapper, T::class.java))

inline fun <reified T : Any> String.fromJson(): T =
    this.fromJson(DEFAULT_OBJECT_MAPPER)

inline fun <reified T : Any> String.fromJsonList(objectMapper: ObjectMapper): List<T> =
    this.fromJson(objectMapper, listType(objectMapper, T::class.java))

inline fun <reified T : Any> String.fromJsonList(): List<T> =
    this.fromJsonList(DEFAULT_OBJECT_MAPPER)

inline fun <reified K : Any, reified V : Any> String.fromJsonMap(objectMapper: ObjectMapper): Map<K, V> =
    this.fromJson(objectMapper, mapType(objectMapper, K::class.java, V::class.java))

inline fun <reified K : Any, reified V : Any> String.fromJsonMap(): Map<K, V> =
    this.fromJsonMap(DEFAULT_OBJECT_MAPPER)

inline fun <reified T : Any> Map<String, Any>.fromMap(objectMapper: ObjectMapper): T =
    objectMapper.convertValue(this, javaType(objectMapper, T::class.java))

inline fun <reified T : Any> Map<String, Any>.fromMap(): T =
    this.fromMap(DEFAULT_OBJECT_MAPPER)
