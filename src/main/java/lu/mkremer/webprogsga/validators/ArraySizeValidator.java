package lu.mkremer.webprogsga.validators;

import java.lang.reflect.Array;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ArraySizeValidator implements ConstraintValidator<ArraySize, Object>{
	
	private int min, max;

	@Override
	public void initialize(ArraySize constraintAnnotation) {
		this.min = constraintAnnotation.min();
		this.max = constraintAnnotation.max();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if(value != null && value.getClass().isArray()) {
			int length = Array.getLength(value);
			return length >= min && length <= max;
		}
		return false;
	}

}
