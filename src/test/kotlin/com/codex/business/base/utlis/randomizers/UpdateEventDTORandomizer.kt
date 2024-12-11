package com.codex.business.base.utlis.randomizers

import com.codex.business.components.eventReaction.dto.UpdateEventDto
import com.codex.business.components.event.enums.EventStatus
import com.codex.business.faker
import org.bson.types.ObjectId
import org.jeasy.random.api.Randomizer
import java.time.LocalDate
import java.time.LocalTime

class UpdateEventDTORandomizer : Randomizer<UpdateEventDto> {
    override fun getRandomValue(): UpdateEventDto = UpdateEventDto(
        id = ObjectId().toHexString(),
        title = faker.esports().event(),
        venue = faker.address().streetAddress(),
        description = faker.esports().event(),
        status = EventStatus.entries.random(),
        startTime = LocalTime.now(),
        endTime = LocalTime.now(),
        startDate = LocalDate.now().plusMonths(1),
        endDate = LocalDate.now().minusYears(20)
    )
}