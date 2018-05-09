package cloud.estimator.user.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.spring.web.advice.security.AuthenticationAdviceTrait;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

@ControllerAdvice
public class SecurityExceptionHandler implements SecurityAdviceTrait, AuthenticationAdviceTrait {

	@Override
	public ResponseEntity<Problem> handleAuthentication(AuthenticationException e, NativeWebRequest request) {
		// TODO Auto-generated method stub
		return SecurityAdviceTrait.super.handleAuthentication(e, request);
	}

	@Override
	public ResponseEntity<Problem> handleAccessDenied(AccessDeniedException e, NativeWebRequest request) {
		// TODO Auto-generated method stub
		return SecurityAdviceTrait.super.handleAccessDenied(e, request);
	}

}
