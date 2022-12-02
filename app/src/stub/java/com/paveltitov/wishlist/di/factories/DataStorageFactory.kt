package com.paveltitov.wishlist.di.factories

import com.paveltitov.wishlist.core.DataStorage
import com.paveltitov.wishlist.data.StubDataStorage

class DataStorageFactory: Factory<DataStorage> {
    override fun create(): DataStorage {
        return StubDataStorage()
    }
}