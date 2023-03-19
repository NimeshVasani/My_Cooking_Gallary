package diamondcraft.devs.mycookinggallary.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import diamondcraft.devs.mycookinggallary.models.Component

class ComponentConverter{

    @TypeConverter
    fun fromSection(component: MutableList<Component>?): String {
        return Gson().toJson(component)

    }

    @TypeConverter
    fun toSection(component: String?): MutableList<Component> {

        return try {
            Gson().fromJson6<MutableList<String>>(component) //using extension function
        } catch (e: Exception) {
            arrayListOf()
        }
    }
}


inline fun <reified T> Gson.fromJson6(json: String?): MutableList<Component> =
    fromJson(json, object : TypeToken<T>() {}.type)
