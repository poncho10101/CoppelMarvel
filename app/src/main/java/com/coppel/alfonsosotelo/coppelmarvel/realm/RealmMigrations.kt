package com.coppel.alfonsosotelo.coppelmarvel.realm

import io.realm.DynamicRealm
import io.realm.RealmMigration


class RealmMigrations: RealmMigration {
    /**
     * Realm migrations need to be added here
     */
    override fun migrate(realm: DynamicRealm?, oldVersion: Long, newVersion: Long) {
//        if(realm != null) {
//            val schema = realm.schema
//
//            if (oldVersion == 1L) {
//                val userSchema = schema.initialize("User")
//                userSchema!!.addField("test", Int::class.java)
//            }
//        }
    }
}