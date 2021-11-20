package com.artofmainstreams.examples.ui.example01

class TodoModel {
    var title: String? = null
    var imageUrl: String? = null
}

class TodoSectionModel {
    var title: String = ""
    var items: ArrayList<TodoModel> = ArrayList()
}