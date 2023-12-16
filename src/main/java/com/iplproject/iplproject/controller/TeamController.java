package com.iplproject.iplproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.iplproject.iplproject.model.Team;
import com.iplproject.iplproject.repository.TeamRepository;

@RestController
public class TeamController {
    @Autowired
    private TeamRepository teamRepository;

    @GetMapping("/team/{teamName}")
    public long getTeam(@PathVariable String teamName) {
        // return new Team(teamName, 1);
        // return teamRepository.findByTeamName(teamName);
        return teamRepository.count();
    }
}
