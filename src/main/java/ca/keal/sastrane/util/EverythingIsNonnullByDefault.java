package ca.keal.sastrane.util;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * FindBugs extension annotation that puts @Nonnull on fields, methods, and parameters. From <a
 * href="http://stackoverflow.com/a/13429092">David Harkness on StackOverflow</a>.
 */
@Nonnull
@Documented
@Retention(RetentionPolicy.RUNTIME)
@TypeQualifierDefault({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface EverythingIsNonnullByDefault {}