/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.engine;

import org.hibernate.AssertionFailure;
import org.hibernate.annotations.OptimisticLockType;

/**
 * Describes how an entity should be optimistically locked.
 * <p>
 * This enumeration is mainly for internal use, since it
 * is isomorphic to {@link OptimisticLockType}.
 *
 * @author Steve Ebersole
 */
public enum OptimisticLockStyle {
	/**
	 * no optimistic locking
	 */
	NONE,
	/**
	 * use a dedicated version column
	 */
	VERSION,
	/**
	 * dirty columns are compared
	 */
	DIRTY,
	/**
	 * all columns are compared
	 */
	ALL;

	public boolean isAllOrDirty() {
		return isAll() || isDirty();
	}

	public boolean isAll() {
		return this == ALL;
	}

	public boolean isDirty() {
		return this == DIRTY;
	}

	public boolean isVersion() {
		return this == VERSION;
	}

	public boolean isNone() {
		return this == NONE;
	}

	/**
	 * @deprecated these integer codes have not been used for a long time
	 */
	@Deprecated(since = "6.2", forRemoval = true)
	public int getOldCode() {
		switch (this) {
			case NONE:
				return -1;
			case VERSION:
				return 0;
			case DIRTY:
				return 1;
			case ALL:
				return 2;
			default:
				throw new AssertionFailure("Unknown OptimisticLockStyle");
		}
	}

	/**
	 * Given an old code (one of the int constants from Cascade),
	 * interpret it as one of the new enums.
	 *
	 * @param oldCode The old int constant code
	 *
	 * @return The interpreted enum value
	 *
	 * @throws IllegalArgumentException If the code did not match any legacy constant.
	 *
	 * @deprecated these integer codes have not been used for a long time
	 */
	@Deprecated(since = "6.2", forRemoval = true)
	public static OptimisticLockStyle interpretOldCode(int oldCode) {
		switch ( oldCode ) {
			case -1:
				return NONE;
			case 0:
				return VERSION;
			case 1:
				return DIRTY;
			case 2:
				return ALL;
			default:
				throw new IllegalArgumentException( "Illegal legacy optimistic lock style code : " + oldCode );
		}
	}
}
