package br.com.msmlabs.cryptoconverter.data.model.spinner

/**
 * This data class represents a single row in our spinner
 */
data class Currency(val image: Int, val text: String) {

    override fun toString(): String {
        return text
    }
}
