package com.kust.kustaurant.presentation.ui.community

import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.FragmentCommunityPostWriteBinding
import com.kust.kustaurant.databinding.PopupCommuPostWriteSortBinding
import com.kust.kustaurant.presentation.ui.detail.EvaluateActivity.Companion.REQ_GALLERY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommunityPostWriteFragment : Fragment() {
    private lateinit var binding: FragmentCommunityPostWriteBinding
    private val viewModel: CommunityPostWriteViewModel by activityViewModels()
    private lateinit var photoPickerLauncher: ActivityResultLauncher<Intent>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityPostWriteBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initPhotoPicker()
        setupUI()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
    }

    private fun setupUI() {
        binding.llSelectPostSort.setOnClickListener { showPopupWindow() }

        binding.ivBold.setOnClickListener {
            val start = binding.etPostContent.selectionStart
            val end = binding.etPostContent.selectionEnd
            Log.d("ivBold", "$start $end")
            if (start != end) {  // 텍스트가 선택된 경우에만 적용
                Log.d("ivBold", "if문 들옴")
                viewModel.applyBold(start, end)
            }
        }

        binding.ivGoBack.setOnClickListener { viewModel.undo() }
        binding.ivGoForward.setOnClickListener { viewModel.redo() }
        binding.ivSelectImg.setOnClickListener { selectGallery() }

        binding.etPostTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.updateTitle(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etPostContent.addTextChangedListener(object : TextWatcher {
            private var debounceJob: Job? = null
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(s: Editable?) {
                debounceJob?.cancel()
                debounceJob = lifecycleScope.launch {
                    delay(500)
                    viewModel.updateContent(SpannableStringBuilder(s))
                }
            }
        })
    }

    private fun initPhotoPicker() {
        photoPickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    result.data?.data?.let { uri: Uri ->
                        val cursorPosition = binding.etPostContent.selectionStart
                        viewModel.insertImage(uri, cursorPosition)
                    }
                }
            }
    }

    private fun selectGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 이상
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_DENIED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
                    REQ_GALLERY
                )
            } else {
                openGallery()
            }
        } else {
            // Android 12 이하
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQ_GALLERY
                )
            } else {
                openGallery()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        photoPickerLauncher.launch(intent)
    }

    private fun showPopupWindow() {
        val popupBinding = PopupCommuPostWriteSortBinding.inflate(layoutInflater)
        val popupWindow = PopupWindow(
            popupBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.showAsDropDown(binding.llSelectPostSort)

        popupBinding.tvFree.setOnClickListener {
            updateSelectedPostSort(popupBinding.tvFree.text.toString(), popupWindow)
        }

        popupBinding.tvSuggest.setOnClickListener {
            updateSelectedPostSort(popupBinding.tvSuggest.text.toString(), popupWindow)
        }

        popupBinding.tvColumn.setOnClickListener {
            updateSelectedPostSort(popupBinding.tvColumn.text.toString(), popupWindow)
        }
    }

    private fun updateSelectedPostSort(selectedText: String, popupWindow: PopupWindow) {
        viewModel.updatePostSort(selectedText)
        popupWindow.dismiss()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_GALLERY && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        }
    }

    private fun setupObservers() {
        viewModel.postContent.observe(viewLifecycleOwner) { newContent ->
            if (binding.etPostContent.text.toString() != newContent.toString()) {
                val currentScrollY = binding.etPostContent.scrollY
                binding.etPostContent.setText(newContent, TextView.BufferType.SPANNABLE)
                binding.etPostContent.scrollY = currentScrollY
            }
        }

        viewModel.postSort.observe(viewLifecycleOwner) { postSort ->
            binding.tvSelectPostSort.text = postSort
        }

        viewModel.postSendReady.observe(viewLifecycleOwner) { isReady ->
            binding.tvFinish.isEnabled = isReady

            val color = if (isReady) {
                ContextCompat.getColor(requireContext(), R.color.signature_1)
            } else {
                ContextCompat.getColor(requireContext(), R.color.cement_4)
            }

            binding.tvFinish.backgroundTintList = ColorStateList.valueOf(color)
        }
    }
}
