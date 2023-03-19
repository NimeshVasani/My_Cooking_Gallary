package diamondcraft.devs.mycookinggallary.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import diamondcraft.devs.mycookinggallary.models.CookingInstruction
import diamondcraft.devs.mycookinggallary.models.Measurement

class MeasurementConverter {
    @TypeConverter
    fun fromInstruction(measurement: MutableList<Measurement>?): String {
        return Gson().toJson(measurement)
    }

    @TypeConverter
    fun toInstruction(measurement: String): MutableList<Measurement>? {
        return try {
            Gson().fromJson7<MutableList<CookingInstruction>>(measurement) //using extension function
        } catch (e: Exception) {
            arrayListOf()
        }
    }
}

inline fun <reified T> Gson.fromJson7(json: String): MutableList<Measurement> =
    fromJson(json, object : TypeToken<T>() {}.type)