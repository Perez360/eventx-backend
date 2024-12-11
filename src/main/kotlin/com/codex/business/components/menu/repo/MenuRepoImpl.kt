package com.codex.business.components.menu.repo

import dev.morphia.Datastore
import dev.morphia.DeleteOptions
import dev.morphia.InsertOneOptions
import dev.morphia.query.FindOptions
import dev.morphia.query.filters.Filters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MenuRepoImpl : MenuRepo, KoinComponent {
    private val datastore: Datastore by inject()

    override fun create(menu: Menu): Menu = datastore.save(menu)

    override fun update(menu: Menu): Menu = datastore.merge(menu, InsertOneOptions().unsetMissing(false))

    override fun findById(id: String): Menu? = datastore.find(Menu::class.java).filter(Filters.eq("_id", id)).first()

    override fun list(page: Int, size: Int): List<Menu> = datastore.find(Menu::class.java)
        .iterator(FindOptions().skip((page - 1) * size).limit(size)).toList()

    override fun deleteById(id: String): Menu? =
        datastore.find(Menu::class.java).filter(Filters.eq("_id", id)).findAndDelete()

    override fun deleteAll(): Boolean = datastore.find(Menu::class.java)
        .delete(DeleteOptions().multi(true)).wasAcknowledged()
}