package diamondcraft.devs.mycookinggallary.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import diamondcraft.devs.mycookinggallary.models.CookingInstruction
import diamondcraft.devs.mycookinggallary.models.Unit

class UnitConverter {
    @TypeConverter
    fun fromInstruction(unit: Unit?): String {
        return Gson().toJson(unit)
    }

    @TypeConverter
    fun toInstruction(unit: String): Unit? {
        return try {
            Gson().fromJson9<MutableList<CookingInstruction>>(unit) //using extension function
        } catch (e: Exception) {
            Unit(null, null, null, null, null, null)
        }
    }
}

inline fun <reified T> Gson.fromJson9(json: String): Unit =
    fromJson(json, object : TypeToken<T>() {}.type)