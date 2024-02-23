package vortex.http.test.controller;

import vortex.annotate.components.Controller;
import vortex.annotate.controller.RequestMapping;
import vortex.annotate.method.mapping.GetMapping;
import vortex.http.elements.HttpStatus;
import vortex.http.elements.ResponseStatus;
import vortex.http.elements.ResponseStatusException;

@Controller
@RequestMapping
public class HttpStatusTestController {

	@GetMapping("/SWITCHING_PROTOCOLS")
	public ResponseStatus<Object> statusSwitchingProtocols() {
		return new ResponseStatus<>(HttpStatus.SWITCHING_PROTOCOLS);
	}

	@GetMapping("/OK")
	public ResponseStatus<Object> statusOK() {
		return new ResponseStatus<>(HttpStatus.OK);
	}

	@GetMapping("/CREATED")
	public ResponseStatus<Object> statusCreated() {
		return new ResponseStatus<>(HttpStatus.CREATED);
	}

	@GetMapping("/ACCEPTED")
	public ResponseStatus<Object> statusAccepted() {
		return new ResponseStatus<>(HttpStatus.ACCEPTED);
	}

	@GetMapping("/NON_AUTHORITATIVE_INFORMATION")
	public ResponseStatus<Object> statusNonAuthoritativeInformation() {
		return new ResponseStatus<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
	}

	@GetMapping("/NO_CONTENT")
	public ResponseStatus<Object> statusNoContent() {
		return new ResponseStatus<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/RESET_CONTENT")
	public ResponseStatus<Object> statusResetContent() {
		return new ResponseStatus<>(HttpStatus.RESET_CONTENT);
	}

	@GetMapping("/PARTIAL_CONTENT")
	public ResponseStatus<Object> statusPartialContent() {
		return new ResponseStatus<>(HttpStatus.PARTIAL_CONTENT);
	}

	@GetMapping("/MULTIPLE_CHOICES")
	public ResponseStatus<Object> statusMultipleChoices() {
		return new ResponseStatus<>(HttpStatus.MULTIPLE_CHOICES);
	}

	@GetMapping("/MOVED_PERMANENTLY")
	public ResponseStatus<Object> statusMovedPermanently() {
		return new ResponseStatus<>(HttpStatus.MOVED_PERMANENTLY);
	}

	@GetMapping("/FOUND")
	public ResponseStatus<Object> statusFound() {
		return new ResponseStatus<>(HttpStatus.FOUND);
	}

	@GetMapping("/SEE_OTHER")
	public ResponseStatus<Object> statusSeeOther() {
		return new ResponseStatus<>(HttpStatus.SEE_OTHER);
	}

	@GetMapping("/NOT_MODIFIED")
	public ResponseStatus<Object> statusNotModified() {
		return new ResponseStatus<>(HttpStatus.NOT_MODIFIED);
	}

	@GetMapping("/USE_PROXY")
	public ResponseStatus<Object> statusUseProxy() {
		return new ResponseStatus<>(HttpStatus.USE_PROXY);
	}

	@GetMapping("/TEMPORARY_REDIRECT")
	public ResponseStatus<Object> statusTemporaryRedirect() {
		return new ResponseStatus<>(HttpStatus.TEMPORARY_REDIRECT);
	}

	@GetMapping("/BAD_REQUEST")
	public ResponseStatus<Object> statusBadRequest() throws ResponseStatusException {
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMeaning());
	}

	@GetMapping("/UNAUTHORIZED")
	public ResponseStatus<Object> statusUnauthorized() throws ResponseStatusException {
		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getMeaning());
	}

	@GetMapping("/PAYMENT_REQUIRED")
	public ResponseStatus<Object> statusPaymentRequired() throws ResponseStatusException {
		throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, HttpStatus.PAYMENT_REQUIRED.getMeaning());

	}

	@GetMapping("/FORBIDDEN")
	public ResponseStatus<Object> statusForbidden() throws ResponseStatusException {
		throw new ResponseStatusException(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.getMeaning());
	}

	@GetMapping("/NOT_FOUND")
	public ResponseStatus<Object> statusNotFound() throws ResponseStatusException {
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getMeaning());
	}

	@GetMapping("/METHOD_NOT_ALLOWED")
	public ResponseStatus<Object> statusMethodNotAllowed() throws ResponseStatusException {
		throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED.getMeaning());
	}

	@GetMapping("/NOT_ACCEPTABLE")
	public ResponseStatus<Object> statusNotAcceptable() throws ResponseStatusException {
		throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, HttpStatus.NOT_ACCEPTABLE.getMeaning());
	}

	@GetMapping("/PROXY_AUTHENTICATION_REQUIRED")
	public ResponseStatus<Object> statusProxyAuthenticationRequired() throws ResponseStatusException {
		throw new ResponseStatusException(HttpStatus.PROXY_AUTHENTICATION_REQUIRED, HttpStatus.PROXY_AUTHENTICATION_REQUIRED.getMeaning());
	}

	@GetMapping("/REQUEST_TIMEOUT")
	public ResponseStatus<Object> statusRequestTimeout() throws ResponseStatusException {
		throw new ResponseStatusException(HttpStatus.REQUEST_TIMEOUT, HttpStatus.REQUEST_TIMEOUT.getMeaning());
	}

	@GetMapping("/CONFLICT")
	public ResponseStatus<Object> statusConflict() throws ResponseStatusException {
		throw new ResponseStatusException(HttpStatus.CONFLICT, HttpStatus.CONFLICT.getMeaning());
	}

	@GetMapping("/GONE")
	public ResponseStatus<Object> statusGone() throws ResponseStatusException {
		throw new ResponseStatusException(HttpStatus.GONE, HttpStatus.GONE.getMeaning());
	}

	@GetMapping("/LENGTH_REQUIRED")
	public ResponseStatus<Object> statusLengthRequired() throws ResponseStatusException {
		throw new ResponseStatusException(HttpStatus.LENGTH_REQUIRED, HttpStatus.LENGTH_REQUIRED.getMeaning());
	}

	@GetMapping("/PRECONDITION_FAILED")
	public ResponseStatus<Object> statusPreconditionFailed() throws ResponseStatusException {
		throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, HttpStatus.PRECONDITION_FAILED.getMeaning());
	}

	@GetMapping("/REQUEST_ENTITY_TOO_LARGE")
	public ResponseStatus<Object> statusRequestEntityTooLarge() throws ResponseStatusException {
		throw new ResponseStatusException(HttpStatus.REQUEST_ENTITY_TOO_LARGE, HttpStatus.REQUEST_ENTITY_TOO_LARGE.getMeaning());
	}

	@GetMapping("/REQUEST_URI_TOO_LONG")
	public ResponseStatus<Object> statusRequestURITooLong() throws ResponseStatusException {
		throw new ResponseStatusException(HttpStatus.REQUEST_URI_TOO_LONG, HttpStatus.REQUEST_URI_TOO_LONG.getMeaning());
	}

	@GetMapping("/UNSUPPORTED_MEDIA_TYPE")
	public ResponseStatus<Object> statusUnsupportedMediaType() throws ResponseStatusException {
		throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, HttpStatus.UNSUPPORTED_MEDIA_TYPE.getMeaning());
	}

	@GetMapping("/REQUEST_RANGE_NOT_SATISFIABLE")
	public ResponseStatus<Object> statusRequestRangeNotSatisfiable() throws ResponseStatusException {
		throw new ResponseStatusException(HttpStatus.REQUEST_RANGE_NOT_SATISFIABLE, HttpStatus.REQUEST_RANGE_NOT_SATISFIABLE.getMeaning());
	}

	@GetMapping("/EXPECTATION_FAILED")
	public ResponseStatus<Object> statusExpectationFailed() throws ResponseStatusException {
		throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, HttpStatus.EXPECTATION_FAILED.getMeaning());
	}

	@GetMapping("/INTERNAL_SERVER_ERROR")
	public ResponseStatus<Object> statusInternalServerError() throws ResponseStatusException {
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getMeaning());
	}

	@GetMapping("/NOT_IMPLEMENTED")
	public ResponseStatus<Object> statusNotImplemented() throws ResponseStatusException {
		throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, HttpStatus.NOT_IMPLEMENTED.getMeaning());
	}

	@GetMapping("/BAD_GATEWAY")
	public ResponseStatus<Object> statusBadGateway() throws ResponseStatusException {
		throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, HttpStatus.BAD_GATEWAY.getMeaning());
	}

	@GetMapping("/SERVICE_UNAVAILABLE")
	public ResponseStatus<Object> statusServiceUnavailable() throws ResponseStatusException {
		throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE.getMeaning());
	}

	@GetMapping("/GATEWAY_TIMEOUT")
	public ResponseStatus<Object> statusGatewayTimeout() throws ResponseStatusException {
		throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, HttpStatus.GATEWAY_TIMEOUT.getMeaning());
	}


}
