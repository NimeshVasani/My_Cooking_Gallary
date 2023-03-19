package diamondcraft.devs.mycookinggallary.other

import java.text.Normalizer

object GlobalFunctions {
    fun fromVulgarFraction(number: String): Double {
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

}