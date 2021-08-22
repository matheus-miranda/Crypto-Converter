package br.com.msmlabs.cryptoconverter.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import br.com.msmlabs.cryptoconverter.R
import br.com.msmlabs.cryptoconverter.core.createDialog
import br.com.msmlabs.cryptoconverter.core.createProgressDialog
import br.com.msmlabs.cryptoconverter.core.hideSoftKeyboard
import br.com.msmlabs.cryptoconverter.core.text
import br.com.msmlabs.cryptoconverter.data.model.types.Crypto
import br.com.msmlabs.cryptoconverter.data.model.types.Fiat
import br.com.msmlabs.cryptoconverter.databinding.FragmentHomeBinding
import br.com.msmlabs.cryptoconverter.presentation.adapter.CurrencyArrayAdapter
import br.com.msmlabs.cryptoconverter.presentation.enitity.Currency
import br.com.msmlabs.cryptoconverter.presentation.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModel()
    private val dialog by lazy { requireContext().createProgressDialog() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        bindAdapters()
        bindListeners()
        inscribeObservers()

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

        binding.btnConvert.setOnClickListener {
            it.hideSoftKeyboard()

            // Get value from the TIL and pass it to the ViewModel
            val fiat = binding.tilConvertFrom.text.lowercase()
            val crypto = when (binding.tilConvertTo.text) {
                "ADA" -> Crypto.ADA.cryptoName
                "BCH" -> Crypto.BCH.cryptoName
                "BTC" -> Crypto.BTC.cryptoName
                "DASH" -> Crypto.DASH.cryptoName
                "DOGE" -> Crypto.DOGE.cryptoName
                "ETH" -> Crypto.ETH.cryptoName
                "LTC" -> Crypto.LTC.cryptoName
                "NEO" -> Crypto.NEO.cryptoName
                "USDT" -> Crypto.USDT.cryptoName
                "XLM" -> Crypto.XLM.cryptoName
                "XMR" -> Crypto.XMR.cryptoName
                else -> Crypto.XRP.cryptoName
            }

            Log.e("HomeFragment", "fiat = $fiat, crypto = $crypto")
            viewModel.getValues(fiat,crypto)
        }
    }

    /**
     * Observer methods
     */
    private fun inscribeObservers() {
        viewModel.state.observe(viewLifecycleOwner, {
            when (it) {
                HomeViewModel.State.Loading -> dialog.show()
                is HomeViewModel.State.Error -> {
                    dialog.dismiss()
                    requireContext().createDialog {
                        setMessage(it.throwable.message)
                    }.show()
                }
                is HomeViewModel.State.Success -> {
                    dialog.dismiss()
                    Log.e("HomeFragment", "onCreate: ${it.value}")
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}