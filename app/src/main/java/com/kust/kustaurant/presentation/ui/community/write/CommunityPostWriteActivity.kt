package com.kust.kustaurant.presentation.ui.community.write

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.inputmethod.InputMethodManager
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
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ActivityCommunityPostWriteBinding
import com.kust.kustaurant.databinding.PopupCommuPostWriteSortBinding
import com.kust.kustaurant.domain.model.community.PostCategory
import com.kust.kustaurant.domain.model.community.toPostCategory
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
    private var postId : Long? = null
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var docPickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityPostWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel

        initWebView()
        initPhotoPicker()
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
                    viewModel.updatePostSort(it.postCategory.toPostCategory())

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

        viewModel.postCategory.observe(this) { category ->
            binding.tvSelectPostSort.text = category?.displayName
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
            uri ?: return@registerForActivityResult
            lifecycleScope.launch {
                viewModel.uploadImageAndGetUrl(uri, getFileNameFromUri(uri))?.let { insertImageAtCursor(it) }
            }
        }

        docPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
            val uri = res.data?.data ?: return@registerForActivityResult
            try {
                contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } catch (_: SecurityException) { /* 공급자 미지원 or 이미 보유 */ }
            lifecycleScope.launch {
                viewModel.uploadImageAndGetUrl(uri, getFileNameFromUri(uri))?.let { insertImageAtCursor(it) }
            }
        }
    }

    private fun insertImageAtCursor(imageUrl: String) {
        binding.etPostContent.evaluateJavascript("javascript:insertImage('$imageUrl');", null)

        binding.etPostContent.postDelayed({ showImeNow() }, 16)
    }

    private fun showImeNow() {
        binding.etPostContent.isFocusable = true
        binding.etPostContent.isFocusableInTouchMode = true
        binding.etPostContent.requestFocus()

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etPostContent, InputMethodManager.SHOW_IMPLICIT)

        WindowInsetsControllerCompat(window, binding.etPostContent)
            .show(WindowInsetsCompat.Type.ime())
    }

    private fun selectGallery() {
        if (ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(this)) {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            openDocumentFallback()
        }
    }

    private fun openDocumentFallback() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        docPickerLauncher.launch(intent)
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
            updateSelectedPostSort(PostCategory.FREE, popupWindow)
        }

        popupBinding.tvSuggest.setOnClickListener {
            updateSelectedPostSort(PostCategory.SUGGESTION, popupWindow)
        }

        popupBinding.tvColumn.setOnClickListener {
            updateSelectedPostSort(PostCategory.COLUMN, popupWindow)
        }
    }

    private fun updateSelectedPostSort(category: PostCategory, popupWindow: PopupWindow) {
        viewModel.updatePostSort(category)
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