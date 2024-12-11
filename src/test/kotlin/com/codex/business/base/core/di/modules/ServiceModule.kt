package com.codex.business.base.core.di.modules

import com.codex.business.components.comment.controller.CommentController
import com.codex.business.components.comment.controller.CommentControllerImpl
import com.codex.business.components.comment.service.CommentService
import com.codex.business.components.comment.service.CommentServiceImpl
import com.codex.business.components.contact.controller.ContactController
import com.codex.business.components.contact.controller.ContactControllerImpl
import com.codex.business.components.contact.service.ContactService
import com.codex.business.components.contact.service.ContactServiceImpl
import com.codex.business.components.document.controller.DocumentController
import com.codex.business.components.document.controller.DocumentControllerImpl
import com.codex.business.components.document.service.DocumentService
import com.codex.business.components.document.service.DocumentServiceImpl
import com.codex.business.components.event.service.EventService
import com.codex.business.components.event.service.EventServiceImpl
import com.codex.business.components.kyc.controller.KycController
import com.codex.business.components.kyc.controller.KycControllerImpl
import com.codex.business.components.kyc.service.KycService
import com.codex.business.components.kyc.service.KycServiceImpl
import com.codex.business.components.media.controller.MediaController
import com.codex.business.components.media.controller.MediaControllerImpl
import com.codex.business.components.media.service.MediaService
import com.codex.business.components.media.service.MediaServiceImpl
import com.codex.business.components.user.service.UserService
import com.codex.business.components.user.service.UserServiceImpl
import org.koin.dsl.module

val serviceModule = module {
    /*Binding and Scoping Services*/
    single<UserService> { UserServiceImpl() }
    single<ContactService> { ContactServiceImpl() }
    single<KycService> { KycServiceImpl() }
    single<DocumentService> { DocumentServiceImpl() }
    single<MediaService> { MediaServiceImpl() }
    single<EventService> { EventServiceImpl() }
    single<CommentService> { CommentServiceImpl() }

    /*Binding and Scoping Controllers*/
    single<ContactController> { ContactControllerImpl() }
    single<KycController> { KycControllerImpl() }
    single<DocumentController> { DocumentControllerImpl() }
    single<MediaController> { MediaControllerImpl() }
    single<CommentController> { CommentControllerImpl() }
}