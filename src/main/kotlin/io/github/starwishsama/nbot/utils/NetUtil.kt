package io.github.starwishsama.nbot.utils

import cn.hutool.http.HttpException
import cn.hutool.http.HttpRequest
import io.github.starwishsama.nbot.BotConstants
import java.io.InputStream
import java.net.Proxy
import java.net.Socket

object NetUtil {
    fun getUrlInputStream(url: String): InputStream {
        return getUrlInputStream(url, 8000)
    }

    fun getUrlInputStream(url: String, timeout: Int): InputStream {
        val request = doHttpRequest(url, timeout)

        if (BotConstants.cfg.proxyUrl != null && BotConstants.cfg.proxyPort != -1) {
            request.setProxy(Proxy(Proxy.Type.HTTP, Socket(BotConstants.cfg.proxyUrl, BotConstants.cfg.proxyPort).remoteSocketAddress))
        }

        return request.execute().bodyStream()
    }

    @Throws(HttpException::class)
    private fun doHttpRequest(url: String, timeout: Int): HttpRequest {
        return HttpRequest.get(url)
                .setFollowRedirects(true)
                .timeout(timeout)
                .header(
                        "user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36"
                )
    }

    fun getPageContent(url: String): String {
        val response = doHttpRequest(url, 8000).executeAsync()
        return if (response.isOk) response.body() else response.status.toString()
    }
}