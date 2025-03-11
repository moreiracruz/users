package br.com.pawloandre.users.exception.handler;

import java.time.LocalDateTime;

public record ResponseError(LocalDateTime timestamp, String status, int statusCode, String error) {

}
