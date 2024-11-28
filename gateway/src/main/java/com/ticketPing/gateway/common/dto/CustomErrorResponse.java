package com.ticketPing.gateway.common.dto;

import org.springframework.http.HttpStatus;

public record CustomErrorResponse(HttpStatus status, String message) {
}
