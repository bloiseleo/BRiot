package briot.apis.v4;

import briot.RiotHttpClient;
import briot.apis.RiotAPI;
import briot.models.Regions;
import briot.models.ShortRegions;
import com.google.gson.reflect.TypeToken;

import java.util.Collection;
import java.util.List;

public class ChampionMaestry extends RiotAPI {
    record RewardConfigDTO(
            String rewardValue,
            String rewardType,
            int maximumReward
    ) { }
    record NextSeasonMilestonesDTO(
            boolean bonus,
            int rewardMarks,
            RewardConfigDTO rewardConfig
    ) { }
    public record ChampionMaestryDTO(
            String puuid,
            long championPointsUntilNextLevel,
            boolean chestGranted,
            long championId,
            long lastPlayTime,
            int championLevel,
            int championPoints,
            long championPointsSinceLastLevel,
            int markRequiredForNextLevel,
            int championSeasonMilestone,
            int tokensEarned,
            List<String> milestoneGrades,
            NextSeasonMilestonesDTO nextSeasonMilestone
    ) { }
    public ChampionMaestry(RiotHttpClient client, Regions region, ShortRegions shortRegions) {
        super(client, region, shortRegions);
    }
    public Collection<ChampionMaestryDTO> byPuuid(String puuid) {
        var uri = createUriFromRouteAndShortRegion("/lol/champion-mastery/v4/champion-masteries/by-puuid/" + puuid, shortRegion);
        TypeToken<Collection<ChampionMaestryDTO>> collectionTypeToken = new TypeToken<>() {};
        return client.get(uri, collectionTypeToken);
    }
    public ChampionMaestryDTO byPuuidAndChampionId(String puuid, int championId) {
        var uri = createUriFromRouteAndShortRegion(
                String.format("/lol/champion-mastery/v4/champion-masteries/by-puuid/%s/by-champion/%s", puuid, championId), shortRegion
        );
        return client.get(uri, ChampionMaestryDTO.class);
    }
    public Collection<ChampionMaestryDTO> byPuuidTop(String puuid) {
        var uri = createUriFromRouteAndShortRegion("/lol/champion-mastery/v4/champion-masteries/by-puuid/" + puuid + "/top", shortRegion);
        TypeToken<Collection<ChampionMaestryDTO>> collectionTypeToken = new TypeToken<>() {};
        return client.get(uri, collectionTypeToken);
    }
    public Integer byPuuidScore(String puuid) {
        var uri = createUriFromRouteAndShortRegion(String.format("/lol/champion-mastery/v4/scores/by-puuid/%s", puuid), shortRegion);
        return client.get(uri, Integer.class);
    }
}
