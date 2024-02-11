package vortex.annotate.annotations;

import java.lang.annotation.Target;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierNickname;

import static javax.annotation.meta.When.MAYBE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
@Target({METHOD, PARAMETER, FIELD})
@Retention(RUNTIME)
@Documented
@Nonnull(when = MAYBE)
@TypeQualifierNickname
public @interface Nullable {
}
