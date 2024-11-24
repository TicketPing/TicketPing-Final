package com.ticketPing.gateway.dto;

import java.util.UUID;

public record UserCache(
        UUID userId,
        String role
) { }