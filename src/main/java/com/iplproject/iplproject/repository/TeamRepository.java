package com.iplproject.iplproject.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iplproject.iplproject.model.Team;

public interface TeamRepository extends CrudRepository<Team, Long> {
    <Optional> Team findByTeamName(String teamName);

    long count();

    List<Team> findAllBy();

    Team save(Team team);
}
