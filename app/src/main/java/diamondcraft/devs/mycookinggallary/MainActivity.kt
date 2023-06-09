package diamondcraft.devs.mycookinggallary

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import diamondcraft.devs.mycookinggallary.databinding.ActivityMainBinding
import diamondcraft.devs.mycookinggallary.viewmodels.CookingViewModel
import diamondcraft.devs.mycookinggallary.viewmodels.CookingViewModelProviderFactory
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: CookingViewModelProviderFactory

    val viewModel by viewModels<CookingViewModel> { viewModelFactory }
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)


    }
}