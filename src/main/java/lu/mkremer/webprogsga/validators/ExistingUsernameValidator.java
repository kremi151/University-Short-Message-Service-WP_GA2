package lu.mkremer.webprogsga.validators;

import javax.ejb.EJB;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lu.mkremer.webprogsga.managers.UserManager;

public class ExistingUsernameValidator implements ConstraintValidator<ExistingUsername, String>{
	
	@EJB
	private UserManager um;

	@Override
	public void initialize(ExistingUsername constraintAnnotation) {}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return um.doesUsernameExist(value);
	}

}
