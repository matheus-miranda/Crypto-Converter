package br.com.msmlabs.cryptoconverter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import br.com.msmlabs.cryptoconverter.data.model.Crypto
import br.com.msmlabs.cryptoconverter.data.model.Fiat
import br.com.msmlabs.cryptoconverter.databinding.FragmentHomeBinding

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
        // Get values from Enum Class Fiat
        val fiatList = Fiat.values()
        val fiatAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, fiatList)

        // Get values from Enum Class Crypto
        val cryptoList = Crypto.values()
        val cryptoAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, cryptoList)

        // Set array list adapter to the drop down views
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