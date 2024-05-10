package com.zaar.meatKGB_w.model.repository

import com.zaar.meatKGB_w.data.LogPass
import com.zaar.meatKGB_w.model.remote.api_retrofit.entityApi.ProductApi
import com.zaar.meatKGB_w.model.remote.api_retrofit.entityApi.RecordApi
import com.zaar.meatKGB_w.model.remote.api_retrofit.entityApi.ShopApi
import com.zaar.meatKGB_w.model.remote.api_retrofit.entityApi.UserApi

interface RemoteRepository {
    suspend fun identificationUser(logPass: LogPass): String
    suspend fun verificationSessionId(sessionId: String): Boolean
    suspend fun getWorker(sessionId: String, enterpriseID: String): UserApi?
    suspend fun getShop(sessionId: String, enterpriseID: String, idWorkshop: Long): ShopApi?
    suspend fun getProducts(sessionId: String, enterpriseID: String, idWorkshop: Long): List<ProductApi>?
    suspend fun postRecord(recordApi: RecordApi, sessionId: String): List<RecordApi>?
    suspend fun deleteRecord(sessionId: String, idRecord: Long, idUser: Long, enterpriseId: String): List<RecordApi>?
    suspend fun getRecords(sessionId: String, idUser: Long, idEnterprise: String): List<RecordApi>?
}