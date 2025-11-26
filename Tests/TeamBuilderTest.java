import Models.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;


class TeamBuilderTest {

    @Test
    void FormTeamsSuccessfullyTest() {

        List<Participant> participants = new ArrayList<>();
        TeamBuilder builder = new TeamBuilder();

        participants.add(createParticipant("P01", PersonalityType.LEADER, GameType.CHESS, RoleType.STRATEGIST));
        participants.add(createParticipant("P02", PersonalityType.THINKER, GameType.FIFA, RoleType.ATTACKER));
        participants.add(createParticipant("P03", PersonalityType.BALANCED, GameType.CSGO, RoleType.SUPPORTER));

        TeamFormationResult result = builder.formTeams(participants, 3);

        assertEquals(1, result.getTeams().size(), "Should form exactly 1 team");
        // We expect 0 leftovers
        assertEquals(0, result.getLeftovers().size(), "Should have no leftovers");
    }

    @Test
    void testNotEnoughLeaders() {

        List<Participant> participants = new ArrayList<>();
        TeamBuilder builder = new TeamBuilder();

        participants.add(createParticipant("P01", PersonalityType.THINKER, GameType.CHESS, RoleType.STRATEGIST));
        participants.add(createParticipant("P02", PersonalityType.BALANCED, GameType.FIFA, RoleType.ATTACKER));


        TeamFormationResult result = builder.formTeams(participants, 2);


        assertEquals(0, result.getTeams().size());
        assertEquals(2, result.getLeftovers().size());
    }


    private Participant createParticipant(String id, PersonalityType type, GameType game, RoleType role) {
        return new Participant(id, "TestName", "test@test.com", game, 5, role, 80, type);
    }
}