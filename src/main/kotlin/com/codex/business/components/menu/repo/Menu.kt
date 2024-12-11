package com.codex.business.components.menu.repo

import com.codex.business.common.enums.Status
import dev.morphia.annotations.Entity
import dev.morphia.annotations.EntityListeners
import dev.morphia.annotations.Id
import dev.morphia.annotations.Version
import java.time.LocalDateTime


@Entity
@EntityListeners(MenuEntityListener::class)
data class Menu(
    @Id var id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var link: String? = null,
    var icon: String? = null,
    var parentId: String? = null,
    var permissions: Set<String>? = mutableSetOf(),
    var status: Status? = Status.ENABLED,
    var menus: MutableSet<Menu>? = mutableSetOf(),
    var createdAt: LocalDateTime? = LocalDateTime.now(),
    var modifiedAt: LocalDateTime? = null,

    @Version var version: Long? = null
) {
    override fun toString(): String {
        return "Menu(" +
                "id=$id, " +
                "name=$name, " +
                "description=$description, " +
                "link=$link, " +
                "icon=$icon, " +
                "parentId=$parentId, " +
                "permissions=$permissions, " +
                "status=$status, " +
                "menus=$menus, " +
                "createdAt=$createdAt, " +
                "modifiedAt=$modifiedAt, " +
                "version=$version" +
                ")"
    }
}

