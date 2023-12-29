package com.iplproject.iplproject.service;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iplproject.iplproject.model.Match;
import com.iplproject.iplproject.model.Team;
import com.iplproject.iplproject.repository.MatchRepository;
import com.iplproject.iplproject.repository.TeamRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MatchService implements CommandLineRunner {
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private TeamRepository teamRepository;

    @Value("${csv.file.path}") // Assuming you have a property in application.properties for the CSV file path
    private String csvFilePath;

    @Override
    public void run(String... args) throws Exception {
        saveDataFromCSV();
        updateTeamStats();
    }

    public void saveDataFromCSV() throws IOException, URISyntaxException {
        // Path path = Path.of(csvFilePath);
        Path path = Path.of(ClassLoader.getSystemResource("match-data.csv").toURI());

        try (Reader reader = Files.newBufferedReader(path)) {
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(0).build();
            List<String[]> rows = csvReader.readAll();

            for (String[] row : rows) {
                Match match = new Match();
                Long id = row[0] != null ? Long.parseLong(row[0]) : null;
                match.setId(id);
                match.setCity(row[1]);
                match.setDate(LocalDate.parse(row[2]));
                if (row[3].equals("2020/21"))
                    match.setSeason("2020");
                else
                    match.setSeason(row[3]);
                match.setMatchNumber(row[4]);
                String firstInningsTeam, secondInningsTeam;
                if (row[8].equals("bat")) {
                    firstInningsTeam = row[7];
                    secondInningsTeam = (row[7].equals(row[5]) ? row[6]
                            : row[5]);
                } else {
                    secondInningsTeam = row[7];
                    // System.out.println("row[7]: " + row[7] + " row[5]: " + row[5]);
                    firstInningsTeam = (row[7].equals(row[5]) ? row[6]
                            : row[5]);
                    // System.out.println(firstInningsTeam);

                }
                match.setTeam1(firstInningsTeam);
                match.setTeam2(secondInningsTeam);
                match.setTossWinner(row[7]);
                match.setTossDecision(row[8]);
                match.setWinningTeam(row[9]);
                match.setWonBy(row[10]);
                match.setWinMargin(row[11]);
                match.setMethod(row[12]);
                match.setPlayerOfMatch(row[13]);
                match.setUmpire1(row[14]);
                match.setUmpire2(row[15]);
                // Set entity fields based on CSV columns
                // System.out.println(match);
                matchRepository.save(match);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTeamStats() {
        Map<String, Team> teamData = new HashMap<>();

        // Update totalMatches for team1
        matchRepository.countTotalMatchesGroupByTeam1()
                .forEach(e -> teamData.put((String) e[0], new Team((String) e[0], (long) e[1])));

        // Update totalMatches for team2
        matchRepository.countTotalMatchesGroupByTeam2().forEach(e -> {
            Team team = teamData.get((String) e[0]);
            if (team != null) {
                team.setTotalMatches(team.getTotalMatches() + (long) e[1]);
            } else {
                teamData.put((String) e[0], new Team((String) e[0], (long) e[1]));
            }

        });

        // Update totalWins
        matchRepository.countTotalWinsGroupByWinningTeam().forEach(e -> {
            Team team = teamData.get((String) e[0]);
            if (team != null)
                team.setTotalWins((long) e[1]);
        });

        // Persist the teams
        teamRepository.saveAll(teamData.values());

        // Print teams (optional)
        // eamData.values().forEach(team -> System.out.println(team));
    }

}
