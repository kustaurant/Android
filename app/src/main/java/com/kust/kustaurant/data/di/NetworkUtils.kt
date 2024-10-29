package com.kust.kustaurant.data.di

import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object NetworkUtils {
    // safe api call 함수로 선언
    suspend fun <T> safeApiCall(call: suspend () -> T): Result<T> {
        return try {
            Result.success(call())
        } catch (e: HttpException) {
            throw mapHttpException(e)
        } catch (e: SocketTimeoutException) {
            throw TimeOutException(message = e.message)
        } catch (e: UnknownHostException) {
            throw InternetException(message = e.message)
        } catch (e: Exception) {
            throw UnknownException(message = e.message)
        }
    }

    // retrofit 통신 코드를 매핑
    private fun mapHttpException(e: HttpException): RuntimeException {
        return when(e.code()) {
            400 -> BadRequestException(message = e.message())
            401 -> UnauthorizedException(message = e.message())
            403 -> ForbiddenException(message = e.message())
            404 -> NotFoundException(message = e.message())
            500, 501, 502, 503 -> ServerException(message = e.message())
            else -> OtherHttpException(code = e.code(), message = e.message())
        }
    }
}