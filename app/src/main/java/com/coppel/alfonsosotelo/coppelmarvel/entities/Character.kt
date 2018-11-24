package com.coppel.alfonsosotelo.coppelmarvel.entities

import com.coppel.alfonsosotelo.coppelmarvel.base.BaseEntity
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Character: RealmObject(), BaseEntity {
    @PrimaryKey
    open var id: Long = 0
    open var name: String = ""
    open var description: String = ""
    open var modified: Date = Date()
    open var resourceURI: String = ""
    open var thumbnail: ImageEntity? = null

    override fun getPrimaryKey(): Any? {
        return id
    }
}