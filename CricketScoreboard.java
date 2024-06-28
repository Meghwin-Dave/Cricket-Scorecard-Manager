import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

class Player {
    private String name;
    private int runs;
    private int ballsFaced;
    private int wickets;
    private int ballsBowled;
    
    public Player(String name) {
        this.name = name;
        this.runs = 0;
        this.ballsFaced = 0;
        this.wickets = 0;
        this.ballsBowled = 0;
    }
    
    public String getName() {
        return name;
    }
    
    public int getRuns() {
        return runs;
    }
    
    public void addRuns(int runs) {
        this.runs += runs;
    }
    
    public void faceBall() {
        this.ballsFaced++;
    }
    
    public void takeWicket() {
        this.wickets++;
    }
    
    public void bowlBall() {
        this.ballsBowled++;
    }
}

class Team {
    private String name;
    private ArrayList<Player> players;
    private int totalRuns;
    private int wicketsLost;
    
    public Team(String name) {
        this.name = name;
        this.players = new ArrayList<>();
        this.totalRuns = 0;
        this.wicketsLost = 0;
    }
    
    public String getName() {
        return name;
    }
    
    public void addPlayer(Player player) {
        players.add(player);
    }
    
    public ArrayList<Player> getPlayers() {
        return players;
    }
    
    public int getTotalRuns() {
        return totalRuns;
    }
    
    public void addRuns(int runs) {
        this.totalRuns += runs;
    }
    
    public int getWicketsLost() {
        return wicketsLost;
    }
    
    public void loseWicket() {
        this.wicketsLost++;
    }
}

class Match {
    private Team team1;
    private Team team2;
    private int overs;
    private Team battingTeam;
    private Team bowlingTeam;
    
    public Match(Team team1, Team team2, int overs) {
        this.team1 = team1;
        this.team2 = team2;
        this.overs = overs;
        this.battingTeam = team1;
        this.bowlingTeam = team2;
    }
    
    public Team getTeam1() {
        return team1;
    }
    
    public Team getTeam2() {
        return team2;
    }
    
    public int getOvers() {
        return overs;
    }
    
    public Team getBattingTeam() {
        return battingTeam;
    }
    
    public Team getBowlingTeam() {
        return bowlingTeam;
    }
    
    public void changeInnings() {
        Team temp = battingTeam;
        battingTeam = bowlingTeam;
        bowlingTeam = temp;
    }
}

class Scoreboard {
    private Match match;
    private int currentOver;
    private int currentBall;
    
    public Scoreboard(Match match) {
        this.match = match;
        this.currentOver = 0;
        this.currentBall = 0;
    }
    
    public void addRuns(int runs) {
        match.getBattingTeam().addRuns(runs);
    }
    
    public void loseWicket() {
        match.getBattingTeam().loseWicket();
    }
    
    public void bowlBall() {
        currentBall++;
        if (currentBall == 6) {
            currentOver++;
            currentBall = 0;
        }
    }
    
    public void displayScore() {
        System.out.println("Score: " + match.getBattingTeam().getTotalRuns() + "/" + match.getBattingTeam().getWicketsLost());
        System.out.println("Overs: " + currentOver + "." + currentBall);
    }
    
    // Getters for currentOver and currentBall
    public int getCurrentOver() {
        return currentOver;
    }
    
    public int getCurrentBall() {
        return currentBall;
    }
}

public class CricketScoreboard {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        try {
            System.out.println("Enter name for Team 1: ");
            String team1Name = scanner.nextLine();
            Team team1 = new Team(team1Name);
            
            System.out.println("Enter number of players in Team 1: ");
            int team1Players = scanner.nextInt();
            scanner.nextLine();
            for (int i = 0; i < team1Players; i++) {
                System.out.println("Enter name for player " + (i + 1) + " of Team 1: ");
                String playerName = scanner.nextLine();
                team1.addPlayer(new Player(playerName));
            }
            
            System.out.println("Enter name for Team 2: ");
            String team2Name = scanner.nextLine();
            Team team2 = new Team(team2Name);
            
            System.out.println("Enter number of players in Team 2: ");
            int team2Players = scanner.nextInt();
            scanner.nextLine();
            for (int i = 0; i < team2Players; i++) {
                System.out.println("Enter name for player " + (i + 1) + " of Team 2: ");
                String playerName = scanner.nextLine();
                team2.addPlayer(new Player(playerName));
            }
            
            System.out.println("Enter number of overs for the match: ");
            int overs = scanner.nextInt();
            scanner.nextLine();
            
            Match match = new Match(team1, team2, overs);
            Scoreboard scoreboard = new Scoreboard(match);
            
            System.out.println("First innings starts now.");
            
            boolean firstInnings = true;
            while (firstInnings) {
                System.out.println("Enter runs scored (or -1 to end innings): ");
                int runs = scanner.nextInt();
                if (runs == -1 || (scoreboard.getCurrentOver() == overs && scoreboard.getCurrentBall() == 5)) {
                    firstInnings = false;
                    match.changeInnings();
                    scoreboard = new Scoreboard(match);
                    System.out.println("Innings ended. Changing sides.");
                    break;
                }
                scoreboard.addRuns(runs);
                
                System.out.println("Enter 1 if a wicket fell, 0 otherwise: ");
                int wicket = scanner.nextInt();
                if (wicket == 1) {
                    scoreboard.loseWicket();
                }
                
                scoreboard.bowlBall();
                scoreboard.displayScore();
                
                // Check for innings end due to wickets
                if (match.getBattingTeam().getWicketsLost() >= team1Players) {
                    firstInnings = false;
                    match.changeInnings();
                    scoreboard = new Scoreboard(match);
                    System.out.println("Innings ended due to all out. Changing sides.");
                    break;
                }
            }
            
            System.out.println("Target for the second innings: " + (team1.getTotalRuns() + 1));

            System.out.println("Second innings starts now.");

            boolean secondInnings = true;
            while (secondInnings) {
                System.out.println("Enter runs scored (or -1 to end innings): ");
                int runs = scanner.nextInt();
                if (runs == -1 || (team2.getTotalRuns() >= team1.getTotalRuns() + 1)) {
                    secondInnings = false;
                    break;
                }
                scoreboard.addRuns(runs);
                
                System.out.println("Enter 1 if a wicket fell, 0 otherwise: ");
                int wicket = scanner.nextInt();
                if (wicket == 1) {
                    scoreboard.loseWicket();
                }
                
                scoreboard.bowlBall();
                scoreboard.displayScore();
                
                // Check for innings end due to target chased
                if (team2.getTotalRuns() >= team1.getTotalRuns() + 1) {
                    secondInnings = false;
                    break;
                }
            }
            
            System.out.println("Match ended.");
            System.out.println("Final Score:");
            System.out.println(team1.getName() + ": " + team1.getTotalRuns() + "/" + team1.getWicketsLost());
            System.out.println(team2.getName() + ": " + team2.getTotalRuns() + "/" + team2.getWicketsLost());
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter the correct data type.");
        } finally {
            scanner.close();
        }
    }
}
