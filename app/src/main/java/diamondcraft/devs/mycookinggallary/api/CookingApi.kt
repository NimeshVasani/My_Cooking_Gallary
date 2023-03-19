package diamondcraft.devs.mycookinggallary.api

import diamondcraft.devs.mycookinggallary.models.CookingFeedResponse
import diamondcraft.devs.mycookinggallary.models.CookingResponse
import diamondcraft.devs.mycookinggallary.models.FeedResponseInner
import diamondcraft.devs.mycookinggallary.other.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CookingApi {

    @Headers(
        "X-RapidAPI-Key: ebaab27037msh88758eaa457c9f3p1a0b33jsn4f4a19666280",
        "X-RapidAPI-Host: tasty.p.rapidapi.com"
    )
    @GET("recipes/list")
    suspend fun getAllRecipes(
        @Query("size")
        size: Int = 300

    ): Response<CookingResponse>

    @Headers(
        "X-RapidAPI-Key: ebaab27037msh88758eaa457c9f3p1a0b33jsn4f4a19666280",
        "X-RapidAPI-Host: tasty.p.rapidapi.com"
    )
    @GET("feeds/list")
    suspend fun getAllFeeds(
        @Query("X-RapidAPI-Key")
        apiKey: String = API_KEY
    ): Response<CookingFeedResponse>
}