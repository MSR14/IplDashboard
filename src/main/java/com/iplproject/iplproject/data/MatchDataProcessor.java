package com.iplproject.iplproject.data;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import com.iplproject.iplproject.model.Match;

public class MatchDataProcessor implements ItemProcessor<MatchInput, Match> {
    private static final Logger log = LoggerFactory.getLogger(MatchDataProcessor.class);

    @Override
    public Match process(final MatchInput matchInput) {
        Match match = new Match();
        Long id = matchInput.getId() != null ? Long.parseLong(matchInput.getId()) : null;
        match.setId(id);
        match.setCity(matchInput.getCity());
        match.setDate(LocalDate.parse(matchInput.getDate()));
        match.setSeason(matchInput.getSeason());
        match.setMatchNumber(matchInput.getMatchNumber());
        String firstInningsTeam, secondInningsTeam;
        if (matchInput.getTossDecision() == "bat") {
            firstInningsTeam = matchInput.getTossWinner();
            secondInningsTeam = (matchInput.getTossWinner() == matchInput.getTeam1() ? matchInput.getTeam2()
                    : matchInput.getTeam1());
        } else {
            secondInningsTeam = matchInput.getTossWinner();
            firstInningsTeam = (matchInput.getTossWinner() == matchInput.getTeam1() ? matchInput.getTeam2()
                    : matchInput.getTeam1());
        }
        match.setTeam1(firstInningsTeam);
        match.setTeam2(secondInningsTeam);
        match.setTossWinner(matchInput.getTossWinner());
        match.setTossDecision(matchInput.getTossDecision());
        match.setWinningTeam(matchInput.getWinningTeam());
        match.setWonBy(matchInput.getWonBy());
        match.setWinMargin(matchInput.getWinMargin());
        match.setMethod(matchInput.getMethod());
        match.setPlayerOfMatch(matchInput.getPlayerOfMatch());
        match.setUmpire1(matchInput.getUmpire1());
        match.setUmpire2(matchInput.getUmpire2());
        return match;

    }
}
