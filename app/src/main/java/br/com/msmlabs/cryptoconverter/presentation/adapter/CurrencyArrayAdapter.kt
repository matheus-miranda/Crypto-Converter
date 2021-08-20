package br.com.msmlabs.cryptoconverter.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import br.com.msmlabs.cryptoconverter.R
import br.com.msmlabs.cryptoconverter.data.model.spinner.Currency

/**
 * Custom class extending ArrayAdapter so we can add the images and text to each row on the spinner
 */
class CurrencyArrayAdapter(context: Context, currencies: List<Currency>) :
    ArrayAdapter<Currency>(context, 0, currencies) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return this.createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return this.createView(position, convertView , parent)
    }

    private fun createView(position: Int, recycledView: View?, parent: ViewGroup): View {

        val currency = getItem(position)

        val view = recycledView ?: LayoutInflater.from(context).inflate(
            R.layout.custom_row,
            parent,
            false
        )

        if (currency != null) {
            view.findViewById<ImageView>(R.id.iv_icon).setImageResource(currency.image)
            view.findViewById<TextView>(R.id.tv_currency_name).text = currency.text
        }

        return view
    }

}