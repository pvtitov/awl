package com.paveltitov.wishlist.di

import com.paveltitov.wishlist.di.factories.Factory
import kotlin.reflect.KClass

object DI {

    private val factories: MutableMap<KClass<out Any>, Factory<out Any>> = mutableMapOf()
    private val dependencies: MutableMap<KClass<out Any>, Any> = mutableMapOf()

    fun setFactory(clazz: KClass<out Any>, factory: Factory<out Any>) {
        factories[clazz] = factory
    }

    fun get(clazz: KClass<out Any>): Any {
        return dependencies[clazz]
            ?: factories[clazz]?.create()?.also { dependencies[clazz] = it }
            ?: throw IllegalStateException("DI failed to provide ${clazz.simpleName}")
    }
}