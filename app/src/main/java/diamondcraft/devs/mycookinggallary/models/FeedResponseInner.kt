package diamondcraft.devs.mycookinggallary.models

import diamondcraft.devs.mycookinggallary.db.Cooking

data class FeedResponseInner(
    var type: String?,
    var items: MutableList<Cooking>?,
    var item: Cooking?,
    var name: String?,
    var category: String?
)
