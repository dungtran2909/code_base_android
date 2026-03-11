package com.dungtran.codebase.data.remote.api

import com.dungtran.codebase.data.remote.dto.ProductDto
import retrofit2.http.GET

interface ProductApi {
    @GET("products")
    suspend fun getProducts(): List<ProductDto>
}

