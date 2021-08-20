package br.com.msmlabs.cryptoconverter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import br.com.msmlabs.cryptoconverter.R
import br.com.msmlabs.cryptoconverter.data.model.Crypto
import br.com.msmlabs.cryptoconverter.data.model.Fiat
import br.com.msmlabs.cryptoconverter.data.model.spinner.Currency
import br.com.msmlabs.cryptoconverter.databinding.FragmentHomeBinding
import br.com.msmlabs.cryptoconverter.presentation.adapter.CurrencyArrayAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        bindAdapters()
        bindListeners()

        return binding.root
    }

    /**
     * Binds the array values to the currency drop down menus
     */
    private fun bindAdapters() {
        // Create custom array adapter for fiat currencies
        val fiatAdapter = CurrencyArrayAdapter(
            requireContext(),
            listOf(
                Currency(R.drawable.au, Fiat.AUD.toString()),
                Currency(R.drawable.br, Fiat.BRL.toString()),
                Currency(R.drawable.ca, Fiat.CAD.toString()),
                Currency(R.drawable.euro, Fiat.EUR.toString()),
                Currency(R.drawable.gb, Fiat.GBP.toString()),
                Currency(R.drawable.us, Fiat.USD.toString()),
            )
        )

        // Create custom array adapter for crypto currencies
        val cryptoAdapter = CurrencyArrayAdapter(
            requireContext(),
            listOf(
                Currency(R.drawable.ada_cardano, Crypto.ADA.toString()),
                Currency(R.drawable.bch_bitcoin_cash, Crypto.BCH.toString()),
                Currency(R.drawable.btc_bitcoin, Crypto.BTC.toString()),
                Currency(R.drawable.dash, Crypto.DASH.toString()),
                Currency(R.drawable.doge_dogecoin, Crypto.DOGE.toString()),
                Currency(R.drawable.eth_ethereum, Crypto.ETH.toString()),
                Currency(R.drawable.ltc_litecoin, Crypto.LTC.toString()),
                Currency(R.drawable.neo, Crypto.NEO.toString()),
                Currency(R.drawable.usdt_tether, Crypto.USDT.toString()),
                Currency(R.drawable.xlm_stellar, Crypto.XLM.toString()),
                Currency(R.drawable.xmr_monero, Crypto.XMR.toString()),
                Currency(R.drawable.xrp_ripple, Crypto.XRP.toString())
            )
        )

        // Set array list adapter to the drop down spinner
        binding.tvConvertFrom.setAdapter(fiatAdapter)
        binding.tvConvertTo.setAdapter(cryptoAdapter)

        // Pre-selected values
        binding.tvConvertFrom.setText(Fiat.BRL.name, false)
        binding.tvConvertTo.setText(Crypto.BTC.name, false)
    }

    /**
     * Set onClickListeners for the views
     */
    private fun bindListeners() {
        // Only enable convert button if there is a value entered
        binding.tilValue.editText?.doAfterTextChanged { text ->
            if (text != null) {
                binding.btnConvert.isEnabled = text.isNotEmpty()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}