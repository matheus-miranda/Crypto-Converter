package br.com.msmlabs.cryptoconverter.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import br.com.msmlabs.cryptoconverter.R
import br.com.msmlabs.cryptoconverter.core.*
import br.com.msmlabs.cryptoconverter.data.model.GeckoResponseEntity
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

        bindToolbar()
        bindAdapters()
        bindListeners()
        bindObservers()

        return binding.root
    }

    /**
     * Required to bind the adapters to the spinners when navigating to another fragment or
     * changing orientation
     */
    override fun onResume() {
        super.onResume()
        bindAdapters()
    }

    /**
     * Sets the toolbar as action bar
     */
    private fun bindToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)
    }

    /**
     * Binds the array values to the currency drop down menus
     */
    private fun bindAdapters() {
        val (fiatAdapter, cryptoAdapter) = createSpinnerArrayAdapters()

        // Set array list adapter to the drop down spinner
        binding.tvConvertFrom.setAdapter(cryptoAdapter)
        binding.tvConvertTo.setAdapter(fiatAdapter)
    }

    /**
     * Create custom array adapter for the spinner drop downs
     */
    private fun createSpinnerArrayAdapters(): Pair<CurrencyArrayAdapter, CurrencyArrayAdapter> {
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
        return Pair(fiatAdapter, cryptoAdapter)
    }

    /**
     * Set onClickListeners for the views
     */
    private fun bindListeners() {
        // Only enable convert button if there is a value entered
        binding.tilValue.editText?.doAfterTextChanged { text ->
            if (text != null) {
                binding.btnConvert.isEnabled = text.isNotEmpty()
                binding.btnSave.isEnabled = false
            }
        }

        binding.btnConvert.setOnClickListener {
            it.hideSoftKeyboard()

            if (!viewModel.swapped) {
                // Get value from the TIL and pass it to the ViewModel
                val (fiat, crypto) = getTilValues()
                viewModel.getValues(fiat, crypto)
            } else {
                val fiat = binding.tilConvertFrom.text.lowercase()
                val crypto = Crypto.getByName(binding.tilConvertTo.text)
                viewModel.getValues(fiat, crypto)
            }
        }

        binding.btnSave.setOnClickListener {
            if (!viewModel.swapped) {
                val currentPrice = binding.tvResult.text.toString()
                val crypto = binding.tilConvertFrom.text
                val fiat = binding.tilConvertTo.text
                val value = binding.etValue.text.toString()
                val types = "$value $crypto / $fiat"
                viewModel.saveToDb(GeckoResponseEntity(types = types, currentPrice = currentPrice))
            } else {
                val currentCrypto = binding.tvResult.text.toString()
                val fiat = binding.tilConvertFrom.text
                val crypto = binding.tilConvertTo.text
                val value = binding.etValue.text.toString()
                val types = "$value $fiat / $crypto"
                viewModel.saveToDb(GeckoResponseEntity(types = types, currentPrice = currentCrypto))
            }
            binding.btnSave.isEnabled = false
        }

        // Hide the keyboard when user clicks on either TILs
        binding.tvConvertFrom.setOnClickListener {
            it.hideSoftKeyboard()
        }
        binding.tvConvertTo.setOnClickListener {
            it.hideSoftKeyboard()
        }

        // Swap the AutoComplete spinners - From Fiat to Crypto
        binding.ibSwap.setOnClickListener {

            // Get an array list adapter
            val (fiatAdapter, cryptoAdapter) = createSpinnerArrayAdapters()

            // Get the values from the TILs
            val textFrom = binding.tilConvertFrom.text
            val textTo= binding.tilConvertTo.text

            if (!viewModel.swapped) {
                viewModel.swapped = true

                // Animate the drop down views so user know they swapped
                binding.apply {
                    tilConvertFrom.animate().apply {
                        duration = 1000
                        rotationXBy(360f)
                    }.start()
                    tilConvertTo.animate().apply {
                        duration = 1000
                        rotationXBy(360f)
                    }.start()

                    // Swap the values in the TILs
                    tilConvertFrom.text = textTo
                    tilConvertTo.text = textFrom

                    // Swap the array values on the drop down spinner
                    tvConvertFrom.setAdapter(fiatAdapter)
                    tvConvertTo.setAdapter(cryptoAdapter)
                }

            } else {
                // Animate the drop down views so user know they swapped
                binding.apply {
                    tilConvertFrom.animate().apply {
                        duration = 1000
                        rotationXBy(-360f)
                    }.start()
                    tilConvertTo.animate().apply {
                        duration = 1000
                        rotationXBy(-360f)
                    }.start()

                    // Swap the values in the TILs
                    tilConvertFrom.text = textTo
                    tilConvertTo.text = textFrom

                    // Swap the array values on the drop down spinner
                    tvConvertFrom.setAdapter(cryptoAdapter)
                    tvConvertTo.setAdapter(fiatAdapter)
                }
                viewModel.swapped = false
            }
        }
    }

    /**
     * Observer methods
     */
    private fun bindObservers() {
        viewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                HomeViewModel.State.Loading -> dialog.show()
                is HomeViewModel.State.Error -> {
                    dialog.dismiss()
                    requireContext().createDialog {
                        setMessage(state.throwable.message)
                    }.show()
                }
                is HomeViewModel.State.Success -> {
                    success(state)
                }
                HomeViewModel.State.Saved -> {
                    dialog.dismiss()
                }
            }
        })
    }

    /**
     * Returns value from the TILs
     */
    private fun getTilValues(): Pair<String, String> {
        val fiat = binding.tilConvertTo.text.lowercase()
        val crypto = Crypto.getByName(binding.tilConvertFrom.text)

        return Pair(fiat, crypto)
    }

    /**
     * Sets the result text view on a successful API call
     */
    private fun success(state: HomeViewModel.State.Success) {
        dialog.dismiss()
        binding.btnSave.isEnabled = true

        if (!viewModel.swapped) {
            // Assign the result to the current price multiplied by the value entered
            val result = binding.tilValue.text.toDouble() * state.exchangeValue[0].currentPrice

            // Get the fiat currency according to what the user chose
            val selectedFiat = binding.tilConvertTo.text
            val fiat = Fiat.getByName(selectedFiat)

            binding.apply {
                // Set the result text view with the formatted currency locale
                tvResult.text = result.formatCurrency(fiat.locale)

                // Set the 24h high and low prices
                tvHigh.text = state.exchangeValue[0].high24H.formatCurrency(fiat.locale)
                tvLow.text = state.exchangeValue[0].low24H.formatCurrency(fiat.locale)

                // Show the views
                ivUpArrow.visibility = View.VISIBLE
                ivDownArrow.visibility = View.VISIBLE
                tv24h.visibility = View.VISIBLE
                tvLow.visibility = View.VISIBLE
                tvHigh.visibility = View.VISIBLE
            }
        } else {
            // Assign the result to the value divided by the current price
            val result = binding.tilValue.text.toDouble() / state.exchangeValue[0].currentPrice

            binding.apply {
                // Set the result text view with the result
                tvResult.text = resources.getString(R.string.formatted_result, result)

                // Hide the views
                ivUpArrow.visibility = View.GONE
                ivDownArrow.visibility = View.GONE
                tv24h.visibility = View.GONE
                tvLow.visibility = View.GONE
                tvHigh.visibility = View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
