package diamondcraft.devs.mycookinggallary.converters

import android.graphics.Bitmap
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import diamondcraft.devs.mycookinggallary.models.CreditKeys

@ProvidedTypeConverter
class CreditConverter {

    @TypeConverter
    fun fromCredit(credits: MutableList<CreditKeys>?): String {
        return Gson().toJson(credits)

    }

    @TypeConverter
    fun toCredit(credits: String): MutableList<CreditKeys>? {
        return try {
            Gson().fromJson<MutableList<String>>(credits) //using extension function
        } catch (e: Exception) {
            arrayListOf()
        }
    }
}

inline fun <reified T> Gson.fromJson(json: String): MutableList<CreditKeys> =
    fromJson(json, object : TypeToken<T>() {}.type)
