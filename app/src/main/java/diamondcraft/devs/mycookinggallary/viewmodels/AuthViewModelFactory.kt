package diamondcraft.devs.mycookinggallary.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import diamondcraft.devs.mycookinggallary.repositories.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthViewModelFactory @Inject constructor(private var repository: AuthRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(authRepository = repository) as T
    }
}