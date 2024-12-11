package com.codex.business.components.userProfile.spec


import com.codex.base.shared.Spec
import com.codex.business.common.enums.Status
import com.codex.business.components.user.repo.User
import com.codex.business.components.user.spec.UserSpec
import com.codex.business.components.userProfile.repo.UserProfile
import dev.morphia.query.filters.Filter
import dev.morphia.query.filters.Filters
import java.time.LocalDateTime

data class UserProfileSpec(
    var firstName: String? = null,
    var lastName: String? = null,
    var otherName: String? = null,
    var status: Status? = null,
    var email: String? = null,
    var role: String? = null,
    var startDate: LocalDateTime? = null,
    var endDate: LocalDateTime? = null,
    var createdAt: LocalDateTime? = null,
    var modifiedAt: LocalDateTime? = null
) : Spec() {

    override fun toParameters(): Map<String, Any?> = mapOf(
        ::firstName.name to firstName,
        ::lastName.name to lastName,
        ::otherName.name to otherName,
        ::email.name to email,
        ::role.name to role,
        ::status.name to status,
        ::startDate.name to startDate,
        ::endDate.name to endDate,
        ::createdAt.name to createdAt,
        ::modifiedAt.name to modifiedAt,
    )


    override fun queryDefinition(param: Map.Entry<String, Any?>): Filter {
        return when (param.key) {
            UserProfileSpec::firstName.name,
            UserProfileSpec::lastName.name,
            UserProfileSpec::otherName.name,
            UserProfileSpec::email.name,
            UserProfileSpec::role.name -> Filters.regex(param.key, param.value.toString())
                .caseInsensitive()

            UserProfileSpec::startDate.name -> Filters.gte(User::createdAt.name, param.value!!)
            UserProfileSpec::endDate.name -> Filters.lte(User::createdAt.name, param.value!!)
            else -> Filters.eq(param.key, param.value)
        }
    }

    companion object {
        fun mapFrom(map: Map<String, Any?>): Spec {
            val page: Int? by map
            val size: Int? by map
            val firstName: String? by map
            val lastName: String? by map
            val otherName: String? by map
            val status: Status? by map
            val email: String? by map
            val role: String? by map
            val startDate: LocalDateTime? by map
            val endDate: LocalDateTime? by map
            val createdAt: LocalDateTime? by map
            val modifiedAt: LocalDateTime? by map

            val spec = UserSpec()
            spec.page = page ?: 1
            spec.size = size ?: 50
            spec.firstName = firstName
            spec.lastName = lastName
            spec.otherName = otherName
            spec.status = status
            spec.email = email
            spec.role = role
            spec.startDate = startDate
            spec.endDate = endDate
            spec.createdAt = createdAt
            spec.modifiedAt = modifiedAt
            return spec
        }
    }


    override fun toString(): String {
        return "UserSpec(" +
                "firstName=$firstName, " +
                "lastName=$lastName, " +
                "otherName=$otherName, " +
                "status=$status, " +
                "email=$email, " +
                "role=$role, " +
                "startDate=$startDate, " +
                "endDate=$endDate, " +
                "createdAt=$createdAt, " +
                "modifiedAt=$modifiedAt, " +

                "sortOrder=$sortOrder, " +
                "sortBy=$sortBy, " +
                "operator=$operator, " +
                "page=$page, " +
                "size=$size" +
                ")"
    }
}