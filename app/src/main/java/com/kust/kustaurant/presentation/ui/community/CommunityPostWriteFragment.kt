package com.kust.kustaurant.presentation.ui.community

import android.annotation.SuppressLint
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
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
    private var cursorPosition: Int = 0 // 커서 위치를 추적할 변수
    private var textChangeJob: Job? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityPostWriteBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initWebView()
        initPhotoPicker()
        setupUI()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        binding.etPostContent.settings.javaScriptEnabled = true
        binding.etPostContent.addJavascriptInterface(JSInterface(), "JSInterface")

        // 페이지가 로드될 때 커서 위치 추적 JavaScript 코드 설정
        binding.etPostContent.evaluateJavascript("""
        document.addEventListener("selectionchange", function() {
            var selection = window.getSelection();
            var cursorPosition = selection.anchorOffset;
            JSInterface.onGetCursorPosition(cursorPosition);
        });
    """.trimIndent(), null)
    }

    private fun setupUI() {
        // Bold button for RichEditor
        binding.ivBold.setOnClickListener {
            binding.etPostContent.setBold()
        }

        // Undo/Redo functionality
        binding.ivGoBack.setOnClickListener { binding.etPostContent.undo() }
        binding.ivGoForward.setOnClickListener { binding.etPostContent.redo() }

        // Image selection
        binding.ivSelectImg.setOnClickListener {
            binding.etPostContent.evaluateJavascript(
                "JSInterface.onGetCursorPosition(window.getSelection().anchorOffset);",
                null
            )
            selectGallery()
        }

        // Listen for HTML content changes
        binding.etPostContent.setOnTextChangeListener { html ->
            textChangeJob?.cancel()
            textChangeJob = lifecycleScope.launch {
                delay(300) // 300ms debounce
                viewModel.updateContentFromHtml(html)
            }
        }

        binding.etPostContent.setOnFocusChangeListener { _, hasFocus ->
                viewModel.onFocusChanged(hasFocus)
        }

        // Observe post sort selection
        binding.llSelectPostSort.setOnClickListener { showPopupWindow() }

        // Title update listener
        binding.etPostTitle.addTextChangedListener { text ->
            viewModel.updateTitle(text.toString())
        }
    }

    private fun initPhotoPicker() {
        photoPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    val newCursorPos = viewModel.insertImageAtCursor(uri.toString(), cursorPosition)
                    // JavaScript로 새로운 커서 위치 설정
                    binding.etPostContent.evaluateJavascript(
                        "window.getSelection().collapse(null, $newCursorPos);", null
                    )
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
        viewModel.hintVisible.observe(viewLifecycleOwner) { isVisible ->
            Log.d("CommuPostView", "${binding.etPostContent.html}}")
            Log.d("CommuPostView", "isVisible is $isVisible")
            if (isVisible && !binding.etPostContent.hasFocus()) {
                //!binding.etPostContent.hasFocus() 조건을 추가한 이유 : etPostContent에서 글자를 썻다가 지울경우 Hint가 바로 나오기 때문에.
                binding.etPostContent.html = getString(R.string.community_post_write_info)
                binding.etPostContent.setTextColor(ContextCompat.getColor(requireContext(), R.color.cement_3))
            } else if (binding.etPostContent.html == getString(R.string.community_post_write_info)) {

                binding.etPostContent.html = ""
                binding.etPostContent.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
        }

        viewModel.postContentHtml.observe(viewLifecycleOwner) { htmlContent ->
            if (binding.etPostContent.html != htmlContent) {
                binding.etPostContent.html = htmlContent
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

    // JSInterface 클래스 정의
    inner class JSInterface {
        @JavascriptInterface
        fun onGetCursorPosition(position: Int) {
            // JavaScript에서 가져온 커서 위치를 Android의 cursorPosition에 저장
            cursorPosition = position
            Log.d("CommuPostWriteFragment", "Cursor position updated to: $cursorPosition")
        }

        @JavascriptInterface
        fun insertImageAtCursor(imageUri: String) {
            viewModel.insertImageAtCursor(imageUri, cursorPosition)
        }
    }
}
