package diamondcraft.devs.mycookinggallary.viewmodels


import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import diamondcraft.devs.mycookinggallary.models.User
import diamondcraft.devs.mycookinggallary.other.Resources
import diamondcraft.devs.mycookinggallary.repositories.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun signInUserWithFirebase(email: String, password: String): MutableLiveData<Resources<User>> {
        return authRepository.signInWithFirebase(email = email, password = password)
    }

    fun registerUserWithFirebase(
        displayName: String,
        email: String,
        password: String
    ): MutableLiveData<Resources<User>> {
        return authRepository.registerUserWithFirebase(displayName, email, password)
    }

    fun updateProfile(displayName: String): MutableLiveData<Resources<Boolean>> {
        return authRepository.updateProfile(displayName = displayName)
    }

    fun checkLoginSession(): Boolean {
        return authRepository.checkLoginSession()
    }

    fun checkPhoneSession(): Boolean {
        return authRepository.checkPhoneSession()
    }

    fun getCurrentUser(): FirebaseUser {
        return authRepository.getCurrentUser()
    }


    fun verifyPhone(
        phoneNum: String,
        activity: Activity
    ) {
        authRepository.verifyPhone(phoneNum = phoneNum, activity = activity)
    }

    fun initCallbacks(
        onVerificationCompleted: (PhoneAuthCredential) -> Unit,
        onVerificationFailed: (FirebaseException) -> Unit,
        onCodeSent: (String, PhoneAuthProvider.ForceResendingToken) -> Unit
    ) {
        authRepository.initCallbacks(onVerificationCompleted, onVerificationFailed, onCodeSent)
    }

    fun verifyPhoneNumberWithCode(code: String): PhoneAuthCredential {
        return authRepository.verifyPhoneNumberWithCode(code)
    }

    fun logout(){
        authRepository.logout()
    }

}