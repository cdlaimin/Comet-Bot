package io.github.starwishsama.nbot.objects.pojo.bilibili.dynamic.dynamicdata

import com.google.gson.annotations.SerializedName
import io.github.starwishsama.nbot.objects.TextPlusPicture
import io.github.starwishsama.nbot.objects.pojo.bilibili.dynamic.DynamicData
import io.github.starwishsama.nbot.objects.pojo.bilibili.liveroom.LiveInfo

data class LiveRoom(@SerializedName("round_status")
                    val roundStatus: Int,
                    @SerializedName("roomid")
                    val roomID: Long) : LiveInfo(), DynamicData {
    override suspend fun getContact(): TextPlusPicture {
        val wrapped = TextPlusPicture("的直播间\n" +
                "直播间标题:${roomInfo.title}\n" +
                "直播状态: ${roomInfo.getStatus(roundStatus).status}\n" +
                "直达链接: ${roomInfo.getRoomURL()}\n")
        if (roomInfo.coverURL.isNotEmpty()) {
            wrapped.picture = roomInfo.coverURL
        }
        return wrapped
    }
}