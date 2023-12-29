package com.iplproject.iplproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.iplproject.iplproject.model.Match;
import com.iplproject.iplproject.model.Team;
import com.iplproject.iplproject.repository.MatchRepository;
import com.iplproject.iplproject.repository.TeamRepository;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin
public class TeamController {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private MatchRepository matchRepository;

    @GetMapping("/teams")
    public Iterable<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @GetMapping("/teams/{teamName}")
    public Team getTeam(@PathVariable String teamName) {
        Team team = teamRepository.findByTeamName(teamName);
        team.setMatches(matchRepository.getTop4ByTeam1OrTeam2OrderByDateDesc(teamName, teamName));
        return team;
    }

    @GetMapping("/teams/{teamName}/matches")
    public List<Match> getMatchesByYear(@PathVariable String teamName, @RequestParam String year) {
        return matchRepository.getByTeam1AndSeasonOrTeam2AndSeasonOrderByDateDesc(teamName, year, teamName, year);
    }

}
