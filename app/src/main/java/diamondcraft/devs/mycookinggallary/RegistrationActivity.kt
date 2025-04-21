package diamondcraft.devs.mycookinggallary


import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import diamondcraft.devs.mycookinggallary.databinding.ActivityRegistrationBinding
import diamondcraft.devs.mycookinggallary.other.GlobalFunctions.validateEmail
import diamondcraft.devs.mycookinggallary.other.GlobalFunctions.validatePassword
import diamondcraft.devs.mycookinggallary.other.GlobalFunctions.validateTwoPasswords
import diamondcraft.devs.mycookinggallary.other.Resources
import diamondcraft.devs.mycookinggallary.viewmodels.AuthViewModel
import diamondcraft.devs.mycookinggallary.viewmodels.AuthViewModelFactory
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private var initialVisibleHeight = 0

    @Inject
    lateinit var viewModelFactory: AuthViewModelFactory
    private val authViewModel by viewModels<AuthViewModel> { viewModelFactory }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registrationPassword1.viewTreeObserver.addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener {
            val r = Rect()
            binding.registrationPassword1.getWindowVisibleDisplayFrame(r)
            val screenHeight: Int = binding.registrationPassword1.rootView.height
            val visibleHeight: Int = r.bottom - r.top
            if (initialVisibleHeight == 0) {
                initialVisibleHeight = visibleHeight
                return@OnGlobalLayoutListener
            }
            val heightDiff = screenHeight - visibleHeight - 20f
            if (heightDiff > screenHeight / 6) {
                // Keyboard is visible
                binding.registrationCard.animate().translationY(-20f).setDuration(200)
                    .start()
            } else {
                // Keyboard is not visible
                binding.registrationCard.animate().translationY(0F).setDuration(200).start()
            }
        })

        binding.registrationEmailEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!validateEmail(s.toString())) {
                    binding.registrationEmail.boxStrokeColor = Color.RED
                } else {
                    binding.registrationEmail.boxStrokeColor = Color.BLUE

                }
            }

        })
        binding.registrationPasswordEdit1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!validatePassword(s.toString())) {
                    binding.registrationPassword1.boxStrokeColor = Color.RED
                } else {
                    binding.registrationPassword1.boxStrokeColor = Color.BLUE
                }
            }

        })
        binding.registrationPasswordEdit2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!validatePassword(s.toString())) {
                    binding.registrationPassword2.boxStrokeColor = Color.RED
                } else {
                    binding.registrationPassword2.boxStrokeColor = Color.BLUE
                }
            }

        })
        binding.registrationBtn.setOnClickListener {
            val userName = binding.registrationNameEdit.text.toString()
            val password1 = binding.registrationPasswordEdit1.text.toString()
            val password2 = binding.registrationPasswordEdit2.text.toString()
            val email = binding.registrationEmailEdit.text.toString()
            if (userName.isNotBlank()) {
                if (validateEmail(email)) {
                    if (validateTwoPasswords(password1, password2)) {
                        authViewModel.registerUserWithFirebase(
                            displayName = userName,
                            email = email,
                            password = password1
                        ).observe(this) { Resources ->
                            when (Resources) {
                                is Resources.Success -> {
                                    binding.registrationLoading.visibility =
                                        View.GONE
                                    finish()
                                }

                                is Resources.Error -> {
                                    binding.registrationLoading.visibility = View.GONE
                                    Snackbar.make(
                                        binding.root,
                                        Resources.message.toString(),
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                }

                                is Resources.Loading -> {
                                    binding.registrationLoading.visibility = View.VISIBLE
                                }
                            }
                        }
                    } else {
                        if (!validatePassword(password1)) {
                            Snackbar.make(
                                binding.root,
                                "password length should be more than 6 letter",
                                Snackbar.LENGTH_LONG
                            ).show()
                        } else {
                            Snackbar.make(
                                binding.root,
                                "Both passwords are not matching",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                } else {
                    Snackbar.make(
                        binding.root,
                        "Enter a valid Email",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            } else {
                Snackbar.make(
                    binding.root,
                    "Username is mandatory",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

    }


}