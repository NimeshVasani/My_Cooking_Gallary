package diamondcraft.devs.mycookinggallary.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import diamondcraft.devs.mycookinggallary.models.CookingInstruction

class InstructionTypeConverter {
    @TypeConverter
    fun fromInstruction(instruction: MutableList<CookingInstruction>?): String {
        return Gson().toJson(instruction)
    }

    @TypeConverter
    fun toInstruction(instruction: String): MutableList<CookingInstruction>? {
        return try {
            Gson().fromJson1<MutableList<CookingInstruction>>(instruction) //using extension function
        } catch (e: Exception) {
            arrayListOf()
        }
    }
}

inline fun <reified T> Gson.fromJson1(json: String): MutableList<CookingInstruction> =
    fromJson(json, object : TypeToken<T>() {}.type)