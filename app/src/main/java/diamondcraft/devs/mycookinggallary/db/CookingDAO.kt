package diamondcraft.devs.mycookinggallary.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CookingDAO {

    @Query("SELECT * FROM COOKING_TABLE")
    fun getAll(): LiveData<List<Cooking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCooking(vararg cooking: Cooking)

    @Delete
    suspend fun deleteCooking(cooking: Cooking)
}