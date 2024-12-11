package com.codex.business.components.user.enum

enum class VerificationStatus {
    PENDING, // The verification process has not yet been started or is in progress.
    VERIFIED, // The verification process has been completed successfully.
    UNVERIFIED, // The verification process has been done.
    FAILED, // The verification process has failed.
    EXPIRED, // The verification process has expired, possibly due to a timeout or deadline.
    REVOKED, // The verification status has been revoked, possibly due to new information or changes.
}