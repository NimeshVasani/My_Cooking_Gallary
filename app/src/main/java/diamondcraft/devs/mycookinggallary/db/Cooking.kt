package diamondcraft.devs.mycookinggallary.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import diamondcraft.devs.mycookinggallary.models.*
import java.io.Serializable

@Entity(tableName = "Cooking_table")
data class Cooking(
    @PrimaryKey val id: Int?,
    @ColumnInfo(name = "name") val name: String?, // name keyword from JSON description
    @ColumnInfo(name = "desc") val description: String?,
    @ColumnInfo(name = "image_main") val thumbnail_url: String?,
    @ColumnInfo(name = "credits") val credits: MutableList<CreditKeys>?,
    @ColumnInfo(name = "instructions") val instructions: MutableList<CookingInstruction>?,
    @ColumnInfo(name = "video_url") val video_url: String?,
    @ColumnInfo(name = "nutrition_info") val nutrition: Nutrition?,
    @ColumnInfo(name = "recipes") val recipes: MutableList<Cooking>?,
    @ColumnInfo(name = "user_rating") val user_ratings: UserRatings?,
    @ColumnInfo(name = "sections") val sections: MutableList<Section>?,
    @ColumnInfo(name = "num_servings") val num_servings: Int?

) : Serializable


//Model
