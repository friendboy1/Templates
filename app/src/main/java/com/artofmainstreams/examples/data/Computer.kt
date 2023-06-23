package com.artofmainstreams.examples.data

class Processor {
    override fun toString() = "Intel Core i7"
}

class Motherboard {
    override fun toString() = "Gigabyte"
}

class RAM {
    override fun toString() = "64GB"
}

data class Computer(
    val processor: Processor,
    val motherboard: Motherboard,
    val ram: RAM
)