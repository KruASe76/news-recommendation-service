package org.hsse.news.database.user;

import org.hsse.news.database.user.exceptions.EmailConflictException;
import org.hsse.news.database.user.exceptions.UserNotFoundException;
import org.hsse.news.database.user.models.AuthenticationCredentials;
import org.hsse.news.database.user.models.User;
import org.hsse.news.database.user.models.UserId;
import org.hsse.news.database.user.repositories.UserRepository;
import org.hsse.news.database.util.ComparisonUtil;
import org.hsse.news.database.util.SampleDataUtil;
import org.hsse.news.database.util.TransactionManager;
import org.hsse.news.util.MockitoUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository repositoryMock;

    @Mock
    private TransactionManager transactionManagerMock;

    @InjectMocks
    private UserService service;

    @Test
    void testFindByIdSuccess() {
        assert SampleDataUtil.DEFAULT_USER.id() != null;
        Mockito.when(repositoryMock.findById(SampleDataUtil.DEFAULT_USER.id()))
                .thenReturn(Optional.of(SampleDataUtil.DEFAULT_USER));

        final Optional<User> userOptional = service.findById(SampleDataUtil.DEFAULT_USER.id());

        Mockito.verify(repositoryMock).findById(SampleDataUtil.DEFAULT_USER.id());
        assertTrue(userOptional.isPresent(), "userOptional should be present");
        ComparisonUtil.assertDeepEquals(SampleDataUtil.DEFAULT_USER, userOptional.get());
    }

    @Test
    void testFindByIdFail() {
        assert SampleDataUtil.DEFAULT_USER.id() != null;
        Mockito.when(repositoryMock.findById(SampleDataUtil.DEFAULT_USER.id()))
                .thenReturn(Optional.empty());

        final Optional<User> userOptional = service.findById(SampleDataUtil.DEFAULT_USER.id());

        Mockito.verify(repositoryMock).findById(SampleDataUtil.DEFAULT_USER.id());
        assertTrue(userOptional.isEmpty(), "userOptional should be empty");
    }

    @Test
    void testAuthenticateSuccess() {  // NOPMD
        assert SampleDataUtil.DEFAULT_USER.id() != null;
        final AuthenticationCredentials credentials =
                new AuthenticationCredentials(
                        SampleDataUtil.DEFAULT_USER.email(),
                        SampleDataUtil.DEFAULT_USER.password()
                );
        Mockito.when(repositoryMock.authenticate(credentials))
                .thenReturn(Optional.of(SampleDataUtil.DEFAULT_USER.id()));

        final Optional<UserId> userIdOptional = service.authenticate(credentials);

        Mockito.verify(repositoryMock).authenticate(credentials);
        assertTrue(userIdOptional.isPresent(), "userIdOptional should be present");
        assertEquals(SampleDataUtil.DEFAULT_USER.id(), userIdOptional.get(), "ids should be equal");
    }

    @Test
    void testAuthenticateFail() {
        assert SampleDataUtil.DEFAULT_USER.id() != null;
        final AuthenticationCredentials credentials =
                new AuthenticationCredentials(
                        SampleDataUtil.DEFAULT_USER.email(),
                        SampleDataUtil.DEFAULT_USER.password()
                );
        Mockito.when(repositoryMock.authenticate(credentials))
                .thenReturn(Optional.empty());

        final Optional<UserId> userIdOptional = service.authenticate(credentials);

        Mockito.verify(repositoryMock).authenticate(credentials);
        assertTrue(userIdOptional.isEmpty(), "userIdOptional should be empty");
    }

    @Test
    void testRegisterSuccess() {
        assert SampleDataUtil.DEFAULT_USER.id() != null;
        final User newUser = new User(
                SampleDataUtil.DEFAULT_USER.email(),
                SampleDataUtil.DEFAULT_USER.password(),
                SampleDataUtil.DEFAULT_USER.username()
        );
        Mockito.when(repositoryMock.create(newUser))
                .thenReturn(newUser.initializeWithId(SampleDataUtil.DEFAULT_USER.id()));

        final User registeredUser = service.register(newUser);

        Mockito.verify(repositoryMock).create(newUser);
        ComparisonUtil.assertDeepEquals(SampleDataUtil.DEFAULT_USER, registeredUser);
    }

    @Test
    void testRegisterEmailConflict() {
        assert SampleDataUtil.DEFAULT_USER.id() != null;
        final User newUser = new User(
                SampleDataUtil.DEFAULT_USER.email(),
                SampleDataUtil.DEFAULT_USER.password(),
                SampleDataUtil.DEFAULT_USER.username()
        );
        Mockito.when(repositoryMock.create(newUser))
               .thenThrow(EmailConflictException.class);

        assertThrows(EmailConflictException.class, () -> service.register(newUser));
        Mockito.verify(repositoryMock).create(newUser);
    }

    @Test
    void testUpdateSuccess() {
        assert SampleDataUtil.DEFAULT_USER.id() != null;
        final User userToUpdate = new User(
                SampleDataUtil.DEFAULT_USER.id(),
                SampleDataUtil.NEW_USER.email(),
                SampleDataUtil.DEFAULT_USER.password(),
                SampleDataUtil.NEW_USER.username()
        );
        MockitoUtil.setupUseTransaction(transactionManagerMock);
        Mockito.when(repositoryMock.findById(SampleDataUtil.DEFAULT_USER.id()))
                .thenReturn(Optional.of(SampleDataUtil.DEFAULT_USER));
        Mockito.doNothing()
                .when(repositoryMock).update(userToUpdate);

        service.update(
                SampleDataUtil.DEFAULT_USER.id(),
                SampleDataUtil.NEW_USER.email(),
                SampleDataUtil.NEW_USER.username()
        );

        final ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        Mockito.verify(repositoryMock).update(argument.capture());
        ComparisonUtil.assertDeepEquals(userToUpdate, argument.getValue());
    }

    @Test
    void testUpdateUserNotFound() {
        assert SampleDataUtil.DEFAULT_USER.id() != null;
        MockitoUtil.setupUseTransaction(transactionManagerMock);
        Mockito.when(repositoryMock.findById(SampleDataUtil.DEFAULT_USER.id()))
                        .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> service.update(
                        SampleDataUtil.DEFAULT_USER.id(),
                        SampleDataUtil.NEW_USER.email(),
                        SampleDataUtil.NEW_USER.username()
                )
        );
    }

    @Test
    void testUpdateEmailConflict() {
        assert SampleDataUtil.DEFAULT_USER.id() != null;
        final User userToUpdate = new User(
                SampleDataUtil.DEFAULT_USER.id(),
                SampleDataUtil.NEW_USER.email(),
                SampleDataUtil.DEFAULT_USER.password(),
                SampleDataUtil.NEW_USER.username()
        );
        MockitoUtil.setupUseTransaction(transactionManagerMock);
        Mockito.when(repositoryMock.findById(SampleDataUtil.DEFAULT_USER.id()))
                .thenReturn(Optional.of(SampleDataUtil.DEFAULT_USER));
        Mockito.doThrow(EmailConflictException.class)
                .when(repositoryMock).update(userToUpdate);

        assertThrows(
                EmailConflictException.class,
                () -> service.update(
                        SampleDataUtil.DEFAULT_USER.id(),
                        SampleDataUtil.NEW_USER.email(),
                        SampleDataUtil.NEW_USER.username()
                )
        );
    }
}
