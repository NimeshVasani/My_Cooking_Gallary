package diamondcraft.devs.mycookinggallary.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import diamondcraft.devs.mycookinggallary.repositories.CookingRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CookingViewModelProviderFactory @Inject constructor(private var repository: CookingRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CookingViewModel(repository = repository) as T
    }
}