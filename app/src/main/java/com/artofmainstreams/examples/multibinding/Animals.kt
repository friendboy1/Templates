package com.artofmainstreams.examples.multibinding

import javax.inject.Inject

interface Animal

class Cat @Inject constructor() : Animal

class Dog @Inject constructor() : Animal

class Mouse @Inject constructor() : Animal