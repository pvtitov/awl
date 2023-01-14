package com.paveltitov.wishlist.di.factories

import android.app.Activity
import com.paveltitov.wishlist.core.DataStorage
import com.paveltitov.wishlist.data.drive.GoogleDriveStore
import java.lang.ref.WeakReference

class DataStorageFactory(activity: Activity) : Factory<DataStorage> {

    private val activityWeakReference = WeakReference(activity)

    override fun create(): DataStorage {
        return GoogleDriveStore(activityWeakReference)
    }
}