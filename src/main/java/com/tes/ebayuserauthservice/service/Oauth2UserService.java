package com.tes.ebayuserauthservice.service;

import com.tes.ebayuserauthservice.model.AccessToken;
import com.tes.ebayuserauthservice.model.Oauth2User;
import com.tes.ebayuserauthservice.model.RefreshToken;
import java.util.List;
import java.util.Optional;

/**
 * An interface for managing {@link Oauth2User} objects.
 *
 * @param <User> the type of the {@link Oauth2User}.
 * @param <UserId> the type of the id of the {@link Oauth2User}.
 */
public interface Oauth2UserService<User extends Oauth2User, UserId> extends CrudService<User, UserId> {

    /**
     * Generates an {@link AccessToken}.
     *
     * @param userId the id of the {@link User}, which will be used while generating.
     * @return the {@link AccessToken}.
     */
    AccessToken generateAccessToken(UserId userId);

    /**
     * Generates a {@link RefreshToken}.
     *
     * @param userId the id of the {@link User}, which will be used while generating.
     * @return the {@link RefreshToken}.
     */
    RefreshToken generateRefreshToken(UserId userId);

    /**
     * Saves a {@link RefreshToken}.
     *
     * @param userId the id of the {@link User}, which will hold the token.
     * @param optionalRefreshToken an {@link Optional} representation of the {@link RefreshToken}.
     * @return the {@link RefreshToken}.
     */
    RefreshToken saveRefreshToken(
            UserId userId,
            Optional<RefreshToken> optionalRefreshToken
    );

    /**
     * Get a {@link RefreshToken}, which will be checked for validity.
     * If expired, a new one will be generated.
     *
     * @param userId the id of the {@link User}, which will hold the token.
     * @return a valid {@link RefreshToken}.
     */
    RefreshToken getValidRefreshToken(UserId userId);

    /**
     * Get a list of {@link RefreshToken}, saved in an {@link User} object.
     *
     * @param userId the id of the {@link User}, which will hold the token.
     * @return a list of {@link RefreshToken}.
     */
    List<RefreshToken> getRefreshTokens(UserId userId);
}
