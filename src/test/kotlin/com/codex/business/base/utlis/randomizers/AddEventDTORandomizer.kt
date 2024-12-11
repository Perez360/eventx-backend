package com.codex.business.base.utlis.randomizers

import com.codex.business.components.eventReaction.dto.AddEventDto
import com.codex.business.faker
import org.jeasy.random.api.Randomizer
import java.time.LocalDate
import java.time.LocalTime

class AddEventDTORandomizer : Randomizer<AddEventDto> {
    override fun getRandomValue(): AddEventDto = AddEventDto(
        title = faker.esports().event(),
        imageLink = mutableListOf(faker.internet().url()),
        venue = faker.address().streetAddress(),
        description = faker.esports().event(),
        reference = faker.random().hex(),
        startTime = LocalTime.now(),
        endTime = LocalTime.now(),
        startDate = LocalDate.now().plusMonths(1),
        endDate = LocalDate.now().minusYears(20)
    )
}