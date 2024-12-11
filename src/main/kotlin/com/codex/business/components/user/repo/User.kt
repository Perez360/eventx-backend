package com.codex.business.components.user.repo

import com.codex.business.common.enums.Status
import com.codex.business.components.kyc.repo.Kyc
import com.codex.business.components.userProfile.repo.UserProfile
import dev.morphia.annotations.*
import dev.morphia.utils.IndexType
import java.time.LocalDateTime

@Entity("users")
@Indexes(
    Index(
        fields = [
            Field(value = "firstName", type = IndexType.DESC),
            Field(value = "lastName", type = IndexType.DESC),
            Field(value = "otherName", type = IndexType.DESC),
            Field(value = "status", type = IndexType.DESC),
            Field(value = "role", type = IndexType.DESC),
            Field(value = "createdAt", type = IndexType.DESC),
            Field(value = "modifiedAt", type = IndexType.DESC),
        ], options = IndexOptions(background = true)
    ),
    Index(fields = [Field(value = "email"), Field(value = "phone")], options = IndexOptions(unique = true))
)
@EntityListeners(UserEntityListener::class)
data class User(
    @Id
    var id: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var otherName: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var password: String? = null,
    var status: Status? = Status.ENABLED,
    var role: String? = null,
    @Reference(idOnly = true, ignoreMissing = true)
    var kyc: Kyc? = null,
    @Reference(idOnly = true, ignoreMissing = true)
    var profile: UserProfile? = null,
    var createdAt: LocalDateTime? = LocalDateTime.now(),
    var modifiedAt: LocalDateTime? = null,

    @Version
    var version: Long? = null
) {

    override fun toString(): String {
        return "User(" +
                "id=$id, " +
                "firstName=$firstName, " +
                "lastName=$lastName, " +
                "otherName=$otherName, " +
                "email=$email, " +
                "phone=$phone, " +
                "password=$password, " +
                "status=$status, " +
                "role=$role, " +
                "kyc=$kyc, " +
                "profile=$profile, " +
                "createdAt=$createdAt, " +
                "modifiedAt=$modifiedAt, " +
                "version=$version" +
                ")"
    }
}

