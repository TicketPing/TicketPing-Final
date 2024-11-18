package com.ticketPing.gateway.application.dto;

import java.util.UUID;

public record UserCache(
        UUID userId,
        String role
) { }