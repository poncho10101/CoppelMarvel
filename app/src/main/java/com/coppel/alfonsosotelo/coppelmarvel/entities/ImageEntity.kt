package com.coppel.alfonsosotelo.coppelmarvel.entities

import com.coppel.alfonsosotelo.coppelmarvel.base.BaseEntity
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ImageEntity: RealmObject(), BaseEntity {
    @PrimaryKey
    open var path: String = ""
    open var extension: String = ""

    fun getFullPath() = "$path.$extension"

    override fun getPrimaryKey(): Any? {
        return path
    }
}