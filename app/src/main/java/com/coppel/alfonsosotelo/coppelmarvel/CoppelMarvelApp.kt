package com.coppel.alfonsosotelo.coppelmarvel

import android.app.Application
import com.coppel.alfonsosotelo.coppelmarvel.realm.RealmMigrations
import com.coppel.alfonsosotelo.coppelmarvel.utils.Globals
import com.squareup.leakcanary.LeakCanary
import io.realm.Realm
import io.realm.RealmConfiguration

class CoppelMarvelApp: Application() {

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }

        LeakCanary.install(this)

        //Init realm database
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name(Globals.DB_NAME)
            .schemaVersion(Globals.DB_SCHEMA_VERSION)
            .migration(RealmMigrations())

        if(Globals.DEBUG)
            config.deleteRealmIfMigrationNeeded() // Debug only, data will delete if change the schema

        Realm.setDefaultConfiguration(config.build())
    }

    override fun onTerminate() {
        Realm.getDefaultInstance().close()
        super.onTerminate()
    }
}