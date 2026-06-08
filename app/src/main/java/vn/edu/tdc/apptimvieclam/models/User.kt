package vn.edu.tdc.apptimvieclam.models

import java.io.Serializable

class User : Serializable {
    var uid: String

    var name: String

    var email: String

    var role: String

    var status: String

    constructor(uid: String, name: String, email: String, role: String, status: String) {
        this.uid = uid
        this.name = name
        this.email = email
        this.role = role
        this.status = status
    }

    constructor() {
        uid = ""
        name = ""
        email = ""
        role = ""
        status = ""
    }
}
