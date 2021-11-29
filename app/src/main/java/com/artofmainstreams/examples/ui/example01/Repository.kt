package com.artofmainstreams.examples.ui.example01

fun getItems(): ArrayList<TodoSectionModel> {
    val items = ArrayList<TodoSectionModel>()
    val todos = ArrayList<TodoModel>()

    val todo1 = TodoModel()
    todo1.title = "Todo 1"
    //todo1.imageUrl = "https://cdn.pixabay.com/photo/2016/03/09/15/30/girl-1246690_960_720.jpg"
    todo1.imageUrl = "https://get.wallhere.com/photo/landscape-mountains-sunset-nature-sunrise-cliff-national-park-canyon-Grand-Canyon-Formation-Terrain-mountain-landform-geographical-feature-badlands-249045.jpg"

    todos.add(todo1)

    val todo2 = TodoModel()
    todo2.title = "Todo 2"
    todo2.imageUrl = "https://cdn.pixabay.com/photo/2016/03/09/16/48/street-1246852_960_720.jpg"

    todos.add(todo2)

    val todo3 = TodoModel()
    todo3.title = "Todo 3"
    todo3.imageUrl = "https://cdn.pixabay.com/photo/2016/03/09/16/49/medieval-1246853_960_720.jpg"

    todos.add(todo3)

    val todo4 = TodoModel()
    todo4.title = "Todo 4"
    todo4.imageUrl = "https://cdn.pixabay.com/photo/2016/03/09/16/47/tattoo-1246840_960_720.jpg"

    todos.add(todo4)

    val item1 = TodoSectionModel()
    item1.title = "Free-Photos from pixabay.com"
    item1.items.addAll(todos)

    items.add(item1)

    val item2 = TodoSectionModel()
    item2.title = "Free-Photos from pixabay.com"
    item2.items.addAll(todos)

    items.add(item2)

    return items
}