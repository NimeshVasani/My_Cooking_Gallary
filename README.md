# Developer Info

All copyright reserved @Nimesh Vasani 

[GitHub Profile](https://github.com/NimeshVasani)

[Linked-In](https://www.linkedin.com/in/nimesh-vasani-99b642154/)

[Stack Overflow](https://stackoverflow.com/users/16579306/nimesh-vasani)

# About The App

My Cooking Gallary app is all about thoousand of tasty food recipes.

Built with  : [MVVM](https://developer.android.com/topic/libraries/architecture/viewmodel?gclid=CjwKCAjw5dqgBhBNEiwA7PryaEGGNXBuF_269i5vAml9SedixRgYXYfktdB8NOZm__qJWmdN6hpUahoC2IQQAvD_BwE&gclsrc=aw.ds#kotlin_1), [Retrofit](https://square.github.io/retrofit/), [Room](https://developer.android.com/training/data-storage/room),[Glide](https://github.com/bumptech/glide) and [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android) with Proper [Navigation UI](https://developer.android.com/guide/navigation/navigation-getting-started).

![alt text](https://github.com/NimeshVasani/My_Cooking_Gallary/blob/main/snapshots/final_snap_shot.png)

App use [tasty food Api from Rapid Api](https://rapidapi.com/apidojo/api/tasty/), where you need to sign in and generate your own Api key.

To Update Api key in project to make it work follow the step : goto `"others"  package > goto "Constant.kt" class > update the value of "const val API_Key"` [Constants.kt](app/src/main/java/diamondcraft/devs/mycookinggallary/other/Constants.kt)


```ruby
package diamondcraft.devs.mycookinggallary.other

object Constants {
    const val DATABASE_NAME = "Cooking_database"
    const val TABLE_NAME = "Cooking_table"

    const val BASE_URL = "https://tasty.p.rapidapi.com/"
    const val API_KEY = "PASTE_YOUR_API_KEY"
}
```

# Architecture & Work-Flow
This app use MVVM architecture skeleton and room database for saving recipes in local database.

![alt text](https://github.com/NimeshVasani/My_Cooking_Gallary/blob/main/snapshots/architecture.png)

As a Dependency injection We use Dagger Hilt to set Up Singlton Object Pattern Through whole Project.

in the `di` package we have 2 modules : 1. [ApiModule](app/src/main/java/diamondcraft/devs/mycookinggallary/di/ApiModule.kt)
 and 2. [AppModule](app/src/main/java/diamondcraft/devs/mycookinggallary/di/AppModule.kt)

We do have different types of [Models](app/src/main/java/diamondcraft/devs/mycookinggallary/models) for Handling Api Data set.

## Special Conversion Fuction
```kotlin
fun fromVulgarFraction(number1: String): Double {
        val number = number1.filterNot { it.isWhitespace() }

        val items = number.split("""\d""".toRegex()).filterNot { it.isEmpty() }
        val mixed: String?
        val fraction = mutableListOf<String>()
        return if (items.isNotEmpty()) {
            mixed = items.first()
            fraction.addAll(Normalizer.normalize(mixed, Normalizer.Form.NFKC).split("\u2044"))
            val decimal = fraction[0].toInt().toDouble() / fraction[1].toDouble()
            val result = """\d+""".toRegex().find(number)
            if (result != null) {
                result.value.toDouble() + decimal
            } else {
                decimal
            }
        } else {
            number.toDouble()
        }
    }
    ```
