package diamondcraft.devs.mycookinggallary.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import diamondcraft.devs.mycookinggallary.models.Nutrition

class NutritionTypeConverter {
    @TypeConverter
    fun fromNutrition(nutrition: Nutrition?): String {
        return Gson().toJson(nutrition)
    }

    @TypeConverter
    fun toNutrition(nutrition: String): Nutrition? {
        return try {
            Gson().fromJson2<String>(nutrition) //using extension function
        } catch (e: Exception) {
            Nutrition(null, null, null, null, null, null)
        }
    }
}

inline fun <reified T> Gson.fromJson2(json: String): Nutrition =
    fromJson(json, object : TypeToken<T>() {}.type)