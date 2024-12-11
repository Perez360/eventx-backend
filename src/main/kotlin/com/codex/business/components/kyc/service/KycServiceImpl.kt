package com.codex.business.components.kyc.service

import com.codex.base.exceptions.ServiceException
import com.codex.base.shared.AppResponse
import com.codex.base.shared.PagedContent
import com.codex.business.components.kyc.dto.CreateKycDto
import com.codex.business.components.kyc.dto.KycDto
import com.codex.business.components.kyc.dto.UpdateKycDto
import com.codex.business.components.kyc.repo.Kyc
import com.codex.business.components.kyc.repo.KycRepo
import com.codex.business.components.kyc.spec.KycSpec
import com.codex.business.components.user.enum.VerificationStatus
import com.codex.business.components.user.repo.UserRepo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory

class KycServiceImpl : KycService, KoinComponent {
    private val kycRepo: KycRepo by inject()
    private val userRepo: UserRepo by inject()
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun ping(): AppResponse<Unit> = AppResponse.Success("The service was reachable successfully", Unit)

    override fun create(dto: CreateKycDto): AppResponse<Kyc> {
        logger.info("Received request to create kyc: $dto")

        val user = userRepo.findById(dto.userId!!)
            ?: throw ServiceException("No user found with id: ${dto.userId}")

        if (user.kyc != null) throw ServiceException("Kyc already exist for user")

        val kyc = Kyc()
        kyc.userId = user.id
        kyc.firstName = dto.firstName
        kyc.lastName = dto.lastName
        kyc.otherName = dto.otherName
        kyc.dateOfBirth = dto.dateOfBirth
        kyc.nationality = dto.nationality
        kyc.status = VerificationStatus.UNVERIFIED

        val savedKyc = kycRepo.create(kyc)
        userRepo.update(user.copy(kyc = savedKyc))
        logger.info("Successfully created kyc {}", savedKyc)
        return AppResponse.Success("Kyc created successfully", savedKyc)
    }

    override fun update(dto: UpdateKycDto): AppResponse<Kyc> {
        val oneKyc = kycRepo.findById(dto.id!!)
            ?: throw ServiceException("No kyc found with id ${dto.id}")

        oneKyc.firstName = dto.firstName
        oneKyc.lastName = dto.lastName
        oneKyc.otherName = dto.otherName
        oneKyc.status = dto.status
        oneKyc.nationality = dto.nationality
        val updatedKyc = kycRepo.update(oneKyc)
        logger.info("Successfully updated kyc {}", updatedKyc)
        return AppResponse.Success("Kyc updated successfully", updatedKyc)
    }

    override fun findById(id: String): AppResponse<Kyc> {
        val kyc = kycRepo.findById(id)
            ?: throw ServiceException("No kyc found with id: $id")
        logger.info("Found a kyc: {}", kyc)
        return AppResponse.Success("Kyc fetched successfully", kyc)
    }

    override fun list(page: Int, size: Int): AppResponse<PagedContent<KycDto>> {
        val pagedKycs = kycRepo.list(page, size)
        logger.info("Listed kyc in pages: {}", pagedKycs)
        return AppResponse.Success("Kyc(s) fetched successfully", pagedKycs)
    }

    override fun search(spec: KycSpec): AppResponse<PagedContent<KycDto>> {
        val pagedKycs: PagedContent<KycDto> = kycRepo.search(spec)
        logger.info("Searched kyc in pages: {}", pagedKycs)
        return AppResponse.Success("Kyc(s) fetched successfully", pagedKycs)
    }
}