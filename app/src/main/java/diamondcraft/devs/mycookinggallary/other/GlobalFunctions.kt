package diamondcraft.devs.mycookinggallary.other

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import java.util.regex.Pattern

object GlobalFunctions {
    fun fromVulgarFraction(number1: String): Double {
        val number = number1.filterNot { it.isWhitespace() || it=='.' }

        val items = number.split("""\d""".toRegex()).filterNot { it.isEmpty() }
        val mixed: String?
        val fraction = mutableListOf<String>()
        return if (items.isNotEmpty()) {
            mixed = items.first()
            fraction.addAll(Normalizer.normalize(mixed, Normalizer.Form.NFKC).split("\u2044"))
            val decimal = fraction[0].toInt().toDouble() / fraction[1].toDouble()
            val result = """\d+""".toRegex().find(number)
            if (result != null) {
                result.value.toDouble() + decimal
            } else {
                decimal
            }
        } else {
            number.toDouble()
        }
    }

    var currentUserUID: String = ""

    fun validateEmail(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$"
        )
        return emailPattern.matcher(email).matches()
    }

    fun validatePassword(password: String): Boolean {
        return password.length >= 6
    }

    fun validateTwoPasswords(password1: String, password2: String): Boolean {
        return password1.length >= 6 && password1 == password2
    }


    private val dateFormat: SimpleDateFormat
        get() = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    private val timeFormat: SimpleDateFormat
        get() = SimpleDateFormat("hh:mm a", Locale.getDefault())

    private val currentDateLiveData: MutableLiveData<String> = MutableLiveData()
    private val currentTimeLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        // Start updating date and time immediately
        updateDateAndTime()
    }

    fun getCurrentDateLiveData(): LiveData<String> = currentDateLiveData

    fun getCurrentTimeLiveData(): LiveData<String> = currentTimeLiveData

    private fun updateDateAndTime() {
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                val currentDate = Calendar.getInstance().time
                val currentTime = Calendar.getInstance().time

                currentDateLiveData.postValue(dateFormat.format(currentDate))
                currentTimeLiveData.postValue(timeFormat.format(currentTime))
            }
        }, 0, 1000) // Update every second (adjust as needed)
    }
}