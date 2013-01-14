package de.tuilmenau.ics.CommonSim.datastream.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation allowing the auto-wiring of datastream nodes as class fields. 
 * 
 * @author Markus Brückner
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoWire {
	/**
	 * The name of the data stream node to open. 
	 */
	String name();
	/**
	 * The actual type of the node to open. If the type given does not correspond to the
	 * type of the existing node under the given name (if any), then an error will be thrown.
	 * If this is not set then the class of the field this annotation belongs to will be used.
	 */
	Class<?> type() default Object.class;
	/**
	 * Make the name unique. In order to make the name unique it is appended with a unique id
	 * for the object containing the field and the given suffix (if any) separated by dots. 
	 * The resulting name will have the form "name.id.suffix" 
	 */
	boolean unique() default false;
	/**
	 * The suffix to append to the name if it is to be made unique. If unique is false (default),
	 * the suffix is ignored.
	 */
	String suffix() default "";
}
