package com.iplproject.iplproject.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchInput {
    private String id;
    private String city;
    private String date;
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
