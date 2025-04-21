package diamondcraft.devs.mycookinggallary.di

import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ms.square.android.expandabletextview.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import diamondcraft.devs.mycookinggallary.api.CookingApi
import diamondcraft.devs.mycookinggallary.db.CookingDAO
import diamondcraft.devs.mycookinggallary.other.Constants
import diamondcraft.devs.mycookinggallary.repositories.AuthRepository
import diamondcraft.devs.mycookinggallary.repositories.CookingRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    fun provideBaseUrl() = Constants.BASE_URL


    @Provides
    @Singleton
    fun providesGson(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .build()
    }


    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson,okHttpClient: OkHttpClient, BASE_URL: String): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson)).baseUrl(BASE_URL)
            .client(okHttpClient).build()
    }

    @Singleton
    @Provides
    fun provideCookingApi(retrofit: Retrofit): CookingApi {
        return retrofit.create(CookingApi::class.java)
    }


    @Singleton
    @Provides
    fun providesRepository(cookingDAO: CookingDAO, cookingApi: CookingApi) =
        CookingRepository(cookingDAO, cookingApi)


    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepository(firebaseAuth)
    }




}