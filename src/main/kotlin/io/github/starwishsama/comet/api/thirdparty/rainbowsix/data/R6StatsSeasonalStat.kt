package io.github.starwishsama.comet.api.thirdparty.rainbowsix.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.starwishsama.comet.BotVariables.mapper
import io.github.starwishsama.comet.enums.R6Rank

data class R6StatsSeasonalStat(
    @JsonProperty("username")
    val userName: String,
    @JsonProperty("platform")
    val platform: String,
    @JsonProperty("ubisoft_id")
    val ubisoftId: String,
    @JsonProperty("uplay_id")
    val uplayId: String,
    @JsonProperty("avatar_url_146")
    val smallAvatar: String,
    @JsonProperty("avatar_url_256")
    val avatar: String,
    /**
     * 格式 "2021-02-17T12:47:24.000Z"
     */
    @JsonProperty("last_updated")
    val lastUpdateTime: String,
    @JsonProperty("seasons")
    val seasonalStat: JsonNode
) {
    fun getSeasonalStat(season: SeasonName): SeasonInfo? {
        val seasonInfo = seasonalStat[season.season]
        return if (seasonInfo.isNull || seasonInfo.isEmpty) {
            null
        } else {
            mapper.readValue(seasonInfo.traverse())
        }
    }

    data class SeasonInfo(
        val name: String,
        @JsonProperty("start_date")
        val startDate: String,
        @JsonProperty("end_date")
        val endDate: String?,
        @JsonProperty("regions")
        val regions: JsonNode
    ) {
        fun getRegionStat(region: Region): PerRegionStat? {
            val regionStat = regions[region.region]
            return if (regionStat.isNull) {
                null
            } else {
                mapper.readValue(regionStat.traverse())
            }
        }

        data class PerRegionStat(
            @JsonProperty("season_id")
            val seasonId: Int,
            @JsonProperty("region")
            val regionName: String,
            @JsonProperty("abandons")
            val abandons: Int,
            @JsonProperty("losses")
            val losses: Int,
            @JsonProperty("max_mmr")
            val maxMMR: Long,
            @JsonProperty("max_rank")
            val maxRank: Int,
            @JsonProperty("mmr")
            val currentMMR: Long,
            @JsonProperty("rank")
            val currentRank: Int,
            @JsonProperty("wins")
            val wins: Long,
            @JsonProperty("kills")
            val kills: Long,
            @JsonProperty("deaths")
            val deaths: Long,
            @JsonProperty("last_match_mmr_change")
            val lastMatchMMRChange: Long,
            @JsonProperty("champions_rank_position")
            val championPosition: Int,
            @JsonProperty("rank_image")
            val rankImage: String,
            @JsonProperty("max_rank_image")
            val maxRankImage: String
        ) {
            fun getRank(): R6Rank = R6Rank.getRank(currentRank)
        }
    }
}

enum class SeasonName(val season: String) {
    NEON_DAWN("neon_dawn"),

    SHADOW_LEGACY("shadow_legacy"),

    STEEL_WAVE("steel_wave"),

    VOID_EDGE("void_edge"),

    SHIFTING_TIDES("shifting_tides"),

    EMBER_RISE("ember_rise")
}

enum class Region(val region: String) {
    /**
     * 亚太
     */
    NCSA("ncsa"),
    EMEA("emea"),
    APAC("apac")
}