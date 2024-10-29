package com.kust.kustaurant.data.di

class BadRequestException(
    override val message: String?
): RuntimeException()

/**
 * 401: unauthorized
 */
class UnauthorizedException(
    override val message: String?
): RuntimeException()

/**
 * 403: forbidden
 */
class ForbiddenException(
    override val message: String?
): RuntimeException()

/**
 * 404: not found
 */
class NotFoundException(
    override val message: String?
): RuntimeException()

/**
 * 500: server error
 */
class ServerException(
    override val message: String?
): RuntimeException()

/**
 * response time out
 */
class TimeOutException(
    override val message: String?
) : RuntimeException()

/**
 * other error
 */
class OtherHttpException(
    val code: Int?,
    override val message: String?
): RuntimeException()

/**
 * unknown error
 */
class UnknownException(
    override val message: String?
): RuntimeException()

class InternetException(
    override val message: String?
): RuntimeException()