package com.codex.business.base.utlis.randomizers

import com.codex.business.faker
import org.jeasy.random.api.Randomizer

class RoleRandomizer : Randomizer<String> {
    override fun getRandomValue(): String = faker.job().position()
}