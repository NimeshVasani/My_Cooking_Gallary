package diamondcraft.devs.mycookinggallary.models

import diamondcraft.devs.mycookinggallary.db.Cooking

data class CookingResponse(
    val results: MutableList<Cooking>,
    val count: Int?
)