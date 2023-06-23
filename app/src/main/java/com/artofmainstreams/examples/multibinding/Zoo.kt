package com.artofmainstreams.examples.multibinding

import javax.inject.Inject

class Zoo @Inject constructor(
    private val animals: Set<@JvmSuppressWildcards Animal>
) {
    fun count() = animals.size
}