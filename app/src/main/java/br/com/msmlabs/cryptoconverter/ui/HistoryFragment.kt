package br.com.msmlabs.cryptoconverter.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.msmlabs.cryptoconverter.R
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
                HistoryViewModel.State.Deleted -> dialog.dismiss()
            }
        })
    }

    /**
     * Sets the toolbar as action bar
     */
    private fun bindToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.tbHistory)
        (activity as AppCompatActivity).supportActionBar?.title = ""
        setHasOptionsMenu(true)

        binding.icBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    /**
     * Inflate the menu
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.history_menu, menu)
    }

    /**
     * Define action for clicking menu item
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_delete) {
            viewModel.deleteAllFromDb()
            item.isEnabled = false
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}