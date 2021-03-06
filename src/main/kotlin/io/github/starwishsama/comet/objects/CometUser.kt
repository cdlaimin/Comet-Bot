/*
 * Copyright (c) 2019-2021 StarWishsama.
 *
 * 此源代码的使用受 GNU General Affero Public License v3.0 许可证约束, 欲阅读此许可证, 可在以下链接查看.
 *  Use of this source code is governed by the GNU AGPLv3 license which can be found through the following link.
 *
 * https://github.com/StarWishsama/Comet-Bot/blob/master/LICENSE
 *
 */

package io.github.starwishsama.comet.objects

import com.fasterxml.jackson.annotation.JsonProperty
import io.github.starwishsama.comet.CometVariables
import io.github.starwishsama.comet.enums.UserLevel
import io.github.starwishsama.comet.objects.wrapper.MessageWrapper
import java.time.LocalDateTime

data class CometUser(
    @JsonProperty("userQQ")
    val id: Long,
    var lastCheckInTime: LocalDateTime = LocalDateTime.now().minusDays(1),
    var checkInPoint: Double = 0.0,
    var checkInTime: Int = 0,
    var bindServerAccount: String = "",
    var r6sAccount: String = "",
    var level: UserLevel = UserLevel.USER,
    var checkInGroup: Long = 0,
    var lastExecuteTime: Long = -1,
    private val permissions: MutableList<String> = mutableListOf(),
    val savedContents: MutableList<MessageWrapper> = mutableListOf()
) {
    fun addPoint(point: Number) {
        checkInPoint += point.toDouble()
    }

    fun consumePoint(point: Number) {
        checkInPoint -= point.toDouble()
    }

    fun plusDay() {
        checkInTime++
    }

    fun resetDay() {
        checkInTime = 1
    }

    fun hasPermission(permission: String): Boolean {
        return permissions.contains(permission) || isBotOwner()
    }

    fun getPermissions(): String = buildString {
        this@CometUser.permissions.forEach {
            append("$it ")
        }
    }.trim()

    /**
     * 比较权限组
     * @return 自己的权限组是否大于等于需要比较的权限组
     */
    fun compareLevel(cmdLevel: UserLevel): Boolean {
        return this.level >= cmdLevel
    }

    fun isBotAdmin(): Boolean {
        return level >= UserLevel.ADMIN || isBotOwner()
    }

    fun isBotOwner(): Boolean {
        return level == UserLevel.OWNER
    }

    fun addPermission(permission: String) {
        permissions.plusAssign(permission)
    }

    /**
     * 判断是否签到过了
     *
     * @author StarWishsama
     * @return 是否签到
     */
    fun isChecked(): Boolean {
        val now = LocalDateTime.now()
        val period = lastCheckInTime.toLocalDate().until(now.toLocalDate())

        return period.days == 0
    }

    companion object {
        fun quickRegister(id: Long): CometUser {
            CometVariables.cometUsers[id].apply {
                val register = CometUser(id)
                return this ?: register.also { CometVariables.cometUsers.putIfAbsent(id, register) }
            }
        }

        fun getUser(id: Long): CometUser? {
            return CometVariables.cometUsers[id]
        }

        fun getUserOrRegister(qq: Long): CometUser = getUser(qq) ?: quickRegister(qq)
    }
}