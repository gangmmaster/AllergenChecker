package com.example.allergenchecker.data.realm

import io.github.xilinjia.krdb.types.RealmObject
import io.github.xilinjia.krdb.types.annotations.PrimaryKey

class Additive : RealmObject {
    @PrimaryKey var id: Long = 0
    var marker: String = ""
    var description: String = ""
    var riskLevel: String = ""
}