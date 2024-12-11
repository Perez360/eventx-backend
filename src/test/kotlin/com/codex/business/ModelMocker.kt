package com.codex.business

import com.codex.base.utils.annotations.*
import com.codex.business.base.utlis.randomizers.*
import com.codex.business.components.activityLog.dto.ActivityLogDTO
import com.codex.business.components.activityLog.dto.AddActivityLogDTO
import com.codex.business.components.activityLog.repo.ActivityLog
import com.codex.business.components.comment.dto.AddCommentDto
import com.codex.business.components.comment.dto.CommentDto
import com.codex.business.components.comment.dto.UpdateCommentDTO
import com.codex.business.components.comment.repo.Comment
import com.codex.business.components.contact.dto.AddContactDTO
import com.codex.business.components.contact.dto.ContactDTO
import com.codex.business.components.contact.dto.UpdateContactDTO
import com.codex.business.components.contact.repo.Contact
import com.codex.business.components.document.dto.AddDocumentDTO
import com.codex.business.components.document.dto.DocumentDto
import com.codex.business.components.document.dto.UpdateDocumentDTO
import com.codex.business.components.document.repo.Document
import com.codex.business.components.eventReaction.dto.AddEventDto
import com.codex.business.components.eventReaction.dto.EventDto
import com.codex.business.components.eventReaction.dto.UpdateEventDto
import com.codex.business.components.event.repo.Event
import com.codex.business.components.kyc.dto.CreateKycDto
import com.codex.business.components.kyc.dto.KycDto
import com.codex.business.components.kyc.dto.UpdateKycDto
import com.codex.business.components.kyc.repo.Kyc
import com.codex.business.components.media.dto.AddMediaDTO
import com.codex.business.components.media.dto.MediaDTO
import com.codex.business.components.media.dto.UpdateMediaDTO
import com.codex.business.components.media.repo.Media
import com.codex.business.components.userProfile.dto.AddUserDto
import com.codex.business.components.userProfile.dto.UpdateUserDto
import com.codex.business.components.userProfile.dto.UserDto
import com.codex.business.components.user.repo.User
import com.github.javafaker.Faker
import dev.morphia.annotations.Id
import dev.morphia.annotations.Version
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.FieldPredicates
import java.util.*


val faker: Faker = Faker.instance(Locale.ENGLISH)
private val parameters =
    EasyRandomParameters().charset(Charsets.UTF_8).collectionSizeRange(1, 10).stringLengthRange(5, 32)
        .scanClasspathForConcreteTypes(true)
        .randomize(FieldPredicates.isAnnotatedWith(FirstName::class.java), FirstNameRandomizer())
        .randomize(FieldPredicates.isAnnotatedWith(LastName::class.java), LastNameRandomizer())
        .randomize(FieldPredicates.isAnnotatedWith(OtherName::class.java), OtherNameRandomizer())
        .randomize(FieldPredicates.isAnnotatedWith(Status::class.java), StatusRandomizer())
        .randomize(FieldPredicates.isAnnotatedWith(Role::class.java), RoleRandomizer())
        .randomize(FieldPredicates.isAnnotatedWith(Password::class.java), PasswordRandomizer())
        .randomize(FieldPredicates.isAnnotatedWith(Id::class.java), ObjectIdRandomizer())
        .excludeField(FieldPredicates.isAnnotatedWith(Version::class.java))
        .randomizationDepth(100)

val generator = EasyRandom(parameters)

/************************************ CONTACT *************************************/
fun mockedContact(): Contact = generator.nextObject(Contact::class.java)
fun mockedContactDTO(): ContactDTO = generator.nextObject(ContactDTO::class.java)
fun mockedAddContactDTO(): AddContactDTO = generator.nextObject(AddContactDTO::class.java)
fun mockedUpdateContactDTO(): UpdateContactDTO = generator.nextObject(UpdateContactDTO::class.java)

/************************************ USER *************************************/
fun mockedUser(): User = generator.nextObject(User::class.java)
fun mockedAddUserDto(): AddUserDto = generator.nextObject(AddUserDto::class.java)
fun mockedUpdateUserDto(): UpdateUserDto = generator.nextObject(UpdateUserDto::class.java)
fun mockedUserDto(): UserDto = generator.nextObject(UserDto::class.java)

/************************************ KYC *************************************/
fun mockedKyc(): Kyc = generator.nextObject(Kyc::class.java)
fun mockedAddKycDTO(): CreateKycDto = generator.nextObject(CreateKycDto::class.java)
fun mockedUpdateKycDTO(): UpdateKycDto = generator.nextObject(UpdateKycDto::class.java)
fun mockedKycDTO(): KycDto = generator.nextObject(KycDto::class.java)

/************************************ EVENT *************************************/
fun mockedEvent(): Event = generator.nextObject(Event::class.java)
fun mockedAddEventDTO(): AddEventDto = generator.nextObject(AddEventDto::class.java)
fun mockedUpdateEventDTO(): UpdateEventDto = generator.nextObject(UpdateEventDto::class.java)
fun mockedEventDTO(): EventDto = generator.nextObject(EventDto::class.java)

/************************************ COMMENT *************************************/
fun mockedComment(): Comment = generator.nextObject(Comment::class.java)
fun mockedAddCommentDTO(): AddCommentDto = generator.nextObject(AddCommentDto::class.java)
fun mockedUpdateCommentDTO(): UpdateCommentDTO = generator.nextObject(UpdateCommentDTO::class.java)
fun mockedCommentDTO(): CommentDto = generator.nextObject(CommentDto::class.java)

/************************************ MEDIA *************************************/
fun mockedMedia(): Media = generator.nextObject(Media::class.java)
fun mockedAddMediaDTO(): AddMediaDTO = generator.nextObject(AddMediaDTO::class.java)
fun mockedUpdateMediaDTO(): UpdateMediaDTO = generator.nextObject(UpdateMediaDTO::class.java)
fun mockedMediaDTO(): MediaDTO = generator.nextObject(MediaDTO::class.java)

/************************************ DOCUMENT *************************************/
fun mockedDocument(): Document = generator.nextObject(Document::class.java)
fun mockedAddDocumentDTO(): AddDocumentDTO = generator.nextObject(AddDocumentDTO::class.java)
fun mockedUpdateDocumentDTO(): UpdateDocumentDTO = generator.nextObject(UpdateDocumentDTO::class.java)
fun mockedDocumentDTO(): DocumentDto = generator.nextObject(DocumentDto::class.java)

/************************************ ACTIVITY LOGS *************************************/
fun mockedActivity(): ActivityLog = generator.nextObject(ActivityLog::class.java)
fun mockActivityDTO(): ActivityLogDTO = generator.nextObject(ActivityLogDTO::class.java)
fun mockAddActivityDTO(): AddActivityLogDTO = generator.nextObject(AddActivityLogDTO::class.java)
