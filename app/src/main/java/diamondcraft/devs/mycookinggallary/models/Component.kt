package diamondcraft.devs.mycookinggallary.models

data class Component(
    var measurements: MutableList<Measurement>?,
    var row_text: String?,
    var extra_comment: String?,
    var ingredient: Ingradient?,
    var id: Int?,
    var position: String?
)
