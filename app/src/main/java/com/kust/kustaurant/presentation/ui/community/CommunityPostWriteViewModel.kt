package com.kust.kustaurant.presentation.ui.community

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.domain.usecase.community.PostCommunityPostCreateUseCase
import com.kust.kustaurant.domain.usecase.community.PostCommunityUploadImageUseCase
import com.kust.kustaurant.domain.usecase.community.PatchPostModifyUseCase
import com.kust.kustaurant.presentation.model.CommunityPostIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.IOException
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
    val postContentHtml: LiveData<String> get() = _postContentHtml
    private val _postSort = MutableLiveData<String>()
    val postSort: LiveData<String> get() = _postSort
    private val _postSendReady = MutableLiveData<Boolean>()
    val postSendReady: LiveData<Boolean> get() = _postSendReady
    private val _postCreateResult = MutableLiveData<PostFinishState?>()
    val postCreateResult: LiveData<PostFinishState?> get() = _postCreateResult
    private val _isEditMode = MutableLiveData<Boolean>()
    private suspend fun uploadImage(uri: Uri, fileName: String): String? {
        return try {
            val imageBytes = imageUtil.getImageBytesFromUri(uri)
            if (imageBytes != null) {
                val requestFile = RequestBody.create(
                    "image/*".toMediaTypeOrNull(),
                    imageBytes
                )

                val imagePart = MultipartBody.Part.createFormData("image", fileName, requestFile)

                postUploadImage(imagePart).imgUrl
            } else {
                Log.e("CommunityPostWriteViewModel", "Failed to get image bytes")
                null
            }
        } catch (e: Exception) {
            Log.e("CommunityPostWriteViewModel", "Upload error: $e")
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
        // 빈 태그를 정규식으로 제거
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

class ImageUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getImageBytesFromUri(uri: Uri): ByteArray? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
