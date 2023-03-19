package diamondcraft.devs.mycookinggallary.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import diamondcraft.devs.mycookinggallary.converters.*

@Database(entities = [Cooking::class], version = 1, exportSchema = false)
@TypeConverters(
    InstructionTypeConverter::class,
    CreditConverter::class,
    NutritionTypeConverter::class,
    CookingConverter::class,
    UserRatingConverter::class,
    SectionConverter::class,
    IngradientConverter::class,
    MeasurementConverter::class,
    UnitConverter::class,
    ComponentConverter::class
)
abstract class CookingDatabase : RoomDatabase() {
    abstract fun getDao(): CookingDAO

}