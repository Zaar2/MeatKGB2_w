package com.zaar.meatKGB_w.model.useCase

import android.content.Context
import android.util.Log
import com.zaar.meatKGB_w.model.local.api_room.entityDb.ProductDb
import com.zaar.meatKGB_w.model.mappers.apiToDb.ProductMapperApiToDb
import com.zaar.meatKGB_w.model.repository.LocalDBRepositoryImpl
import com.zaar.meatKGB_w.model.repository.RemoteRepositoryImpl
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.coroutineContext

class UpdateProductUseCase(
    private val sessionId: String,
    private val enterpriseId: String,
    private val idWorkshop: Long = -1,
    private val myContext: Context
) {
    suspend fun executeWithReplace(): Boolean {
        val productsApi = RemoteRepositoryImpl().getProducts(
            sessionId = sessionId,
            idWorkshop = idWorkshop,
            enterpriseID = enterpriseId
        )
        return if (!productsApi.isNullOrEmpty()) {
            Log.d("TAG", "Dispatcher = ${coroutineContext[ContinuationInterceptor]},\n MESSAGE: CurrentThread[${Thread.currentThread().name}]")
            val productsDb = productsApi.run {
                val productsOutput = mutableListOf<ProductDb>()
                forEach {
                    productsOutput.add(ProductMapperApiToDb(it).execute())
                }
                productsOutput
            }
            val idsProductInserted = myContext.let {
                LocalDBRepositoryImpl(it)
                    .productsInsertWithReplace(productsDb)
            }
            idsProductInserted.size == productsDb.size
        } else false
    }

    suspend fun executeWithAppend(): Boolean {
        val productsApi = RemoteRepositoryImpl().getProducts(
            sessionId = sessionId,
            idWorkshop = idWorkshop,
            enterpriseID = enterpriseId
        )
        return if (!productsApi.isNullOrEmpty()) {
            val productsDb = productsApi.run {
                val productsOutput = mutableListOf<ProductDb>()
                forEach {
                    productsOutput.add(ProductMapperApiToDb(it).execute())
                }
                productsOutput
            }
            val idsProductInserted = myContext.let {
                LocalDBRepositoryImpl(it)
                    .productsInsert(productsDb)
            }
            idsProductInserted.size == productsDb.size
        } else false
    }
}