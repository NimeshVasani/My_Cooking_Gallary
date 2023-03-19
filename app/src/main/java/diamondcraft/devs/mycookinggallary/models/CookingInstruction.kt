package diamondcraft.devs.mycookinggallary.models

data class CookingInstruction(
    val appliance: String?,
    val position: String?,
    val display_text: String?,
    val start_time: Double?,
    val end_time: Double?,
    val temperature: String? = "Not Applicable"
)
