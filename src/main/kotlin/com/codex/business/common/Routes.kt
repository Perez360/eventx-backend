package com.codex.business.common

sealed class Routes(val path: String) {
    companion object {
         const val BASE = "/api/v1"
    }

    data object User : Routes("$BASE/user/*")
    data object Contact : Routes("$BASE/contact/*")
    data object Comment : Routes("$BASE/comment/*")
    data object Kyc : Routes("$BASE/kyc/*")
    data object Event : Routes("$BASE/event/*")
    data object Category : Routes("$BASE/category/*")
    data object Media : Routes("$BASE/media/*")
    data object Document : Routes("$BASE/document/*")
    data object File : Routes("$BASE/file/*")
    data object Menu : Routes("$BASE/menu/*")
}