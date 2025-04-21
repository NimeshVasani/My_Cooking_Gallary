package diamondcraft.devs.mycookinggallary.viewmodels

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import diamondcraft.devs.mycookinggallary.db.Cooking
import diamondcraft.devs.mycookinggallary.models.CookingFeedResponse
import diamondcraft.devs.mycookinggallary.models.CookingResponse
import diamondcraft.devs.mycookinggallary.other.Resources
import diamondcraft.devs.mycookinggallary.repositories.CookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CookingViewModel @Inject constructor(
    private val repository: CookingRepository
) :
    ViewModel(), LifecycleObserver {

    // Room Database Offline Saving

    fun addCooking(cooking: Cooking) = viewModelScope.launch {
        repository.addCooking(cooking)
    }

    fun deleteCooking(cooking: Cooking) = viewModelScope.launch {
        repository.deleteCooking(cooking)
    }

    fun getSavedCooking() = repository.getSavedCooking()


    //Retrofit Online Data Loading

    var allRecipes: MutableLiveData<Resources<CookingResponse>> = MutableLiveData()
    var allRecipesResponse: CookingResponse? = null


    var searchRecipes: MutableStateFlow<Resources<CookingResponse>> = MutableStateFlow<Resources<CookingResponse>>(Resources.Loading())


    var allFeeds: MutableLiveData<Resources<CookingFeedResponse>> = MutableLiveData()
    private var allFeedsResponse: CookingFeedResponse? = null


    val liveSharedData: LiveData<Cooking> = sharedCooking
    val liveSharedRelatedData: LiveData<MutableList<Cooking>> = sharedRelatedCooking

    companion object {
        var sharedCooking: MutableLiveData<Cooking> = MutableLiveData()
        var sharedRelatedCooking: MutableLiveData<MutableList<Cooking>> = MutableLiveData()
    }

    init {
        getAllRecipes()
        getAllFeeds()

    }

    private fun getAllRecipes() = viewModelScope.launch {
        allRecipes.postValue(Resources.Loading())
        val response = repository.getAllRecipes()
        allRecipes.postValue(handleAllRecipesResponse(response))
    }

    private fun getAllFeeds() = viewModelScope.launch {
        allFeeds.postValue(Resources.Loading())
        val response = repository.getAllFeeds()
        allFeeds.postValue(handleAllFeedsResponse(response))
    }

    fun updateSharedData(newValue: Cooking) = viewModelScope.launch {
        sharedCooking.value = newValue
    }

    fun updateRelatedSharedData(newValue: MutableList<Cooking>) = viewModelScope.launch {
        sharedRelatedCooking.value = newValue
    }

    private fun handleAllFeedsResponse(response: Response<CookingFeedResponse>): Resources<CookingFeedResponse> {
        if (response.isSuccessful) {
            response.body()?.let { feedResponseInner ->

                if (allFeedsResponse == null) {
                    allFeedsResponse = feedResponseInner
                } else {
                    val oldRecipes = allFeedsResponse?.results
                    val newRecipes = feedResponseInner.results
                    oldRecipes?.addAll(newRecipes)
                }
                Log.d("response", response.body().toString())
                return Resources.Success(allFeedsResponse ?: feedResponseInner)
            }
        }
        Log.d("response", "fail")

        return Resources.Error(response.message())

    }

    private fun handleAllRecipesResponse(response: Response<CookingResponse>): Resources<CookingResponse> {
        if (response.isSuccessful) {
            response.body()?.let { cookingResponse ->

                if (allRecipesResponse == null) {
                    allRecipesResponse = cookingResponse
                } else {
                    val oldRecipes = allRecipesResponse?.results
                    val newRecipes = cookingResponse.results

                    oldRecipes?.addAll(newRecipes)

                }
                Log.d("response", response.body().toString())
                return Resources.Success(allRecipesResponse ?: cookingResponse)
            }
        }
        Log.d("response", "fail")

        return Resources.Error(response.message())
    }


    fun searchRecipes(query: String) = viewModelScope.launch {

        val response = repository.searchRecipes(query)
        if (response.isSuccessful) {
            response.body()?.let { cookingResponse ->
              searchRecipes.value = Resources.Success(cookingResponse)
            }
        }
        else{
            searchRecipes.value = Resources.Error(response.message())
        }
    }


}
