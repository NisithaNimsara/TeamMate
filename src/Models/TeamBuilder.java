package Models;

import java.util.*;

//this class responsible for form teams
public class TeamBuilder {

    private static final int similarGames = 2;
    private static int similarRoles;

    public TeamFormationResult formTeams(List<Participant> allParticipants, int teamSize) {
        if (teamSize > 5) {
            similarRoles = 5;
        }else {
            similarRoles = 3;
        }

        List<Team> teams = new ArrayList<>();
        List<Participant> leftovers = new ArrayList<>();

        //create separate lists based on Personality
        List<Participant> leaders_temp = new ArrayList<>();
        List<Participant> thinkers = new ArrayList<>();
        List<Participant> balanced = new ArrayList<>();

        //Separate based on Personality
        for (Participant p : allParticipants) {
            if (p.getPersonalityType() == PersonalityType.LEADER)
                leaders_temp.add(p);
            else if (p.getPersonalityType() == PersonalityType.THINKER)
                thinkers.add(p);
            else
                balanced.add(p); //(both BALANCED and OTHERS)
        }

        // Shuffle for randomness before we start matching
        Collections.shuffle(leaders_temp);
        Collections.shuffle(thinkers);
        Collections.shuffle(balanced);

        //convert to deque(easy for remove - pop)
        Deque<Participant> leaders = new ArrayDeque<>(leaders_temp);

        // determine Max Possible Teams
        int maxTeamsByLeader = leaders.size(); //
        int maxTeamsByThinker = thinkers.size(); //
        int maxTeamsByTotal = allParticipants.size() / teamSize;

        //final team count is the smallest of these constraints
        int teamCount = Math.min(maxTeamsByLeader, Math.min(maxTeamsByThinker, maxTeamsByTotal));     // 4.5.2

        //initialize every team with 1 Leader                                                         // 4.5.3 --
        for (int i = 1; i <= teamCount; i++) {
            Team t = new Team(i);
            t.addMember(leaders.pop()); //pop from laders
            teams.add(t); // And add to a team
        }
        //all extra leaders go to leftovers
        leftovers.addAll(leaders);

        //assign mandatory 1 thinker to each team
        Iterator<Team> teamIt = teams.iterator(); //create iterator to loop through teams safely
        while (teamIt.hasNext()) {
            Team t = teamIt.next(); //get the next team

            //find a thinker that fits the game and role constraints
            Participant bestThinker = findBestFit(t, thinkers, null);

            if (bestThinker != null) {
                t.addMember(bestThinker);
                thinkers.remove(bestThinker);
            } else {
                //if no thinker fits, the team is invalid, so move members to leftovers
                leftovers.addAll(t.getMembers());
                teamIt.remove(); // Remove the invalid team from the list
            }
        }

        //calculate average skill of remaining players to try and balance teams
        double targetSkill = calculateListsAverage(thinkers, balanced);

        //fill the remaining spots in each team
        for (Team t : teams) {
            // Keep adding members until the team reaches the required size
            while (t.getMembers().size() < teamSize) {
                Participant candidate = null;

                // If no thinker was added, try to add a Balanced player
                if (!balanced.isEmpty()) {
                    candidate = findBestFit(t, balanced, targetSkill); // Find best fit balanced player
                    if (candidate != null)
                        balanced.remove(candidate); // Remove from balanced's list if found
                }

                //Priority: Try to add second Thinker if constraints allow
                if (candidate == null && t.getPersonalityCount(PersonalityType.THINKER) < 2 && !thinkers.isEmpty()) {
                    candidate = findBestFit(t, thinkers, targetSkill); // Find best fit thinker
                    if (candidate != null)
                        thinkers.remove(candidate); // Remove from thinker's list if found
                }

                // If a valid candidate was found, add them to the team
                if (candidate != null) {
                    t.addMember(candidate);
                } else {
                    break;
                }
            }
        }

        //Finally, Remove teams that are not full
        Iterator<Team> finalIt = teams.iterator();
        while (finalIt.hasNext()) {
            Team t = finalIt.next();
            //Check if team size is less than required
            if (t.getMembers().size() < teamSize) {
                leftovers.addAll(t.getMembers()); //move all members to leftovers
                finalIt.remove(); //delete the team
            }
        }                                                                                            //-- 4.5.3

        //add all unused players to leftover's list
        leftovers.addAll(thinkers);                                                                  // 4.5.4 --
        leftovers.addAll(balanced);                                                                  // -- 4.5.4

        // Return the final result, valid teams and leftover participants
        return new TeamFormationResult(teams, leftovers);                                            // 4.5.5
    }

    //------Helper Methods--------

    //Finds the best participant from a list that fits
    private Participant findBestFit(Team team, List<Participant> candidates, Double targetSkill) {

        Participant best = null;
        double minDiff = Double.MAX_VALUE; // Start with a very high difference value

        for (Participant participant : candidates) {
            //check if adding this participant exceeds the MAX_SAME_GAME(2) limit
            if (team.getGameCount(participant.getPreferredGame()) >= similarGames)
                continue;
            //check if adding this participant exceeds the MAX_SAME_ROLE(3) limit
            if (team.getRoleCount(participant.getPreferredRole()) >= similarRoles)
                continue;

            //skill Balance logic
            if (targetSkill != null) {
                //calculate current total skill of the team
                double currentTeamSum = team.getAverageSkill() * team.getMembers().size();
                //calculate what the new average would be if we added this person
                double newAvg = (currentTeamSum + participant.getSkillLevel()) / (team.getMembers().size() + 1);

                //check difference between new average and target average
                double diff = Math.abs(newAvg - targetSkill);
                if (diff < minDiff) {
                    minDiff = diff; //update minimum difference
                    best = participant; //set this person as the best candidate so far
                }
            } else {
                return participant; //return the first valid participant
            }
        }
        return best;
    }

    //calculates the average skill level of two lists combined
    private double calculateListsAverage(List<Participant> list1, List<Participant> list2) {
        double sum = 0;
        int count = 0;
        //sum up skill of list1
        for (Participant p : list1) {
            sum += p.getSkillLevel();
            count++;
        }
        //sum up skill of list2
        for (Participant p : list2) {
            sum += p.getSkillLevel();
            count++;
        }
        //return average (prevent division by zero)
        return count == 0 ? 0 : sum / count;
    }
}