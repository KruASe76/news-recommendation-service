package org.hsse.news.database.user.repositories;

import org.hsse.news.database.user.exceptions.EmailConflictException;
import org.hsse.news.database.user.exceptions.UserNotFoundException;
import org.hsse.news.database.user.models.AuthenticationCredentials;
import org.hsse.news.database.user.models.User;
import org.hsse.news.database.user.models.UserId;
import org.hsse.news.database.util.ComparisonUtil;
import org.hsse.news.database.util.SampleDataUtil;
import org.hsse.news.database.util.TestcontainersUtil;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class JdbiUserRepositoryTest {
    @Container
    private static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16.4-alpine");
    private static Jdbi jdbi;

    private JdbiUserRepository repository;

    @BeforeAll
    static void beforeAll() {
        jdbi = TestcontainersUtil.prepareContainer(POSTGRES);
    }

    @BeforeEach
    void setUp() {
        SampleDataUtil.prepareUsers(jdbi);
        repository = new JdbiUserRepository(jdbi);
    }

    @AfterEach
    void tearDown() {
        SampleDataUtil.clearDatabase(jdbi);
    }

    @Test
    void testFindByIdSuccess() {
        assert SampleDataUtil.DEFAULT_USER.id() != null;
        final Optional<User> userOptional = repository.findById(SampleDataUtil.DEFAULT_USER.id());

        assertTrue(userOptional.isPresent(), "userOptional should be present");
        ComparisonUtil.assertDeepEquals(SampleDataUtil.DEFAULT_USER, userOptional.get());
    }

    @Test
    void testFindByIdFail() {
        assert SampleDataUtil.NEW_USER.id() != null;
        final Optional<User> userOptional = repository.findById(SampleDataUtil.NEW_USER.id());

        assertTrue(userOptional.isEmpty(), "userOptional should be empty");
    }

    @Test
    void testAuthenticateSuccess() { // NOPMD
        final AuthenticationCredentials credentials =
                new AuthenticationCredentials(
                        SampleDataUtil.DEFAULT_USER.email(),
                        SampleDataUtil.DEFAULT_USER.password()
                );
        final Optional<UserId> userIdOptional = repository.authenticate(credentials);

        assertTrue(userIdOptional.isPresent(), "userIdOptional should be present");
        assertEquals(SampleDataUtil.DEFAULT_USER.id(), userIdOptional.get(), "ids should be equal");
    }

    @Test
    void testAuthenticateIncorrectPassword() {
        final AuthenticationCredentials credentials =
                new AuthenticationCredentials(
                        SampleDataUtil.DEFAULT_USER.email(),
                        "wrong_password"
                );
        final Optional<UserId> userIdOptional = repository.authenticate(credentials);

        assertTrue(userIdOptional.isEmpty(), "userIdOptional should be empty");
    }

    @Test
    void testAuthenticateNonExistentEmail() {
        final AuthenticationCredentials credentials =
                new AuthenticationCredentials(
                        "non_existent_email@example.com",
                        SampleDataUtil.DEFAULT_USER.password()
                );
        final Optional<UserId> userIdOptional = repository.authenticate(credentials);

        assertTrue(userIdOptional.isEmpty(), "userIdOptional should be empty");
    }

    @Test
    void testCreateSuccess() {
        final User newUser = new User(
                SampleDataUtil.NEW_USER.email(),
                SampleDataUtil.NEW_USER.password(),
                SampleDataUtil.NEW_USER.username()
        );
        final User createdUser = repository.create(newUser);

        assertNotNull(createdUser.id(), "createdUser should be initialized");
        ComparisonUtil.assertDeepEquals(
                newUser.initializeWithId(createdUser.id()),
                createdUser
        );
    }

    @Test
    void testCreateEmailConflict() {
        final User newUser = new User(
                SampleDataUtil.DEFAULT_USER.email(),
                SampleDataUtil.NEW_USER.password(),
                SampleDataUtil.NEW_USER.username()
        );

        assertThrows(
                EmailConflictException.class,
                () -> repository.create(newUser)
        );
    }

    @Test
    void testUpdateSuccess() {
        final User userToUpdate = new User(
                SampleDataUtil.DEFAULT_USER.id(),
                SampleDataUtil.NEW_USER.email(),
                SampleDataUtil.NEW_USER.password(),
                SampleDataUtil.NEW_USER.username()
        );
        repository.update(userToUpdate);

        assert userToUpdate.id() != null;
        final Optional<User> updatedUserOptional = repository.findById(userToUpdate.id());

        assertTrue(updatedUserOptional.isPresent(), "updatedUserOptional should be present");
        ComparisonUtil.assertDeepEquals(userToUpdate, updatedUserOptional.get());
    }

    @Test
    void testUpdateUserNotFound() {
        assertThrows(
                UserNotFoundException.class,
                () -> repository.update(SampleDataUtil.NEW_USER)
        );
    }

    @Test
    void testUpdateEmailConflict() {
        repository.create(
                new User(
                        SampleDataUtil.NEW_USER.email(),
                        SampleDataUtil.NEW_USER.password(),
                        SampleDataUtil.NEW_USER.username()
                )
        );

        final User userToUpdate = new User(
                SampleDataUtil.DEFAULT_USER.id(),
                SampleDataUtil.NEW_USER.email(),
                SampleDataUtil.NEW_USER.password(),
                SampleDataUtil.NEW_USER.username()
        );

        assertThrows(
                EmailConflictException.class,
                () -> repository.update(userToUpdate)
        );
    }

    @Test
    void testDeleteSuccess() {
        assert SampleDataUtil.DEFAULT_USER.id() != null;
        repository.delete(SampleDataUtil.DEFAULT_USER.id());

        assertTrue(
                repository.findById(SampleDataUtil.DEFAULT_USER.id()).isEmpty(),
                "DEFAULT_USER should not be present"
        );
    }

    @Test
    void testDeleteUserNotFound() { // NOPMD
        assert SampleDataUtil.NEW_USER.id() != null;
        assertThrows(
                UserNotFoundException.class,
                () -> repository.delete(SampleDataUtil.NEW_USER.id())
        );

        assert SampleDataUtil.DEFAULT_USER.id() != null;
        assertTrue(
                repository.findById(SampleDataUtil.DEFAULT_USER.id()).isPresent(),
                "DEFAULT_USER should be present"
        );
    }
}
