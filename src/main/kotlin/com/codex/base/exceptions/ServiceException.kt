package com.codex.base.exceptions


class ServiceException(override val message: String?, cause: Throwable? = null) : RuntimeException(message, cause)
