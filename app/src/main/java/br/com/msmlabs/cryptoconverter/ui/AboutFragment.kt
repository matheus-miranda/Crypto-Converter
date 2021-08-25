package br.com.msmlabs.cryptoconverter.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.msmlabs.cryptoconverter.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)

        bindToolbar()
        bindListeners()

        return binding.root
    }

    private fun bindToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.tbAbout)
        (activity as AppCompatActivity).supportActionBar?.title = ""
        setHasOptionsMenu(true)

        binding.icBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun bindListeners() {
        binding.ivEmail.setOnClickListener {
            sendEmail()
        }
    }

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("msmlabs.apps@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Crypto Converter - Feedback")
        }
        if (activity?.let { intent.resolveActivity(it.packageManager) } != null) {
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "No e-mail client installed", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}