package diamondcraft.devs.mycookinggallary.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import diamondcraft.devs.mycookinggallary.converters.CreditConverter
import diamondcraft.devs.mycookinggallary.db.CookingDAO
import diamondcraft.devs.mycookinggallary.db.CookingDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesCookingDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, CookingDatabase::class.java, "Cooking_Database").addTypeConverter(CreditConverter()).build()

    @Singleton
    @Provides
    fun providesCookingDao(db: CookingDatabase): CookingDAO = db.getDao()
}