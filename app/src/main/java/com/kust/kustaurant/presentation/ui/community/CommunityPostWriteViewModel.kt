package com.kust.kustaurant.presentation.ui.community

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val _postContentHtml = MutableLiveData<String>()
    private val _postSort = MutableLiveData<String>()
    val postSort: LiveData<String> get() = _postSort
    private val _postSendReady = MutableLiveData<Boolean>()
    val postSendReady: LiveData<Boolean> get() = _postSendReady
    private val _postCreateResult = MutableLiveData<PostFinishState?>()
    val postCreateResult: LiveData<PostFinishState?> get() = _postCreateResult
    private val _isEditMode = MutableLiveData<Boolean>()
    val toastMessage : LiveData<String?> get() = _toastMessage
    private val _toastMessage = MutableLiveData<String?>()

    private suspend fun uploadImage(uri: Uri, fileName: String): String? = withContext(Dispatchers.IO) {
        try {
            val mime = imageUtil.queryMimeType(uri)
            val size = imageUtil.querySize(uri)
            val max = 10L * 1024 * 1024
            if (size in 1..Long.MAX_VALUE && size > max) {
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

    fun onSubmit(postId : Int?) {
        if(_isEditMode.value == true)
            postId?.let { modifyPost(it) }
        else if(_isEditMode.value == false)
            createPost()
    }

    private fun modifyPost(postId: Int) {
        viewModelScope.launch {
            try {
                patchPostModify(
                    postId.toString(),
                    _postTitle.value!!,
                    _postSort.value!!,
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
                    _postSort.value!!,
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


    fun updatePostSort(selectedSort: String) {
        _postSort.value = selectedSort
        isPostSendReady()
    }

    fun updateTitle(title: String) {
        _postTitle.value = title
        isPostSendReady()
    }

    private fun isPostSendReady() {
        val content = _postContentHtml.value?.replace(Regex("<p><br></p>|<br>|<p></p>"), "")?.trim()
        val isContentNotEmpty = !content.isNullOrEmpty()
        _postSendReady.value = !_postSort.value.isNullOrEmpty() &&
                isContentNotEmpty &&
                !_postTitle.value.isNullOrEmpty()
    }
}

enum class PostFinishState{
    FINISH_IDLE, FINISH_ERR, FINISH_MODIFY, FINISH_CREATE
}
