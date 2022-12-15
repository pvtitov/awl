package com.paveltitov.wishlist.di.factories

import com.paveltitov.wishlist.core.DataStorage
import com.paveltitov.wishlist.data.drive.GoogleDriveStore

class DataStorageFactory : Factory<DataStorage> {
    override fun create(): DataStorage {
        return GoogleDriveStore()
    }
}