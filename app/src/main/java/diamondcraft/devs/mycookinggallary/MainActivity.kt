package diamondcraft.devs.mycookinggallary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import dagger.hilt.android.AndroidEntryPoint
import diamondcraft.devs.mycookinggallary.databinding.ActivityMainBinding
import diamondcraft.devs.mycookinggallary.other.GlobalFunctions.currentUserUID
import diamondcraft.devs.mycookinggallary.viewmodels.AuthViewModel
import diamondcraft.devs.mycookinggallary.viewmodels.AuthViewModelFactory
import diamondcraft.devs.mycookinggallary.viewmodels.CookingViewModel
import diamondcraft.devs.mycookinggallary.viewmodels.CookingViewModelProviderFactory
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    @Inject
    lateinit var authViewModelFactory: AuthViewModelFactory
    private val authViewModel by viewModels<AuthViewModel> { authViewModelFactory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!authViewModel.checkLoginSession()) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        } else {
            currentUserUID = authViewModel.getCurrentUser().uid
        }


        val shapeDrawable: MaterialShapeDrawable =
            binding.navView.background as MaterialShapeDrawable
        shapeDrawable.shapeAppearanceModel = shapeDrawable.shapeAppearanceModel
            .toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, 20f)
            .build()

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        binding.navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            if (destination.id == R.id.searchFragment ) {
                binding.navView.menu.findItem(R.id.navigation_discover).isChecked = true
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (!authViewModel.checkLoginSession()) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        } else {
            currentUserUID = authViewModel.getCurrentUser().uid
        }
    }

    override fun onStart() {
        super.onStart()
        if (!authViewModel.checkLoginSession()) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        } else {
            currentUserUID = authViewModel.getCurrentUser().uid
        }
    }

    override fun onPause() {
        super.onPause()
        if (!authViewModel.checkLoginSession()) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        } else {
            currentUserUID = authViewModel.getCurrentUser().uid
        }
    }
}




