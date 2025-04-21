package diamondcraft.devs.mycookinggallary.ui.profile

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import diamondcraft.devs.mycookinggallary.LoginActivity
import diamondcraft.devs.mycookinggallary.R
import diamondcraft.devs.mycookinggallary.databinding.FragmentDiscoverBinding
import diamondcraft.devs.mycookinggallary.databinding.FragmentProfileBinding
import diamondcraft.devs.mycookinggallary.viewmodels.AuthViewModel
import diamondcraft.devs.mycookinggallary.viewmodels.CookingViewModel
import kotlin.getValue

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<AuthViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginBtn .setOnClickListener {
            viewModel.logout()
            activity?.startActivity(Intent(activity, LoginActivity::class.java))
        }

    }


}