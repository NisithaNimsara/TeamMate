package Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// This class forms balanced teams
public class TeamBuilder {

    private static final int MAX_SAME_GAME_PER_TEAM = 2; // max 2 players preferring same game
    private static final int MAX_SAME_ROLE_PER_TEAM = 3; // avoid too many same roles
    private static final int MAX_LEADERS_PER_TEAM = 1;   // only 1 leader per team
    private static final int MAX_THINKERS_PER_TEAM = 2;  // max 2 thinkers per team


    //MAIN method
    public List<Team> formTeams(List<Participant> members, int teamSize) {

        //team size validation
        if (teamSize <=0){
            throw new IllegalArgumentException("Team size must be positive");
        }

        //get a copy of input list(avoid modify original)
        List<Participant> all = new ArrayList<>(members);
        if (all.isEmpty()){
            return new ArrayList<>();
        }

        // Calculate global average skill to help balance team strengths
        double globalAverageSkill = all.stream().mapToInt(Participant::getSkillLevel).average().orElse(0.0);


        //split personality groups------------------------------------------------------------
        List<Participant> leaders = new ArrayList<>();
        List<Participant> thinkers = new ArrayList<>();
        List<Participant> balanced = new ArrayList<>();

        for (Participant p : all){
            if (p.getPersonalityType() == PersonalityType.LEADER){
                leaders.add(p);
            } else if (p.getPersonalityType() == PersonalityType.THINKER) {
                thinkers.add(p);
            } else {
                balanced.add(p);
            }
        }

        //thins meke the placement random
        Collections.shuffle(leaders);
        Collections.shuffle(thinkers);
        Collections.shuffle(balanced);

        // Create the required number of empty teams
        int teamCount = (int) Math.ceil(all.size()/ (double)teamSize);
        List<Team> teams = new ArrayList<>();
        for (int i = 1; i <= teamCount; i++){
            teams.add(new Team(i));
        }

        // Phase 1 - Place leaders first----------
        for (Participant leader : leaders){

            // Try with full caps
            Team chosen = chooseTeam(leader,teams,teamSize,globalAverageSkill,true,true);

            // If cannot place respecting all caps, relax personality rule
            if (chosen == null){
                chosen = chooseTeam(leader,teams,teamSize,globalAverageSkill,true,false);
            }

            if (chosen != null){
                chosen.addMember(leader);
            }
        }

        // Phase 2 - Ensure at least one THINKER in every team--------
        for (Team team : teams){

            // If no thinkers left, stop
            if (thinkers.isEmpty()) break;

            // Check if team already has a thinker
            boolean hasThinker = team.getMembers().stream().allMatch(p -> p.getPersonalityType() == PersonalityType.THINKER);

            if (!hasThinker){

                Participant selected = null;

                // try thinkers respecting all caps
                for (Participant thinker : thinkers){
                    if (respectsGameAndRoleCaps(team, thinker) && respectsPersonalityCaps(team, thinker)){
                        selected = thinker;
                        break;
                    }
                }

                // if none fits personality caps,
                if (selected == null){
                    for (Participant thinker : thinkers){
                        if (respectsGameAndRoleCaps(team, thinker)) {
                            selected = thinker;
                            break;
                        }
                    }
                }

                // add thinker if not found ans space exist
                if (selected != null && team.getMembers().size() < teamSize){
                    team.addMember(selected);
                    thinkers.remove(selected);
                }
            }
        }

        //Phase 3 - place remaining thinkers
        for (Participant thinker : new ArrayList<>(thinkers)) {

            Team chosen =  chooseTeam(thinker,teams,teamSize,globalAverageSkill,true,true);

            //if no perfect team found
            if (chosen == null){
                chosen =  chooseTeam(thinker,teams,teamSize,globalAverageSkill,true,false);
            }

            if (chosen != null){
                chosen.addMember(thinker);
                thinkers.remove(thinker);
            }
        }

        //Phase 4 - place balanced participants
        List<Participant> remaing = new ArrayList<>();
        remaing.addAll(thinkers);   // any leftover thinkers
        remaing.addAll(balanced);   // add all balanced players

        for (Participant p : remaing){

            // First try respecting role, but not strict personality
            Team chosen = chooseTeam(p,teams,teamSize,globalAverageSkill,true,false);

            // If still no place, final attempt ignoring all except size
            if (chosen == null){
                chosen = chooseTeam(p,teams,teamSize,globalAverageSkill,false,false);
            }

            if (chosen != null){
                chosen.addMember(p);
            }
        }
        return teams;
    }


    //-----------------------------------------------------------------------------------------------
    // method for pick best team for a participant
    private Team chooseTeam(Participant p,
                            List<Team> teams,
                            int teamSize,
                            double globalAverageSkill,
                            boolean enforceGameRoleCaps,
                            boolean enforcePersonalityCaps) {

        Team beat = null;
        double bestScore = Double.MAX_VALUE;

        for (Team t : teams){
            //Skip full teams
            if (t.getMembers().size() >= teamSize) continue;

            // Skip if game/role rules broken
            if (enforceGameRoleCaps && !respectsGameAndRoleCaps(t, p)) continue;

            // Skip if personality caps broken
            if (enforcePersonalityCaps && !respectsPersonalityCaps(t, p)) continue;

            //simple score method
            int size = t.getMembers().size();
            double avgSkill = t.getAverageSkill();
            double skillDifference = Math.abs(avgSkill - globalAverageSkill);

            double score = size * 10.0 + skillDifference;

            if (score < bestScore){
                bestScore = score;
                beat = t;
            }
        }

        return beat;
    }

    // Check game and role caps
    private boolean respectsGameAndRoleCaps(Team t, Participant p) {

        if (t.getGameCount(p.getPreferredGame()) >= MAX_SAME_GAME_PER_TEAM) return false;
        if (t.getRoleCount(p.getPreferredRole()) >= MAX_SAME_ROLE_PER_TEAM) return false;

        return true;
    }


    // Check personality caps
    private boolean respectsPersonalityCaps(Team t, Participant p) {
        PersonalityType type = p.getPersonalityType();

        if (type == PersonalityType.LEADER &&
                t.getPersonalityCount(PersonalityType.LEADER) >= MAX_LEADERS_PER_TEAM)
            return false;

        if (type == PersonalityType.THINKER &&
                t.getPersonalityCount(PersonalityType.THINKER) >= MAX_THINKERS_PER_TEAM)
            return false;

        // BALANCED has no cap
        return true;
    }
}
