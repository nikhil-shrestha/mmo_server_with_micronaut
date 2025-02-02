package server.player.character.service;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.player.character.dto.Character;
import server.player.character.dto.CreateCharacterRequest;
import server.player.character.repository.PlayerCharacterRepository;
import server.util.TestCharacterUtil;

import javax.inject.Inject;
import java.util.Map;

@MicronautTest
public class PlayerCharacterServiceTest {

    // This test will be very similar to
    // PlayerCharacterRepository test as there's limited functionality
    @Inject
    PlayerCharacterService playerCharacterService;

    @Inject
    PlayerCharacterRepository playerCharacterRepository;

    private static final String TEST_USERNAME = "USER";
    private static final String TEST_CHARACTER_NAME = "CHARACTER";

    @BeforeEach
    void cleanDb() {
        playerCharacterRepository.deleteByCharacterName(TEST_CHARACTER_NAME).blockingGet();
    }

    @Test
    void testSaveCharacterAndGetCharacterForUser() {
        // Given
        CreateCharacterRequest createCharacterRequest = new CreateCharacterRequest();
        createCharacterRequest.setName(TEST_CHARACTER_NAME);
        Map<String, String> appearanceInfo = Map.of("key", "value");
        createCharacterRequest.setAppearanceInfo(appearanceInfo);

        Character testCharacter = TestCharacterUtil.getBasicTestCharacter(
                TEST_USERNAME, TEST_CHARACTER_NAME, appearanceInfo);

        // When
        Character character = playerCharacterService.createCharacter(createCharacterRequest, TEST_USERNAME);

        // Then
        Assertions.assertThat(character).usingRecursiveComparison()
                .ignoringFields("updatedAt")
                .isEqualTo(testCharacter);

        Assertions.assertThat(character.getUpdatedAt()).isNotNull();
    }

}
