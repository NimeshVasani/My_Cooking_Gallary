package diamondcraft.devs.mycookinggallary.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import diamondcraft.devs.mycookinggallary.db.Cooking

class CookingConverter {
    @TypeConverter
    fun fromCooking(cooking: MutableList<Cooking>?): String {
        return Gson().toJson(cooking)

    }

    @TypeConverter
    fun toCooking(cooking: String?): MutableList<Cooking> {

        return try {
            Gson().fromJson3<MutableList<String>>(cooking) //using extension function
        } catch (e: Exception) {
            arrayListOf()
        }
    }
}


inline fun <reified T> Gson.fromJson3(json: String?): MutableList<Cooking> =
    fromJson(json, object : TypeToken<T>() {}.type)
