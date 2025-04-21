package diamondcraft.devs.mycookinggallary.repositories


import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import diamondcraft.devs.mycookinggallary.models.User
import diamondcraft.devs.mycookinggallary.other.Resources
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var verificationId: String


    fun registerUserWithFirebase(
        displayName: String,
        email: String,
        password: String
    ): MutableLiveData<Resources<User>> {
        val authenticationData = MutableLiveData<Resources<User>>()
        authenticationData.value = Resources.Loading()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isComplete) {
                    if (task.isSuccessful) {
                        val isNewUser: Boolean = task.result.additionalUserInfo?.isNewUser ?: false
                        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
                        firebaseUser?.let {
                            firebaseUser.updateProfile(buildProfileUpdateRequest(displayName))
                                .addOnCompleteListener { task ->
                                    if (task.isComplete) {
                                        if (task.isSuccessful) {
                                            val uId = firebaseUser.uid
                                            val name = firebaseUser.displayName
                                            val userEmail = firebaseUser.email
                                            val user = User(name!!, userEmail!!, uId)
                                            authenticationData.value = Resources.Success(user)
                                        } else {
                                            authenticationData.value =
                                                Resources.Error(task.exception?.message.toString())
                                        }
                                    } else {
                                        authenticationData.value =
                                            Resources.Error(task.exception?.message.toString())
                                    }
                                }
                        }
                    } else {
                        authenticationData.value =
                            Resources.Error(task.exception?.message.toString())
                    }
                } else {
                    authenticationData.value = Resources.Error(task.exception?.message.toString())
                }
            }
        return authenticationData
    }

    fun signInWithFirebase(email: String, password: String): MutableLiveData<Resources<User>> {
        val authenticationData = MutableLiveData<Resources<User>>()
        authenticationData.value = Resources.Loading()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                authenticationData.value = Resources.Loading()
                if (task.isSuccessful) {
                    val isNewUser: Boolean = task.result.additionalUserInfo?.isNewUser ?: false
                    val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
                    firebaseUser?.let {
                        val uId = firebaseUser.uid
                        val name = firebaseUser.displayName
                        val userEmail = firebaseUser.email
                        val user = User(name ?: "", userEmail!!, uId)

                        authenticationData.value = Resources.Success(user)
                    }
                } else {
                    Log.d("firebase User is null", "${task.exception?.message}")
                    authenticationData.value = Resources.Error(task.exception?.message.toString())
                }
            }
        return authenticationData
    }

    fun updateProfile(displayName: String): MutableLiveData<Resources<Boolean>> {
        val isUpdated: MutableLiveData<Resources<Boolean>> = MutableLiveData()
        firebaseAuth.currentUser!!.updateProfile(
            buildProfileUpdateRequest(
                displayName = displayName
            )
        ).addOnCompleteListener { task ->
            if (task.isComplete) {
                if (task.isSuccessful) {
                    isUpdated.value = Resources.Success(true)
                } else {
                    isUpdated.value = Resources.Error(task.exception?.message.toString())
                }
            } else {
                isUpdated.value = Resources.Loading()
            }
        }
        return isUpdated
    }

    fun checkLoginSession(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun checkPhoneSession(): Boolean {
        return firebaseAuth.currentUser!!.phoneNumber.isNullOrBlank()
    }

    fun getCurrentUser(): FirebaseUser {
        return firebaseAuth.currentUser!!
    }


    fun initCallbacks(
        onVerificationCompleted: (PhoneAuthCredential) -> Unit,
        onVerificationFailed: (FirebaseException) -> Unit,
        onCodeSent: (String, PhoneAuthProvider.ForceResendingToken) -> Unit
    ) {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                onVerificationCompleted(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                onVerificationFailed(e)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                this@AuthRepository.verificationId = verificationId
                onCodeSent(verificationId, token)
            }
        }
    }

    fun verifyPhone(
        phoneNum: String,
        activity: Activity
    ) {
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNum)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyPhoneNumberWithCode(code: String): PhoneAuthCredential {
        return PhoneAuthProvider.getCredential(verificationId, code)
    }

    private fun buildProfileUpdateRequest(
        displayName: String,
    ): UserProfileChangeRequest {

        return UserProfileChangeRequest.Builder()
            .setDisplayName(displayName)
            .build()

    }

    fun logout(){
        firebaseAuth.signOut()
    }

}