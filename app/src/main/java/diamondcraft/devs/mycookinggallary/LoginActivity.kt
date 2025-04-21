package diamondcraft.devs.mycookinggallary




import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import diamondcraft.devs.mycookinggallary.databinding.ActivityLoginBinding
import diamondcraft.devs.mycookinggallary.other.GlobalFunctions.currentUserUID
import diamondcraft.devs.mycookinggallary.other.GlobalFunctions.validateEmail
import diamondcraft.devs.mycookinggallary.other.GlobalFunctions.validatePassword
import diamondcraft.devs.mycookinggallary.other.Resources
import diamondcraft.devs.mycookinggallary.viewmodels.AuthViewModel
import diamondcraft.devs.mycookinggallary.viewmodels.AuthViewModelFactory
import javax.inject.Inject


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var initialVisibleHeight = 0


    @Inject
    lateinit var viewModelFactory: AuthViewModelFactory
    private val viewModel by viewModels<AuthViewModel> { viewModelFactory }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.contentLoadingProgressBar.visibility = View.GONE

        binding.loginPassword.viewTreeObserver.addOnGlobalLayoutListener(OnGlobalLayoutListener {
            val r = Rect()
            binding.loginPasswordEdit.getWindowVisibleDisplayFrame(r)
            val screenHeight: Int = binding.loginPasswordEdit.rootView.height
            val visibleHeight: Int = r.bottom - r.top
            if (initialVisibleHeight == 0) {
                initialVisibleHeight = visibleHeight
                return@OnGlobalLayoutListener
            }
            val heightDiff = screenHeight - visibleHeight
            if (heightDiff > screenHeight / 4) {
                // Keyboard is visible
                val translationY = -heightDiff / 2f
                binding.loginCard.animate().translationY(translationY).setDuration(200).start()
            } else {
                // Keyboard is not visible
                binding.loginCard.animate().translationY(0F).setDuration(200).start()
            }
        })

        val text = "Didn't Registered Yet? <font color='#0000FF'>SignUp</font> here...!!"

        binding.loginSignupLine.apply {
            this.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        }

        binding.loginEmailEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!validateEmail(s.toString())) {
                    binding.loginEmail.boxStrokeColor = Color.RED
                } else {
                    binding.loginEmail.boxStrokeColor = Color.BLUE

                }
            }

        })
        binding.loginPasswordEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!validatePassword(s.toString())) {
                    binding.loginPassword.boxStrokeColor = Color.RED
                } else {
                    binding.loginPassword.boxStrokeColor = Color.BLUE
                }
            }

        })

        binding.loginSignupLine.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
        }
        binding.loginBtn.setOnClickListener {
            val email = binding.loginEmailEdit.text.toString()
            val password = binding.loginPasswordEdit.text.toString()
            if (validateEmail(email) && validatePassword(password))
                viewModel.signInUserWithFirebase(
                    email = email,
                    password = password
                ).observe(this) { resource ->
                    when (resource) {
                        is Resources.Success -> {
                            binding.contentLoadingProgressBar.visibility = View.GONE
                            currentUserUID = viewModel.getCurrentUser().uid

                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }

                        is Resources.Error -> {
                            binding.contentLoadingProgressBar.visibility = View.GONE
                            Snackbar.make(
                                binding.root,
                                resource.message.toString(),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }

                        is Resources.Loading -> {
                            binding.contentLoadingProgressBar.visibility = View.VISIBLE
                        }
                    }
                }
        }
    }
}