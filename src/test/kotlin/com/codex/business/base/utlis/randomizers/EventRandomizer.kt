package com.codex.business.base.utlis.randomizers

import com.codex.business.components.comment.repo.Comment
import com.codex.business.components.event.enums.EventStatus
import com.codex.business.components.event.repo.Event
import com.codex.business.components.media.repo.Media
import com.codex.business.faker
import com.codex.business.generator
import org.jeasy.random.api.Randomizer
import java.time.LocalDate
import java.util.stream.Collectors

class EventRandomizer : Randomizer<Event> {
    override fun getRandomValue(): Event = Event(
        title = faker.esports().event(),
        description = faker.esports().event(),
        venue = faker.address().streetAddress(),
        reference = faker.random().hex(),
        status = EventStatus.entries.random(),
        media = generator.objects(Media::class.java, 5).collect(Collectors.toList()),
        comments = generator.objects(Comment::class.java, 5).collect(Collectors.toList()),
        startDate = LocalDate.now().plusMonths(1),
        endDate = LocalDate.now().minusYears(20)
    )
}