package com.codex.business.components.auth.service

import com.codex.base.exceptions.ServiceException
import com.codex.base.shared.AppResponse
import com.codex.base.shared.Texts
import com.codex.base.utils.Validator
import com.codex.business.components.auth.dto.AuthDto
import com.codex.business.components.auth.dto.LoginDto
import com.codex.business.components.auth.dto.RegisterDto
import com.codex.business.components.user.dto.CreateUserDto
import com.codex.business.components.user.dto.UserDto
import com.codex.business.components.user.service.UserService
import com.codex.business.components.user.spec.UserSpec
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import java.util.*

class AuthServiceImpl : AuthService, KoinComponent {
    private val userService: UserService by inject()
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun ping(): AppResponse<String> = AppResponse.Success("The service was reachable successfully", null)

    override fun login(dto: LoginDto): AppResponse<AuthDto> {
        logger.info("Received request to login: $dto")

        val spec = when {
            Validator.isValidEmail(dto.identifier!!) -> UserSpec(email = dto.identifier)
            Validator.isValidPhone(dto.identifier) -> UserSpec(phone = dto.identifier)
            else -> {
                logger.info("Invalid email or phone: ${dto.identifier}")
                throw ServiceException("Invalid email or phone")
            }
        }

        val userResponse = userService.search(spec)

        return if (userResponse.code == Texts.CODE_SUCCESS) {
            logger.info("Login was successful")
            val authDto = AuthDto(UUID.randomUUID().toString(), userResponse.data?.data?.first())
            logger.info("Responding to user with: $authDto")
            AppResponse.Success("Login successful", authDto)
        } else {
            AppResponse.Unauthorized("You are unauthorized")
        }
    }


    override fun register(dto: RegisterDto): AppResponse<UserDto> {
        logger.info("Received request to register: $dto")

        val createDto = CreateUserDto()
        createDto.firstName = dto.firstName
        createDto.lastName = dto.lastName
        createDto.otherName = dto.otherName
        createDto.role = dto.role
        when {
            Validator.isValidEmail(dto.identifier!!) -> createDto.email = dto.identifier
            Validator.isValidPhone(dto.identifier) -> createDto.phone = dto.identifier
        }

        val userResponse = userService.create(createDto)

        return if (userResponse.code == Texts.CODE_SUCCESS) {
            logger.info("Successfully registered user")
            AppResponse.Success("You have been registered successfully", null)
        } else {
            logger.info("Sorry!!!. We couldn't register your account.")
            AppResponse.Failure("Sorry. We couldn't register your account")
        }
    }
}
