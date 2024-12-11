package com.codex.business.components.menu.repo

interface MenuRepo {
    fun create(menu: Menu): Menu

    fun update(menu: Menu): Menu

    fun findById(id: String): Menu?

    fun list(page: Int = 1, size: Int = 50): List<Menu>

    fun deleteById(id: String): Menu?

    fun deleteAll(): Boolean
}