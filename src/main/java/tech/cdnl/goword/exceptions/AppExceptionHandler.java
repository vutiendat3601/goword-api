package tech.cdnl.goword.exceptions;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import tech.cdnl.goword.shared.models.ApiResponseStatus;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
		log.error("[ %s ] %s".formatted(RuntimeException.class.getSimpleName(), e.getMessage()));
		ErrorResponse errResp = new ErrorResponse(
				ApiResponseStatus.ERROR,
				AppErrorMessage.SERVER_ERROR,
				AppErrorCode.SERVER_ERROR,
				ZonedDateTime.now().toEpochSecond());
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(errResp);
	}

	@ExceptionHandler(AuthException.class)
	public ResponseEntity<ErrorResponse> handleBadCredentialsException(AuthException e) {
		log.error("[ %s ] %s".formatted(RuntimeException.class.getSimpleName(), e.getMessage()));
		ErrorResponse errResp = new ErrorResponse(
				ApiResponseStatus.ERROR,
				e.getMessage(),
				e.getErrorCode(),
				ZonedDateTime.now().toEpochSecond());
		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(errResp);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
		log.error("[ %s ] %s".formatted(RuntimeException.class.getSimpleName(), e.getMessage()));
		ErrorResponse errResp = new ErrorResponse(
				ApiResponseStatus.ERROR,
				AppErrorMessage.ACCESS_FORBIDDEN,
				AppErrorCode.ACCESS_FORBIDDEN,
				ZonedDateTime.now().toEpochSecond());
		return ResponseEntity
				.status(HttpStatus.FORBIDDEN)
				.body(errResp);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
		log.error("[ %s ] %s".formatted(ResourceNotFoundException.class.getSimpleName(), e.getMessage()));
		ErrorResponse errResp = new ErrorResponse(
				ApiResponseStatus.ERROR,
				e.getMessage(),
				e.getCode(),
				ZonedDateTime.now().toEpochSecond());
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(errResp);
	}

	@ExceptionHandler(ResourceConflictException.class)
	public ResponseEntity<ErrorResponse> handleResourceDuplicationException(ResourceConflictException e) {
		log.error("[ %s ] %s".formatted(ResourceConflictException.class.getSimpleName(), e.getMessage()));
		ErrorResponse errResp = new ErrorResponse(
				ApiResponseStatus.ERROR,
				e.getMessage(),
				e.getCode(),
				ZonedDateTime.now().toEpochSecond());
		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.body(errResp);
	}
}
