package com.artofmainstreams.examples.multibinding

fun main() {
    val appComponent: ZooComponent = DaggerZooComponent.create()
    println(appComponent.zoo.count())
}