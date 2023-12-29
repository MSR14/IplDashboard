import { Link } from "react-router-dom";
import {} from "../App.css";
function MatchSmallCard({ match, teamName }) {
  if (match == null) return null;
  const otherTeam = match.team1 === teamName ? match.team2 : match.team1;
  const otherTeamRoute = `/teams/${otherTeam}`;
  const isMatchWon = teamName === match.winningTeam;
  return (
    <div
      className={
        isMatchWon ? "MatchSmallCard won-card" : "MatchSmallCard lost-card"
      }
    >
      <span className="vs">vs</span>
      <h1>
        <Link to={otherTeamRoute}>{otherTeam}</Link>
      </h1>
      <p className="match-result">
        {match.winningTeam} won by {match.winMargin} {match.wonBy}
      </p>
    </div>
  );
}

export default MatchSmallCard;
