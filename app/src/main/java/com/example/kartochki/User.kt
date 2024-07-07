package com.example.kartochki


class User {
    var userId: String? = null
    var role: String? = null
    var phone: String? = null
    var email: String? = null
    var password: String? = null
    var address: String? = null
    var timestamp: String? = null
    var notes: String? = null
    private var iDs: String? = null

    constructor()
    constructor(
        userId: String?,
        role: String?,
        phone: String?,
        email: String?,
        password: String?,
        address: String?,
        timestamp: String?,
        notes: String?,
        iDs: String?
    ) {
        this.userId = userId
        this.role = role
        this.phone = phone
        this.email = email
        this.password = password
        this.address = address
        this.timestamp = timestamp
        this.notes = notes
        this.iDs = iDs
    }

    fun getiDs(): String? {
        return iDs
    }

    fun setiDs(iDs: String?) {
        this.iDs = iDs
    }
}
