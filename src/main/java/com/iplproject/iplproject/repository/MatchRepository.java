package com.iplproject.iplproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iplproject.iplproject.model.Match;

public interface MatchRepository extends JpaRepository<Match, Long> {
    @Query("SELECT m.team1, COUNT(*) FROM Match m GROUP BY m.team1")
    List<Object[]> countTotalMatchesGroupByTeam1();

    @Query("SELECT m.team2, COUNT(*) FROM Match m GROUP BY m.team2")
    List<Object[]> countTotalMatchesGroupByTeam2();

    @Query("SELECT m.winningTeam, COUNT(*) FROM Match m GROUP BY m.winningTeam")
    List<Object[]> countTotalWinsGroupByWinningTeam();

    List<Match> getTop4ByTeam1OrTeam2OrderByDateDesc(String teamName1, String teamName2);

    List<Match> getByTeam1AndSeasonOrTeam2AndSeasonOrderByDateDesc(String teamName1, String year, String teamName2,
            String season);
}
