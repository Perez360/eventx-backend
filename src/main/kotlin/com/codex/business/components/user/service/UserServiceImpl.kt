package com.codex.business.components.user.service

import com.codex.base.exceptions.ServiceException
import com.codex.base.shared.AppResponse
import com.codex.base.shared.PagedContent
import com.codex.base.utils.Mapper
import com.codex.base.utils.PasswordOperations
import com.codex.business.components.kyc.repo.KycRepo
import com.codex.business.components.user.dto.CreateUserDto
import com.codex.business.components.user.dto.UpdateUserDto
import com.codex.business.components.user.dto.UserDto
import com.codex.business.components.user.repo.User
import com.codex.business.components.user.repo.UserRepo
import com.codex.business.components.user.spec.UserSpec
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class UserServiceImpl : UserService, KoinComponent {
    private val userRepo: UserRepo by inject()
    private val kycRepo: KycRepo by inject()
    private val logger = LoggerFactory.getLogger(this::class.java)


    override fun ping(): AppResponse<Unit> = AppResponse.Success("The service was reachable successfully", Unit)

    override fun create(dto: CreateUserDto): AppResponse<UserDto> {
        val user = User()
        user.firstName = dto.firstName
        user.lastName = dto.lastName
        user.otherName = dto.otherName
        user.email = dto.email
        user.password = PasswordOperations.hashPassword(dto.password!!)
        user.role = dto.role
        val savedUser = userRepo.create(user)
        logger.info("User created successfully {}", savedUser)
        return AppResponse.Success("User created successfully", Mapper.convert(savedUser))
    }

    override fun update(dto: UpdateUserDto): AppResponse<UserDto> {
        val oneUser = userRepo.findById(dto.id!!) ?: throw ServiceException("No user found with id ${dto.id}")
//        userRepo.search(UserSpec(email = dto.email)) ?: throw ServiceException("No user found with id ${dto.id}")

        oneUser.firstName = dto.firstName
        oneUser.lastName = dto.lastName
        oneUser.otherName = dto.otherName
        oneUser.role = dto.role
        oneUser.status = dto.status
        oneUser.modifiedAt = LocalDateTime.now()
        val updatedUser = userRepo.update(oneUser)
        logger.info("User updated successfully {}", updatedUser)
        return AppResponse.Success("User updated successfully", Mapper.convert(updatedUser))
    }

    override fun findById(id: String): AppResponse<UserDto> {
        val oneUser = userRepo.findById(id) ?: throw ServiceException("No user found with id $id")
        logger.info("Found a user: {}", oneUser)
        return AppResponse.Success("User fetched successfully", Mapper.convert(oneUser))
    }


    override fun list(page: Int, size: Int): AppResponse<PagedContent<UserDto>> {
        val pagedUsers = userRepo.list(page, size)
        logger.info("Listed user(s) in pages: {}", pagedUsers)
        return AppResponse.Success("Users fetched successfully", pagedUsers)
    }

    override fun search(spec: UserSpec): AppResponse<PagedContent<UserDto>> {
        val pagedUsers: PagedContent<UserDto> = userRepo.search(spec)
        logger.info("Searched user(s) in pages: {}", pagedUsers)
        return AppResponse.Success("User(s) fetched successfully", pagedUsers)
    }


    override fun deleteById(id: String): AppResponse<UserDto> {
        val oneUser = userRepo.deleteById(id)
            ?: throw ServiceException("No user found with id $id")
        oneUser.kyc?.let { kyc -> kycRepo.deleteById(kyc.id!!) }
        logger.info("Successfully deleted user: {}", oneUser)
        return AppResponse.Success("User deleted successfully", Mapper.convert(oneUser))
    }
}