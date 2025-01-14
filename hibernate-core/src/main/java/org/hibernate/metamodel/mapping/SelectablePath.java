/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.metamodel.mapping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import org.hibernate.Incubating;

/**
 * The path for a selectable.
 *
 * @author Christian Beikov
 */
@Incubating
public class SelectablePath implements Serializable {
	private final SelectablePath parent;
	private final String name;
	private final int index;

	public SelectablePath(String root) {
		this.parent = null;
		this.name = root.intern();
		this.index = 0;
	}

	private SelectablePath(SelectablePath parent, String name) {
		this.parent = parent;
		this.name = name;
		this.index = parent.index + 1;
	}

	public SelectablePath[] getParts() {
		final SelectablePath[] array = new SelectablePath[index + 1];
		parts( array );
		return array;
	}

	private void parts(SelectablePath[] array) {
		if ( parent != null ) {
			parent.parts( array );
		}
		array[index] = this;
	}

	public SelectablePath[] relativize(SelectablePath basePath) {
		final SelectablePath[] array = new SelectablePath[index - basePath.index];
		relativize( array, basePath );
		return array;
	}

	private boolean relativize(SelectablePath[] array, SelectablePath basePath) {
		if ( equals( basePath ) ) {
			return true;
		}
		if ( parent != null ) {
			if ( parent.relativize( array, basePath ) ) {
				array[index - basePath.index - 1] = this;
				return true;
			}
		}
		return false;
	}

	public String getSelectableName() {
		return name;
	}

	public SelectablePath getParent() {
		return parent;
	}

	public SelectablePath append(String selectableName) {
		return new SelectablePath( this, selectableName );
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder( name.length() * index );
		toString( sb );
		return sb.toString();
	}

	private void toString(StringBuilder sb) {
		if ( parent != null ) {
			parent.toString( sb );
			sb.append( '.' );
		}
		sb.append( name );
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		SelectablePath that = (SelectablePath) o;

		if ( !Objects.equals( parent, that.parent ) ) {
			return false;
		}
		return name.equals( that.name );
	}

	@Override
	public int hashCode() {
		int result = parent != null ? parent.hashCode() : 0;
		result = 31 * result + name.hashCode();
		return result;
	}
}
