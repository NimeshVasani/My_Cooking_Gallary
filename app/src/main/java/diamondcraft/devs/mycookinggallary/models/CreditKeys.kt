package diamondcraft.devs.mycookinggallary.models

import androidx.room.Entity
import java.io.Serializable

data class CreditKeys(
    val name: String?,
    val type: String?
) : Serializable
