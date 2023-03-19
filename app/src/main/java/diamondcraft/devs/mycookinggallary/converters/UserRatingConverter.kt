package diamondcraft.devs.mycookinggallary.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import diamondcraft.devs.mycookinggallary.models.UserRatings

class UserRatingConverter {
    @TypeConverter
    fun fromNutrition(userRatings: UserRatings?): String {
        return Gson().toJson(userRatings)
    }

    @TypeConverter
    fun toNutrition(userRating: String): UserRatings? {
        return try {
            Gson().fromJson4<String>(userRating) //using extension function
        } catch (e: Exception) {
            UserRatings(null, null, null)
        }
    }
}


inline fun <reified T> Gson.fromJson4(json: String): UserRatings =
    fromJson(json, object : TypeToken<T>() {}.type)