package vn.edu.tdc.apptimvieclam.models

import java.io.Serializable

class User : Serializable {
    var uid: String

    var fullName: String

    var email: String

    var role: String

    var status: String

    constructor(uid: String, fullName: String, email: String, role: String, status: String) {
        this.uid = uid
        this.fullName = fullName
        this.email = email
        this.role = role
        this.status = status
    }

    constructor() {
        uid = ""
        fullName = ""
        email = ""
        role = ""
        status = ""
    }
}
