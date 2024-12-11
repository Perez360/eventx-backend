package com.codex.business.common


sealed class EBAction {
    val PING: String = "PING"
    val CREATE: String = "CREATE"
    val UPDATE: String = "UPDATE"
    val GET_BY_ID: String = "GET_BY_ID"
    val LIST: String = "LIST"
    val SEARCH: String = "SEARCH"
    val DELETE_BY_ID: String = "DELETE_BY_ID"

    data object UserEBAction : EBAction()
    data object KycEBAction : EBAction()
    data object ContactEBAction : EBAction()
    data object CommentEBAction : EBAction()
    data object MediaEBAction : EBAction()
    data object DocumentEBAction : EBAction()
    data object EventEBAction : EBAction()
    data object EventCategoryEBAction : EBAction()
    data object FileEBAction : EBAction() {
        const val UPLOAD: String = "UPLOAD"
        const val DOWNLOAD: String = "DOWNLOAD"
    }

    data object OneSignalEBAction : EBAction() {
        const val PUSH_NOTIFICATION = "PUSH_NOTIFICATION"
    }
}