/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies a {@code check} constraint to be included in the generated DDL.
 * <ul>
 * <li>When a field or property is annotated, the check constraint is added to the column definition.
 * <li>When an entity class is annotated, the check constraint is added to the primary table.
 * </ul>
 *
 * @author Emmanuel Bernard
 *
 * @see DialectOverride.Check
 */
@Target({TYPE, METHOD, FIELD})
@Retention(RUNTIME)
public @interface Check {
	/**
	 * The check constraint, written in native SQL.
	 */
	String constraints();
}
