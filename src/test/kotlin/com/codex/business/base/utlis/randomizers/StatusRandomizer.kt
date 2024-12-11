package com.codex.business.base.utlis.randomizers

import com.codex.business.common.enums.Status
import org.jeasy.random.api.Randomizer

class StatusRandomizer : Randomizer<Status> {
    override fun getRandomValue(): Status = Status.entries.random()
}