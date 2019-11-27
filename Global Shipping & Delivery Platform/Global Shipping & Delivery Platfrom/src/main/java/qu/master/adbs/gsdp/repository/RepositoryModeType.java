package qu.master.adbs.gsdp.repository;

import javax.inject.Qualifier;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


@Qualifier
@Retention(RUNTIME)
@Target({FIELD, TYPE, METHOD})
public @interface RepositoryModeType {
	
	RepositoryMode value();
	
	
}

