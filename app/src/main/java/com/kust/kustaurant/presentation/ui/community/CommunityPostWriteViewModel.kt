package com.kust.kustaurant.presentation.ui.community

import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.text.style.StyleSpan
import android.util.Log
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

    private val _postContent = MutableLiveData<SpannableStringBuilder>()
    val postContent: LiveData<SpannableStringBuilder> get() = _postContent

    private val _postSort = MutableLiveData<String>()
    val postSort: LiveData<String> get() = _postSort

    private val _postSendReady = MutableLiveData<Boolean>()
    val postSendReady: LiveData<Boolean> get() = _postSendReady


    private val undoStack = Stack<SpannableStringBuilder>()
    private val redoStack = Stack<SpannableStringBuilder>()

    private fun saveToUndoStack() {
        _postContent.value?.let { content ->
            undoStack.push(SpannableStringBuilder(content))
            Log.d("CommunityViewModel", "Undo stack saved. Current undo stack size: ${undoStack.size}")
        }
    }

    fun updateContent(newContent: SpannableStringBuilder) {
        saveToUndoStack()  // 기존 postContent의 상태를 저장
        _postContent.value = newContent
        isPostSendReady()
    }

    fun applyBold(start: Int, end: Int) {
        _postContent.value?.let { content ->
            val spannable = SpannableStringBuilder(content)
            spannable.setSpan(
                StyleSpan(Typeface.BOLD), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            _postContent.value = spannable
            saveToUndoStack()
            Log.d("CommunityViewModel", "Bold applied from $start to $end")
        } ?: Log.d("CommunityViewModel", "applyBold: No content to apply bold")
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            redoStack.push(_postContent.value ?: SpannableStringBuilder())
            _postContent.value = undoStack.pop()
            Log.d("CommunityViewModel", "Undo performed. Undo stack size: ${undoStack.size}, Redo stack size: ${redoStack.size}")
        } else {
            Log.d("CommunityViewModel", "Undo stack is empty. Nothing to undo.")
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            undoStack.push(_postContent.value ?: SpannableStringBuilder())
            _postContent.value = redoStack.pop()
            Log.d("CommunityViewModel", "Redo performed. Undo stack size: ${undoStack.size}, Redo stack size: ${redoStack.size}")
        } else {
            Log.d("CommunityViewModel", "Redo stack is empty. Nothing to redo.")
        }
    }

    fun insertImage(uri: Uri, cursorPosition: Int) {
        val content = _postContent.value ?: SpannableStringBuilder()
        val spannable = SpannableStringBuilder(content)
        val imageSpan = imageSpanHelper.createImageSpan(uri)
        spannable.insert(cursorPosition, " ")
        spannable.setSpan(
            imageSpan,
            cursorPosition,
            cursorPosition + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        _postContent.value = spannable
        saveToUndoStack()
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
        val isContentNotEmpty = _postContent.value?.isNotEmpty() == true && _postContent.value?.trim().toString().isNotEmpty()
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
