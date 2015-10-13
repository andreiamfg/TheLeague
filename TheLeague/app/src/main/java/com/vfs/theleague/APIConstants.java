package com.vfs.theleague;

// This class constructs all string urls to be used with the api requests
public class APIConstants
{
    private static final String RIOT_API_KEY = "?api_key=561bf79d-e751-40aa-b81b-705fb09516c0";

    private static final String GET_SUMMONER_PREFIX = "https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/";

    private static final String GET_SUMMONER_STATS_PREFIX = "https://na.api.pvp.net/api/lol/na/v1.3/stats/by-summoner/";
    private static final String GET_SUMMONER_STATS_SUFFIX = "/summary?season=SEASON2015&api_key=561bf79d-e751-40aa-b81b-705fb09516c0";

    private static final String GET_RECENT_GAMES_PREFIX = "https://na.api.pvp.net/api/lol/na/v1.3/game/by-summoner/";
    private static final String GET_RECENT_GAMES_SUFFIX = "/recent";

    private static final String GET_CHAMPIMG_PREFIX = "http://ddragon.leagueoflegends.com/cdn/5.7.2/img/champion/";
    private static final String GET_CHAMPION_URL = "http://ddragon.leagueoflegends.com/cdn/5.7.2/data/en_US/champion.json";
    private static final String GET_SUMMONER_ICON = "http://ddragon.leagueoflegends.com/cdn/5.7.2/img/profileicon/";


    public static String getUrlForSummoner (String summonerName)
    {
        return GET_SUMMONER_PREFIX + summonerName + RIOT_API_KEY;
    }

    public static String getUrlForSummonerStats(String summonerId)
    {
        return GET_SUMMONER_STATS_PREFIX + summonerId + GET_SUMMONER_STATS_SUFFIX;
    }

    public static String getUrlForRecentGames(String summonerId)
    {
        return GET_RECENT_GAMES_PREFIX + summonerId + GET_RECENT_GAMES_SUFFIX + RIOT_API_KEY;
    }


    public static String getUrlForChampions()
    {
        return GET_CHAMPION_URL;
    }

    public static String getUrlForChampIMG()
    {
        return GET_CHAMPIMG_PREFIX;
    }

    public static String getUrlForSummIcon()
    {
        return GET_SUMMONER_ICON;
    }
}
