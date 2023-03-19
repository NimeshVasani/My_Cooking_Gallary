package diamondcraft.devs.mycookinggallary.repositories

import diamondcraft.devs.mycookinggallary.api.CookingApi
import diamondcraft.devs.mycookinggallary.db.Cooking
import diamondcraft.devs.mycookinggallary.db.CookingDAO
import diamondcraft.devs.mycookinggallary.models.CookingFeedResponse
import diamondcraft.devs.mycookinggallary.models.CookingResponse
import diamondcraft.devs.mycookinggallary.models.FeedResponseInner
import retrofit2.Response
import javax.inject.Inject

class CookingRepository @Inject constructor(
    private val dao: CookingDAO,
    private val api: CookingApi
) {

    suspend fun addCooking(cooking: Cooking) = dao.addCooking(cooking)
    suspend fun deleteCooking(cooking: Cooking) = dao.deleteCooking(cooking)
    fun getSavedCooking() = dao.getAll()

    suspend fun getAllRecipes(): Response<CookingResponse> = api.getAllRecipes()

    suspend fun getAllFeeds() : Response<CookingFeedResponse> = api.getAllFeeds()

}