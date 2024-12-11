package com.codex.base.core.di.modules

import com.codex.base.core.settings.CacheManager
import com.codex.business.components.auth.service.AuthService
import com.codex.business.components.auth.service.AuthServiceImpl
import com.codex.business.components.comment.repo.CommentRepo
import com.codex.business.components.comment.repo.CommentRepoImpl
import com.codex.business.components.comment.service.CommentService
import com.codex.business.components.comment.service.CommentServiceImpl
import com.codex.business.components.event.repo.EventRepo
import com.codex.business.components.event.repo.EventRepoImpl
import com.codex.business.components.event.service.EventService
import com.codex.business.components.event.service.EventServiceImpl
import com.codex.business.components.eventCategory.repo.EventCategoryRepo
import com.codex.business.components.eventCategory.repo.EventCategoryRepoImpl
import com.codex.business.components.eventCategory.service.EventCategoryService
import com.codex.business.components.eventCategory.service.EventCategoryServiceImpl
import com.codex.business.components.file.repo.FileRepo
import com.codex.business.components.file.repo.FileRepoImpl
import com.codex.business.components.file.service.FileService
import com.codex.business.components.file.service.FileServiceImpl
import com.codex.business.components.kyc.repo.KycRepo
import com.codex.business.components.kyc.repo.KycRepoImpl
import com.codex.business.components.kyc.service.KycService
import com.codex.business.components.kyc.service.KycServiceImpl
import com.codex.business.components.menu.repo.MenuRepo
import com.codex.business.components.menu.repo.MenuRepoImpl
import com.codex.business.components.menu.service.MenuService
import com.codex.business.components.menu.service.MenuServiceImpl
import com.codex.business.components.user.repo.UserRepo
import com.codex.business.components.user.repo.UserRepoImpl
import com.codex.business.components.user.service.UserService
import com.codex.business.components.user.service.UserServiceImpl
import com.codex.business.integrations.oneSignal.service.OneSignalService
import com.codex.business.integrations.oneSignal.service.OneSignalServiceImpl
import org.koin.dsl.module


//Binding Service Modules
val SERVICE_MODULE = module {
    single<CacheManager<Any>> { CacheManager() }

    single<MenuRepo> { MenuRepoImpl() }
    single<MenuService> { MenuServiceImpl() }

    single<UserRepo> { UserRepoImpl() }
    single<UserService> { UserServiceImpl() }

    single<KycRepo> { KycRepoImpl() }
    single<KycService> { KycServiceImpl() }

    single<EventRepo> { EventRepoImpl() }
    single<EventService> { EventServiceImpl() }

    single<EventCategoryRepo> { EventCategoryRepoImpl() }
    single<EventCategoryService> { EventCategoryServiceImpl() }

    single<CommentRepo> { CommentRepoImpl() }
    single<CommentService> { CommentServiceImpl() }

    single<FileRepo> { FileRepoImpl() }
    single<FileService> { FileServiceImpl() }

    single<AuthService> { AuthServiceImpl() }
    single<OneSignalService> { OneSignalServiceImpl() }
}