package diamondcraft.devs.mycookinggallary.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import diamondcraft.devs.mycookinggallary.models.Section

class SectionConverter {

    @TypeConverter
    fun fromSection(section: MutableList<Section>?): String {
        return Gson().toJson(section)

    }

    @TypeConverter
    fun toSection(section: String?): MutableList<Section> {

        return try {
            Gson().fromJson5<MutableList<String>>(section) //using extension function
        } catch (e: Exception) {
            arrayListOf()
        }
    }
}


inline fun <reified T> Gson.fromJson5(json: String?): MutableList<Section> =
    fromJson(json, object : TypeToken<T>() {}.type)
