package com.coppel.alfonsosotelo.coppelmarvel.entities

import com.coppel.alfonsosotelo.coppelmarvel.base.BaseEntity
import com.vicpin.krealmextensions.queryFirst
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User: RealmObject(), BaseEntity {
    @PrimaryKey
    open var id: Long = 0
    open var username: String = ""
    open var password: String = ""

    override fun getPrimaryKey(): Any? {
        return id
    }

    companion object {
        fun isLoggedIn(): Boolean {
            return queryFirst<User>() != null
        }

        fun getLoggedUser(): User? {
            return queryFirst()
        }
    }
}