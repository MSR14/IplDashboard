package com.iplproject.iplproject.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.iplproject.iplproject.model.Match;
import com.iplproject.iplproject.model.Team;
import com.iplproject.iplproject.repository.TeamRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    // private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    public JobCompletionNotificationListener(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");
            Map<String, Team> teamData = new HashMap<>();
            List<Object[]> results = entityManager
                    .createQuery("select m.team1, count(*) from Match m group by team1", Object[].class)
                    .getResultList();
            results.stream().map(e -> new Team((String) e[0], (long) e[1]))
                    .forEach(team -> teamData.put(team.getTeamName(), team));
            entityManager.createQuery("select m.team2, count(*) from Match m group by team2", Object[].class)
                    .getResultList()
                    .stream()
                    .forEach(e -> {
                        if (teamData.get((String) e[0]) == null) {
                            teamData.put((String) e[0], new Team((String) e[0], (long) e[1]));
                        } else {
                            Team team2 = teamData.get((String) e[0]);
                            team2.setTotalMatches(team2.getTotalMatches() + (long) e[1]);
                        }

                    });
            entityManager
                    .createQuery("select m.winningTeam, count(*) from Match m group by m.winningTeam", Object[].class)
                    .getResultList()
                    .stream()
                    .forEach(e -> {

                        Team team2 = teamData.get((String) e[0]);
                        if (team2 != null) {
                            team2.setTotalWins((long) e[1]);
                        }

                    });
            teamData.values().forEach(team -> {
                // entityManager.getTransaction().begin();
                // entityManager.persist(team);
                // log.info("Saving team: {}", team);

                // // teamRepository.save(team);
                // log.info("Saved team successfully: {}", team);
                // entityManager.getTransaction().commit();
            });
            // entityManager.persist(new Match());
            // teamData.values().forEach(team -> System.out.println(team.toString()));

            // jdbcTemplate
            // .query("SELECT team1,team2, date FROM match",
            // (rs, row) -> "Team 1 " + rs.getString(1) + "Team 2 " + rs.getString(2) +
            // "Date "
            // + rs.getString(3))
            // .forEach(str -> System.out.println(str));
            // jdbcTemplate
            // .query("SELECT team1, team2, date FROM match", new
            // DataClassRowMapper<>(Match.class))
            // .forEach(match -> log.info("Found <{{}}> in the database.", match));
        }
    }
}
