package com.codex.business.base.utlis.randomizers

import org.bson.types.ObjectId
import org.jeasy.random.api.Randomizer

class ObjectIdRandomizer : Randomizer<String> {
    override fun getRandomValue(): String = ObjectId().toHexString()
}