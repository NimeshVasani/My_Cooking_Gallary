package diamondcraft.devs.mycookinggallary.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import diamondcraft.devs.mycookinggallary.db.CookingDatabase
import diamondcraft.devs.mycookinggallary.repositories.CookingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class MyCookingApp : Application() {

}