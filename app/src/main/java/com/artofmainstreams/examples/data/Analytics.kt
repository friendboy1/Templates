package com.artofmainstreams.examples.data

import javax.inject.Inject

/**
 * Добавляется Inject, чтобы явно не прописывать Provide объекта с обычным конструктором в Module
 */
class Analytics @Inject constructor() {

    fun trackScreenShow() {
    }

    fun trackNewsRequest(newsId: String) {
        // Do nothing
    }
}