package com.codex.business.components.kyc.repo

import com.codex.business.components.user.enum.VerificationStatus
import dev.morphia.annotations.*
import dev.morphia.utils.IndexType
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Indexes(
    Index(
        fields = [
            Field(value = "firstName", type = IndexType.DESC),
            Field(value = "lastName", type = IndexType.DESC),
            Field(value = "otherName", type = IndexType.DESC),
            Field(value = "dateOfBirth", type = IndexType.DESC),
            Field(value = "status", type = IndexType.DESC),
            Field(value = "nationality", type = IndexType.DESC),
            Field(value = "createdAt", type = IndexType.DESC),
            Field(value = "modifiedAt", type = IndexType.DESC),
        ], options = IndexOptions(background = true)
    )
)
@EntityListeners(KycEntityListener::class)
data class Kyc(
    @Id
    var id: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var otherName: String? = null,
    var nationality: String? = null,
    var userId: String? = null,
    @Property("dob")
    var dateOfBirth: LocalDate? = null,
    var status: VerificationStatus? = VerificationStatus.UNVERIFIED,
    var createdAt: LocalDateTime? = LocalDateTime.now(),
    var modifiedAt: LocalDateTime? = null,
    @Version
    var version: Long? = 0
) {

    override fun toString(): String {
        return "Kyc(" +
                "id=$id, " +
                "firstName=$firstName, " +
                "lastName=$lastName, " +
                "otherName=$otherName, " +
                "nationality=$nationality, " +
                "userId=$userId, " +
                "dateOfBirth=$dateOfBirth, " +
                "status=$status, " +
                "createdAt=$createdAt, " +
                "modifiedAt=$modifiedAt, " +
                "version=$version" +
                ")"
    }

}