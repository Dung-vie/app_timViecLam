package vn.edu.tdc.apptimvieclam.models

import java.io.Serializable

class Notification : Serializable {
    var title: String

    var message: String

    var type: String

    var isRead: Boolean

    var createdAt: Long

    constructor(createdAt: Long, isRead: Boolean, type: String, message: String, title: String) {
        this.createdAt = createdAt
        this.isRead = isRead
        this.type = type
        this.message = message
        this.title = title
    }

    constructor() {
        this.createdAt = 0L
        this.isRead = false
        this.type = ""
        this.message = ""
        this.title = ""
    }
}