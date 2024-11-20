package auth;

import java.util.UUID;

public record UserCacheDto(
    UUID userId,
    String role
) { }
