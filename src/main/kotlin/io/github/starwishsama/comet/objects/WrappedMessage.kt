package io.github.starwishsama.comet.objects

import io.github.starwishsama.comet.utils.NetUtil
import io.github.starwishsama.comet.utils.toMirai
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.EmptyMessageChain
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.uploadAsImage

data class WrappedMessage(var text: String?) {
    var picture: String? = null

    suspend fun getPicture(contact: Contact): Image? {
        if (picture != null) {
            return picture?.let { NetUtil.getUrlInputStream(it).uploadAsImage(contact) }
        }
        return null
    }

    fun plusImageUrl(url: String): WrappedMessage {
        this.picture = url
        return this
    }

    suspend fun toMessageChain(contact: Contact): MessageChain {
        val sText = text
        if (sText != null) {
            val image = getPicture(contact)
            if (image != null) {
                return sText.toMirai() + image
            }
            return sText.toMirai()
        }
        return EmptyMessageChain
    }
}