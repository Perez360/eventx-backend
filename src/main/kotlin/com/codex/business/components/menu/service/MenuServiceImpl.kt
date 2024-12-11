package com.codex.business.components.menu.service

import com.codex.base.exceptions.ServiceException
import com.codex.base.shared.AppResponse
import com.codex.base.utils.Mapper
import com.codex.business.components.menu.dto.CreateMenuDto
import com.codex.business.components.menu.dto.MenuDto
import com.codex.business.components.menu.dto.UpdateMenuDto
import com.codex.business.components.menu.repo.Menu
import com.codex.business.components.menu.repo.MenuRepo
import com.codex.business.components.user.repo.UserRepo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.util.stream.Collectors


class MenuServiceImpl : MenuService, KoinComponent {
    private val userRepo: UserRepo by inject()
    private val menuRepo: MenuRepo by inject()
    private val logger = LoggerFactory.getLogger(this::class.java)


    override fun ping(): AppResponse<Unit> = AppResponse.Success("The service was reachable successfully", Unit)

    override fun create(dto: CreateMenuDto): AppResponse<MenuDto> {
        val menu = Menu()
        menu.name = dto.name
        menu.description = dto.description
        menu.link = dto.link
        menu.status = dto.status
        menu.icon = dto.icon
        menu.parentId = dto.parentId
        menu.permissions = dto.permissions
        val savedMenu = menuRepo.create(menu)
        logger.info("Menu created successfully {}", savedMenu)
        return AppResponse.Success("Menu created successfully", Mapper.convert(savedMenu))
    }

    override fun update(dto: UpdateMenuDto): AppResponse<MenuDto> {
        val oneMenu = menuRepo.findById(dto.id!!) ?: throw ServiceException("No menu found with id ${dto.id}")
        oneMenu.name = dto.name
        oneMenu.link = dto.link
        oneMenu.parentId = dto.parentId
        oneMenu.status = dto.status
        oneMenu.icon = dto.icon
        oneMenu.permissions = dto.permissions
        oneMenu.modifiedAt = LocalDateTime.now()
        val updatedMenu = menuRepo.update(oneMenu)
        logger.info("Menu updated successfully {}", updatedMenu)
        return AppResponse.Success("Menu updated successfully", Mapper.convert(updatedMenu))
    }

    override fun findById(id: String): AppResponse<MenuDto> {
        val oneMenu = menuRepo.findById(id) ?: throw ServiceException("No menu found with id $id")
        logger.info("Found a menu: {}", oneMenu)
        return AppResponse.Success("Menu fetched successfully", Mapper.convert(oneMenu))
    }


    override fun list(page: Int, size: Int): AppResponse<List<MenuDto>> {
        val menus = menuRepo.list(page, size)
        logger.info("Found a list of menus: $menus")
        val generatedMenus = buildMenuTreeWithPermissions(menus, listOf("admin"))
        logger.info("Listed menu(s) in pages: {}", generatedMenus)
        return AppResponse.Success("Menus fetched successfully", Mapper.convert(generatedMenus))
    }

    override fun deleteById(id: String): AppResponse<MenuDto> {
        val oneMenu = menuRepo.deleteById(id)
            ?: throw ServiceException("No menu found with id $id")
        logger.info("Successfully deleted menu: {}", oneMenu)
        return AppResponse.Success("Menu deleted successfully", Mapper.convert(oneMenu))
    }


    private fun buildMenuTreeWithPermissions(items: List<Menu>, userPermissions: List<String>): List<Menu> {
        val rootItems: List<Menu> = items.stream()
            .filter { item: Menu -> item.parentId == null && hasPermission(item, userPermissions) }
            .collect(Collectors.toList())

        logger.info("Roots: $rootItems")
        for (root in rootItems) {
            logger.info("Root: $root")
            root.menus!!.addAll(findChildrenWithPermissions(root, items, userPermissions))
        }

        return rootItems
    }

    private fun findChildrenWithPermissions(
        parent: Menu,
        items: List<Menu>,
        userPermissions: List<String>
    ): List<Menu> {
        val children: List<Menu> = items.stream()
            .filter { item: Menu -> parent.id == item.parentId && hasPermission(item, userPermissions) }
            .collect(Collectors.toList())

        for (child in children) {
            logger.info("Child: $child")
            child.menus!!.addAll(findChildrenWithPermissions(child, items, userPermissions))
        }

        return children
    }

    private fun hasPermission(item: Menu, userPermissions: List<String>): Boolean {
        return item.permissions!!.stream()
            .anyMatch(userPermissions::contains) // Check if the user has any of the required permissions
    }
}