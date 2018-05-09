package cloud.estimator.user.web.rest.errors;

import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.validation.ConstraintViolationProblem;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;

/**
 * Controller advice to translate the server side exceptions to client-friendly
 * json structures. The error response follows RFC7807 - Problem Details for
 * HTTP APIs (https://tools.ietf.org/html/rfc7807)
 */
@ControllerAdvice
public class ExceptionTranslator implements ProblemHandling {

	/**
	 * Post-process Problem payload to add the message key for front-end if needed
	 */
	@Override
	public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
		if (entity == null || entity.getBody() == null) {
			return entity;
		}
		Problem problem = entity.getBody();
		if (!(problem instanceof ConstraintViolationProblem || problem instanceof DefaultProblem)) {
			return entity;
		}
		ProblemBuilder builder = Problem.builder()
				.withType(Problem.DEFAULT_TYPE.equals(problem.getType()) ? ErrorConstants.DEFAULT_TYPE
						: problem.getType())
				.withStatus(problem.getStatus()).withTitle(problem.getTitle())
				.with("path", request.getNativeRequest(HttpServletRequest.class).getRequestURI());

		if (problem instanceof ConstraintViolationProblem) {
			builder.with("violations", ((ConstraintViolationProblem) problem).getViolations()).with("message",
					ErrorConstants.ERR_VALIDATION);
			return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
		} else {
			builder.withCause(((DefaultProblem) problem).getCause()).withDetail(problem.getDetail())
					.withInstance(problem.getInstance());
			problem.getParameters().forEach(builder::with);
			if (!problem.getParameters().containsKey("message") && problem.getStatus() != null) {
				builder.with("message", "error.http." + problem.getStatus().getStatusCode());
			}
			return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
		}
	}

	@ExceptionHandler(ConcurrencyFailureException.class)
	public ResponseEntity<Problem> handleConcurrencyFailure(ConcurrencyFailureException ex, NativeWebRequest request) {
		Problem problem = Problem.builder().withStatus(Status.CONFLICT)
				.with("message", ErrorConstants.ERR_CONCURRENCY_FAILURE).build();
		return create(ex, problem, request);
	}
}
