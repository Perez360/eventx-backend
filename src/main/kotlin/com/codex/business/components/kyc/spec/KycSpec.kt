package com.codex.business.components.kyc.spec


import com.codex.base.shared.Spec
import com.codex.business.components.user.enum.VerificationStatus
import com.codex.business.components.user.repo.User
import dev.morphia.query.filters.Filter
import dev.morphia.query.filters.Filters
import java.time.LocalDate
import java.time.LocalDateTime

data class KycSpec(
    var firstName: String? = null,
    var lastName: String? = null,
    var otherName: String? = null,
    var occupation: String? = null,
    var dateOfBirth: LocalDate? = null,
    var status: VerificationStatus? = null,
    var startDate: LocalDateTime? = null,
    var endDate: LocalDateTime? = null,
    var createdAt: LocalDateTime? = null,
    var modifiedAt: LocalDateTime? = null,
) : Spec() {

    override fun toParameters(): Map<String, Any?> = mapOf(
        ::firstName.name to firstName,
        ::lastName.name to lastName,
        ::otherName.name to otherName,
        ::occupation.name to occupation,
        ::dateOfBirth.name to dateOfBirth,
        ::status.name to status,
        ::createdAt.name to createdAt,
        ::modifiedAt.name to modifiedAt,
        ::startDate.name to startDate,
        ::endDate.name to endDate
    )


    override fun queryDefinition(param: Map.Entry<String, Any?>): Filter {
        return when (param.key) {
            ::firstName.name,
            ::lastName.name,
            ::otherName.name,
                -> Filters.regex(param.key, param.value.toString())
                .caseInsensitive()

            ::startDate.name -> Filters.gte(User::createdAt.name, param.value!!)
            ::endDate.name -> Filters.lte(User::createdAt.name, param.value!!)
            else -> Filters.eq(param.key, param.value)
        }
    }

    override fun toString(): String {
        return "(" +
                "firstName=$firstName, " +
                "lastName=$lastName, " +
                "otherName=$otherName, " +
                "occupation=$occupation, " +
                "dateOfBirth=$dateOfBirth, " +
                "status=$status, " +
                "startDate=$startDate, " +
                "endDate=$endDate, " +
                "createdAt=$createdAt, " +
                "modifiedAt=$modifiedAt" +

                "sortOrder=$sortOrder, " +
                "sortBy=$sortBy, " +
                "operator=$operator, " +
                "page=$page, " +
                "size=$size" +
                ")"
    }
}