package com.carollibrary.exception

class BadRequestException(override val message: String, val errorCode: String): Exception() {
}