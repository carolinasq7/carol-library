package com.carollibrary.exception

class NotFoundException(override val message: String, val errorCode: String): Exception() {
}