package com.kust.kustaurant.presentation.ui.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.source
import java.io.IOException
import javax.inject.Inject

class ImageUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun queryMimeType(uri: Uri): String =
        context.contentResolver.getType(uri) ?: "image/jpeg"

    fun querySize(uri: Uri): Long {
        val cr = context.contentResolver
        return cr.query(uri, arrayOf(OpenableColumns.SIZE), null, null, null)?.use { c ->
            if (c.moveToFirst()) c.getLong(0) else -1L
        } ?: -1L
    }

    fun asStreamingPart(
        uri: Uri,
        formField: String,
        fileName: String,
        mimeType: String
    ): MultipartBody.Part {
        val body = object : RequestBody() {
            override fun contentType() = mimeType.toMediaType()
            override fun contentLength(): Long = -1
            override fun writeTo(sink: okio.BufferedSink) {
                val input = context.contentResolver.openInputStream(uri)
                    ?: throw IOException("Cannot open input stream for $uri")
                input.use { stream ->
                    sink.writeAll(stream.source())
                }
            }
        }
        return MultipartBody.Part.createFormData(formField, fileName, body)
    }
}
