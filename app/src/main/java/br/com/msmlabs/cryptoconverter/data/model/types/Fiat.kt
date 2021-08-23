package br.com.msmlabs.cryptoconverter.data.model.types

import java.util.*

enum class Fiat(val locale: Locale) {
    AUD(Locale("en", "au")),
    BRL(Locale("pt", "BR")),
    CAD(Locale.CANADA),
    EUR(Locale.GERMANY),
    GBP(Locale.UK),
    USD(Locale.US)
}