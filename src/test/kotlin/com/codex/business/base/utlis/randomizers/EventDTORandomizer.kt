package com.codex.business.base.utlis.randomizers

import com.codex.business.components.comment.dto.CommentDto
import com.codex.business.components.eventReaction.dto.EventDto
import com.codex.business.components.event.enums.EventStatus
import com.codex.business.components.media.dto.MediaDTO
import com.codex.business.faker
import com.codex.business.generator
import org.bson.types.ObjectId
import org.jeasy.random.api.Randomizer
import java.time.LocalDate
import java.time.LocalTime
import java.util.stream.Collectors

class EventDTORandomizer : Randomizer<EventDto> {
    override fun getRandomValue(): EventDto = EventDto(
        id = ObjectId().toHexString(),
        title = faker.esports().event(),
        venue = faker.address().streetAddress(),
        description = faker.esports().event(),
        comments = generator.objects(CommentDto::class.java, 5).collect(Collectors.toList()),
        media = generator.objects(MediaDTO::class.java, 5).collect(Collectors.toList()),
        reference = faker.random().hex(),
        status = EventStatus.entries.random(),
        startTime = LocalTime.now(),
        endTime = LocalTime.now(),
        startDate = LocalDate.now().plusMonths(1),
        endDate = LocalDate.now().minusYears(20)
    )
}