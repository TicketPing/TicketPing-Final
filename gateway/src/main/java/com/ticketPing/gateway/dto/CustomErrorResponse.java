package com.ticketPing.gateway.dto;

import org.springframework.http.HttpStatus;

public record CustomErrorResponse(HttpStatus status, String message) {
}
