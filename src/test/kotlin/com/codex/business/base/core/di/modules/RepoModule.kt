package com.codex.business.base.core.di.modules

import com.codex.business.components.activityLog.repo.ActivityLogRepo
import com.codex.business.components.activityLog.repo.ActivityLogRepoImpl
import com.codex.business.components.comment.repo.CommentRepo
import com.codex.business.components.comment.repo.CommentRepoImpl
import com.codex.business.components.contact.repo.ContactRepo
import com.codex.business.components.contact.repo.ContactRepoImpl
import com.codex.business.components.document.repo.DocumentRepo
import com.codex.business.components.document.repo.DocumentRepoImpl
import com.codex.business.components.event.repo.EventRepo
import com.codex.business.components.event.repo.EventRepoImpl
import com.codex.business.components.kyc.repo.KycRepo
import com.codex.business.components.kyc.repo.KycRepoImpl
import com.codex.business.components.media.repo.MediaRepo
import com.codex.business.components.media.repo.MediaRepoImpl
import com.codex.business.components.user.repo.UserRepo
import com.codex.business.components.user.repo.UserRepoImpl
import org.koin.dsl.module

val repoModule = module {
    /*Binding and Scoping Repositories*/
    single<UserRepo> { UserRepoImpl() }
    single<ContactRepo> { ContactRepoImpl() }
    single<EventRepo> { EventRepoImpl() }
    single<KycRepo> { KycRepoImpl() }
    single<MediaRepo> { MediaRepoImpl() }
    single<CommentRepo> { CommentRepoImpl() }
    single<DocumentRepo> { DocumentRepoImpl() }
    single<ActivityLogRepo> { ActivityLogRepoImpl() }
}