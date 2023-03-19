package diamondcraft.devs.mycookinggallary.ui.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import diamondcraft.devs.mycookinggallary.R

class DiscoverViewModel : ViewModel() {


    private val _text = MutableLiveData<String>().apply {
        value = "Let's Eat Foood \n Lets doo ittt"
    }
    val text: LiveData<String> = _text

    private val _image = MutableLiveData<Int>().apply {
        value = R.drawable.ic_dummy
    }
    val image: LiveData<Int> = _image
}