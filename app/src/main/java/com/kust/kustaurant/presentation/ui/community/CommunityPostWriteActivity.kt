package com.kust.kustaurant.presentation.ui.community

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ActivityCommunityPostWriteBinding
import com.kust.kustaurant.databinding.PopupCommuPostWriteSortBinding
import com.kust.kustaurant.presentation.common.BaseActivity
import com.kust.kustaurant.presentation.model.CommunityPostIntent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommunityPostWriteActivity : BaseActivity() {
    private lateinit var binding: ActivityCommunityPostWriteBinding
    private val viewModel: CommunityPostWriteViewModel by viewModels()
    private var textChangeJob: Job? = null
    private var postId : Int? = null
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var docPickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityPostWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel

        initWebView()
        initPhotoPicker()
        initFallback()
        setupUI()
        observers()

        val postSummary = intent.getParcelableExtra<CommunityPostIntent>("postSummary")
        viewModel.setEditMode(postSummary)

        binding.etPostContent.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                postSummary?.let {

                    // JavaScript 전달을 위한 안전한 이스케이프 처리
                    val escapedHtmlContent = it.postBody.replace("'", "\\'")
                        .replace("\n", "\\n")
                        .replace("\"", "\\\"")

                    viewModel.updateContentFromHtml(it.postBody)
                    viewModel.updateTitle(it.postTitle)
                    viewModel.updatePostSort(it.postCategory)

                    postId = it.postId
                    binding.etPostContent.evaluateJavascript("javascript:setHtmlContent('$escapedHtmlContent');", null)
                }
            }
        }
    }
    private fun observers() {
        viewModel.postTitle.observe(this) { title ->
            if (binding.etPostTitle.text.toString() != title) {
                binding.etPostTitle.setText(title)
            }
        }

        viewModel.postSort.observe(this) { sort ->
            binding.tvSelectPostSort.text = sort
        }

        viewModel.postSendReady.observe(this) { isReady ->
            binding.tvFinish.isEnabled = isReady
            binding.tvFinish.isClickable = isReady
            val color = if (isReady) {
                ContextCompat.getColor(this, R.color.signature_1)
            } else {
                ContextCompat.getColor(this, R.color.cement_4)
            }
            binding.tvFinish.backgroundTintList = ColorStateList.valueOf(color)
        }

        viewModel.toastMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.clearToastMessage()
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        binding.etPostContent.settings.javaScriptEnabled = true
        binding.etPostContent.settings.domStorageEnabled = true
        binding.etPostContent.loadUrl("file:///android_asset/quill.html")

        binding.etPostContent.addJavascriptInterface(JSInterface(), "JSInterface")
    }

    private fun setupUI() {
        binding.ivBold.setOnClickListener {
            binding.etPostContent.evaluateJavascript("javascript:setBold();", null)
        }

        binding.ivGoBack.setOnClickListener {
            binding.etPostContent.evaluateJavascript("javascript:quill.history.undo();", null)
        }
        binding.ivGoForward.setOnClickListener {
            binding.etPostContent.evaluateJavascript("javascript:quill.history.redo();", null)
        }

        binding.ivSelectImg.setOnClickListener {
            selectGallery()
        }

        binding.llSelectPostSort.setOnClickListener { showPopupWindow() }

        binding.etPostTitle.addTextChangedListener { text ->
            val newText = text.toString()
            if (viewModel.postTitle.value != newText) {
                viewModel.updateTitle(newText)
            }
        }

        binding.tvFinish.setOnClickListener {

            viewModel.onSubmit(postId)
        }

        binding.btnBack.setOnClickListener{
            finish()
        }

        viewModel.postCreateResult.observe(this) { res ->
            when(res) {
                PostFinishState.FINISH_MODIFY -> {
                    Toast.makeText(this, "게시글이 성공적으로 수정되었습니다.", Toast.LENGTH_SHORT).show()
                    viewModel.resetPostCreateResult()
                    finish()
                }
                PostFinishState.FINISH_ERR ->{
                    Toast.makeText(this, "오류가 발생하였습니다. 잠시후 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    viewModel.resetPostCreateResult()
                }
                PostFinishState.FINISH_CREATE -> {
                    Toast.makeText(this, "게시글이 성공적으로 업로드되었습니다.", Toast.LENGTH_SHORT).show()
                    viewModel.resetPostCreateResult()
                    finish()
                }
                PostFinishState.FINISH_IDLE, null -> {
                }
            }
        }
    }

    // Fragment에서 ViewModel에서 반환된 HTML 값을 직접 설정하고 커서 위치 재확인
    private fun initPhotoPicker() {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                lifecycleScope.launch {
                    val imageUrl = viewModel.uploadImageAndGetUrl(uri, getFileNameFromUri(uri))
                    imageUrl?.let {
                        insertImageAtCursor(it)
                    }
                }
            }
        }
    }

    private fun initFallback() {
        docPickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                if (uri != null) {
                    lifecycleScope.launch {
                        val url = viewModel.uploadImageAndGetUrl(uri, getFileNameFromUri(uri))
                        url?.let { insertImageAtCursor(it) }
                    }
                }
            }
        }
    }

    private fun insertImageAtCursor(imageUrl: String) {
        // JavaScript를 호출하여 이미지를 현재 커서 위치에 삽입
        binding.etPostContent.evaluateJavascript("javascript:insertImage('$imageUrl');", null)
    }

    private fun selectGallery() {  
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            pickMedia.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        } else {
            // Android 8~10(API 26~29)
            openDocumentFallback() 
    }

    private fun selectGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            pickMedia.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        } else {
            // Android 8~10(API 26~29)
            openDocumentFallback()
        }
    }

    private fun showPopupWindow() {
        val popupBinding = PopupCommuPostWriteSortBinding.inflate(layoutInflater)
        val popupWindow = PopupWindow(
            popupBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.showAsDropDown(binding.llSelectPostSort, 0, -binding.llSelectPostSort.height)

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

    private fun getFileNameFromUri(uri: Uri): String {
        var fileName: String? = null
        val cursor = contentResolver?.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName ?: "default.jpg"
    }

    override fun onResume() {
        super.onResume()
        viewModel.resetPostSendReady()
    }

    inner class JSInterface {
        @JavascriptInterface
        fun onTextChange(html: String) {
            lifecycleScope.launch {
                textChangeJob?.cancel()
                textChangeJob = launch {
                    delay(300) // 300ms debounce
                    viewModel.updateContentFromHtml(html)
                }
            }
        }
    }
}