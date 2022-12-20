package com.paveltitov.wishlist.di.factories

import android.app.Activity
import com.paveltitov.wishlist.core.DataStorageCoroutines
import com.paveltitov.wishlist.data.drive.GoogleDriveStore
import java.lang.ref.WeakReference

class DataStorageFactory(activity: Activity) : Factory<DataStorageCoroutines> {

    private val activityWeakReference = WeakReference(activity)

    override fun create(): DataStorageCoroutines {
        return GoogleDriveStore(activityWeakReference)
    }
}