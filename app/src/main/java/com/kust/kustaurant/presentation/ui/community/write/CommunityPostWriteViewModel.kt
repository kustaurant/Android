package com.kust.kustaurant.presentation.ui.community.write

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.domain.model.community.PostCategory
import com.kust.kustaurant.domain.usecase.community.PatchPostModifyUseCase
import com.kust.kustaurant.domain.usecase.community.PostCommunityPostCreateUseCase
import com.kust.kustaurant.domain.usecase.community.PostCommunityUploadImageUseCase
import com.kust.kustaurant.presentation.model.CommunityPostIntent
import com.kust.kustaurant.presentation.ui.util.ImageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CommunityPostWriteViewModel @Inject constructor(
    private val imageUtil: ImageUtil,
    private val postPostCreate: PostCommunityPostCreateUseCase,
    private val postUploadImage: PostCommunityUploadImageUseCase,
    private val patchPostModify: PatchPostModifyUseCase,
) : ViewModel() {
    private val _postTitle = MutableLiveData<String>()
    val postTitle: LiveData<String> get() = _postTitle

    private val _postCategory = MutableLiveData<PostCategory?>()
    val postCategory: LiveData<PostCategory?> get() = _postCategory

    private val _postSendReady = MutableLiveData<Boolean>()
    val postSendReady: LiveData<Boolean> get() = _postSendReady

    private val _postCreateResult = MutableLiveData<PostFinishState?>()
    val postCreateResult: LiveData<PostFinishState?> get() = _postCreateResult

    val toastMessage: LiveData<String?> get() = _toastMessage
    private val _toastMessage = MutableLiveData<String?>()

    private val _postContentHtml = MutableLiveData<String>()
    private val _isEditMode = MutableLiveData<Boolean>()

    companion object {
        private const val MAX_FILE_SIZE = 10L * 1024 * 1024 // 10MB
    }

    private suspend fun uploadImage(uri: Uri, fileName: String): String? =
        withContext(Dispatchers.IO) {
            try {
                val mime = imageUtil.queryMimeType(uri)
                val size = imageUtil.querySize(uri)
                if (size > 0 && size > MAX_FILE_SIZE) {
                    Log.e("Upload", "File too large: $size")
                    _toastMessage.postValue("파일 크기가 너무 큽니다. (최대 10MB까지 업로드 가능)")
                    return@withContext null
                }

                val part = imageUtil.asStreamingPart(
                    uri = uri,
                    formField = "image",
                    fileName = fileName.ifBlank { "upload.jpg" },
                    mimeType = mime
                )
                return@withContext postUploadImage(part).imgUrl
            } catch (se: SecurityException) {
                Log.e("Upload", "SecurityException: $se")
                _toastMessage.postValue("권한 문제가 발생했습니다.")
                null
            } catch (e: Exception) {
                Log.e("Upload", "Upload error: $e")
                _toastMessage.postValue("업로드 중 오류가 발생했습니다.")
                null
            }
        }

    fun setEditMode(postSummary: CommunityPostIntent?) {
        _isEditMode.value = postSummary?.postBody != null
    }

    fun resetPostCreateResult() {
        _postCreateResult.value = PostFinishState.FINISH_IDLE
    }

    fun resetPostSendReady() {
        _postSendReady.value = false
    }

    suspend fun uploadImageAndGetUrl(uri: Uri, fileName: String): String? {
        return uploadImage(uri, fileName)
    }

    fun onSubmit(postId: Long?) {
        if (_isEditMode.value == true)
            postId?.let { modifyPost(it) }
        else if (_isEditMode.value == false)
            createPost()
    }

    private fun modifyPost(postId: Long) {
        viewModelScope.launch {
            try {
                patchPostModify(
                    postId.toString(),
                    _postTitle.value!!,
                    _postCategory.value ?: PostCategory.FREE,
                    _postContentHtml.value!!
                )

                _postCreateResult.value = PostFinishState.FINISH_MODIFY
            } catch (e: Exception) {
                Log.e("CommunityPostWriteViewModel", "From modifyPost, Err is $e")
                _postCreateResult.value = PostFinishState.FINISH_ERR
            }
        }
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }

    private fun createPost() {
        viewModelScope.launch {
            try {
                postPostCreate(
                    _postTitle.value!!,
                    _postCategory.value ?: PostCategory.FREE,
                    _postContentHtml.value!!
                )

                _postCreateResult.value = PostFinishState.FINISH_CREATE
            } catch (e: Exception) {
                Log.e("CommunityPostWriteViewModel", "From createPost, Err is $e")
                _postCreateResult.value = PostFinishState.FINISH_ERR
            }
        }
    }

    fun updateContentFromHtml(html: String) {
        _postContentHtml.value = html
        isPostSendReady()
    }


    fun updatePostSort(newCategory: PostCategory) {
        _postCategory.value = newCategory
        isPostSendReady()
    }

    fun updateTitle(title: String) {
        _postTitle.value = title
        isPostSendReady()
    }

    private fun isPostSendReady() {
        val content = _postContentHtml.value?.replace(Regex("<p><br></p>|<br>|<p></p>"), "")?.trim()
        val isContentNotEmpty = !content.isNullOrEmpty()
        _postSendReady.value =
            _postCategory.value != null &&
                    _postCategory.value != PostCategory.ALL &&
                    isContentNotEmpty &&
                    !_postTitle.value.isNullOrEmpty()
    }

    fun setToastMessage(message: String) {
        _toastMessage.value = message
    }
}

enum class PostFinishState {
    FINISH_IDLE, FINISH_ERR, FINISH_MODIFY, FINISH_CREATE
}
