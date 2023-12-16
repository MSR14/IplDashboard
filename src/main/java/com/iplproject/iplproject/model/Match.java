package com.iplproject.iplproject.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Match {
    @Id
    private long id;
    private String city;
    private LocalDate date;
    private String season;
    private String matchNumber;
    private String team1;
    private String team2;
    private String tossWinner;
    private String tossDecision;
    private String winningTeam;
    private String wonBy;
    private String winMargin;
    private String method;
    private String playerOfMatch;
    private String umpire1;
    private String umpire2;
}
