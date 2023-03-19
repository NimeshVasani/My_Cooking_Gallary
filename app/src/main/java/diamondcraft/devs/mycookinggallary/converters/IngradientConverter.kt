package diamondcraft.devs.mycookinggallary.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import diamondcraft.devs.mycookinggallary.models.CookingInstruction
import diamondcraft.devs.mycookinggallary.models.Ingradient

class IngradientConverter {
    @TypeConverter
    fun fromInstruction(ingradient: Ingradient?): String {
        return Gson().toJson(ingradient)
    }

    @TypeConverter
    fun toInstruction(ingradient: String): Ingradient? {
        return try {
            Gson().fromJson8<CookingInstruction>(ingradient) //using extension function
        } catch (e: Exception) {
            Ingradient(null, null, null)
        }
    }
}

inline fun <reified T> Gson.fromJson8(json: String): Ingradient =
    fromJson(json, object : TypeToken<T>() {}.type)