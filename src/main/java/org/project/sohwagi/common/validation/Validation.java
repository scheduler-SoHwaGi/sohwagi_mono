package org.project.sohwagi.common.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import jakarta.validation.Validator;
import java.util.Set;

public class Validation {
	private final static Validator validator = buildDefaultValidatorFactory().getValidator();

	public static <T> void validate(T object) {
		Set<ConstraintViolation<T>> violations = validator.validate(object);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
	}

}
