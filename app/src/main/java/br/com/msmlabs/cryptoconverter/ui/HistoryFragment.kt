package br.com.msmlabs.cryptoconverter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.msmlabs.cryptoconverter.core.createDialog
import br.com.msmlabs.cryptoconverter.core.createProgressDialog
import br.com.msmlabs.cryptoconverter.databinding.FragmentHistoryBinding
import br.com.msmlabs.cryptoconverter.presentation.adapter.HistoryListAdapter
import br.com.msmlabs.cryptoconverter.presentation.viewmodel.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HistoryViewModel by viewModel()
    private val adapter by lazy { HistoryListAdapter() }
    private val dialog by lazy { requireContext().createProgressDialog() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        bindToolbar()
        bindAdapter()
        bindObservers()

        return binding.root
    }

    private fun bindAdapter() {
        binding.apply {
            rvHistory.adapter = adapter
            rvHistory.addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL)
            )
        }
    }

    private fun bindObservers() {
        viewModel.getAllExchanges()

        viewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                HistoryViewModel.State.Loading -> {
                    dialog.show()
                }
                is HistoryViewModel.State.Error -> {
                    dialog.dismiss()
                    requireContext().createDialog {
                        setMessage(state.throwable.message)
                    }.show()
                }
                is HistoryViewModel.State.Success -> {
                    dialog.dismiss()
                    adapter.submitList(state.exchangeValue)
                }
            }
        })
    }

    /**
     * Sets the toolbar as action bar
     */
    private fun bindToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.tbHistory)
        (activity as AppCompatActivity).supportActionBar?.title = ""

        binding.icBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}