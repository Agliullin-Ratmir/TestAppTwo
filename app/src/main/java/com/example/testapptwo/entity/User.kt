package com.example.testapptwo.entity

class User(
        var age: Int?,
        var name: String?,
        var id: Int
) {
    constructor() : this(-1, "", -1)
}