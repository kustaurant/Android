package com.kust.kustaurant.presentation.ui.community

import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.text.style.ImageSpan
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kust.kustaurant.domain.usecase.community.GetCommunityPostListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Stack
import javax.inject.Inject
@HiltViewModel
class CommunityPostWriteViewModel @Inject constructor(
    private val imageSpanHelper: ImageSpanHelper,
    private val postPostCreate: GetCommunityPostListUseCase,
) : ViewModel() {

    private val _postTitle = MutableLiveData<String>()
    val postTitle: LiveData<String> get() = _postTitle

    private val _postContentHtml = MutableLiveData<String>()
    val postContentHtml: LiveData<String> get() = _postContentHtml

    private val _postSort = MutableLiveData<String>()
    val postSort: LiveData<String> get() = _postSort

    private val _postSendReady = MutableLiveData<Boolean>()
    val postSendReady: LiveData<Boolean> get() = _postSendReady

    private val _hintVisible = MutableLiveData<Boolean>(true)
    val hintVisible: LiveData<Boolean> get() = _hintVisible


    fun updateContentFromHtml(html: String) {
        _postContentHtml.value = html
        _hintVisible.value = html.isEmpty()
        isPostSendReady()
    }

    fun onFocusChanged(hasFocus: Boolean) {
        _hintVisible.value = !hasFocus && _postContentHtml.value.isNullOrEmpty()
    }

    fun insertImage(uri: Uri) {
        val currentHtml = _postContentHtml.value ?: ""
        val newHtml = "$currentHtml<img src='$uri' />"
        _postContentHtml.value = newHtml
        isPostSendReady()
    }

    fun insertImageAtCursor(imageUri: String, cursorPosition: Int): Int {
        val currentHtml = _postContentHtml.value ?: ""
        val imageTag = "<img src='$imageUri' />"
        val updatedHtml = StringBuilder(currentHtml).apply {
            insert(cursorPosition, imageTag)
        }.toString()

        // 새로운 HTML 설정
        _postContentHtml.value = updatedHtml
        isPostSendReady()

        // 새로운 커서 위치를 반환 (이미지 태그 길이를 추가)
        return cursorPosition + imageTag.length
    }



    fun updatePostSort(selectedSort: String) {
        _postSort.value = selectedSort
        isPostSendReady()
    }

    fun updateTitle(title: String) {
        _postTitle.value = title
        isPostSendReady()
    }

    private fun isPostSendReady() {
        val isContentNotEmpty = _postContentHtml.value?.isNotEmpty() == true
        _postSendReady.value = !_postSort.value.isNullOrEmpty() &&
                isContentNotEmpty &&
                !_postTitle.value.isNullOrEmpty()
    }
}

class ImageSpanHelper(private val context: Context) {
    fun createImageSpan(uri: Uri): ImageSpan {
        return ImageSpan(context, uri)
    }
}
