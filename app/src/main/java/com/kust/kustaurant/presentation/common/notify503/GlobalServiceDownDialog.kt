package com.kust.kustaurant.presentation.common.notify503

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.kust.kustaurant.databinding.DialogShowServiceUpdateBinding

class GlobalServiceDownDialog : DialogFragment() {
    interface Listener {
        fun onServiceDownDialogDismissed()
    }

    private var _binding: DialogShowServiceUpdateBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_MESSAGE = "arg_message"

        fun newInstance(message: String) =
            GlobalServiceDownDialog().apply {
                arguments = bundleOf(ARG_MESSAGE to message)
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        _binding = DialogShowServiceUpdateBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCancelable(true)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val message = requireArguments().getString(ARG_MESSAGE).orEmpty()

        binding.notifyClRoot.clipToOutline = true
        binding.notifyTvContent.text = message
        binding.notifyTvConfirm.setOnClickListener { dismiss() }

        return dialog
    }

    fun updateMessage(msg: String) {
        binding.notifyTvContent.text = msg
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onDismiss(dialog: DialogInterface) {
        (activity as? Listener)?.onServiceDownDialogDismissed()
        super.onDismiss(dialog)
    }
}
