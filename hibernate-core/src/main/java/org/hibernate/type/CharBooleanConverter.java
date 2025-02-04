/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.type;

import jakarta.persistence.AttributeConverter;
import org.hibernate.dialect.Dialect;
import org.hibernate.metamodel.model.convert.spi.BasicValueConverter;
import org.hibernate.type.descriptor.java.BooleanJavaType;
import org.hibernate.type.descriptor.java.CharacterJavaType;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.jdbc.JdbcType;

/**
 * Abstract supertype of converters which map {@link Boolean} to {@link Character}.
 *
 * @author Steve Ebersole
 * @author Gavin King
 */
public abstract class CharBooleanConverter
		implements AttributeConverter<Boolean, Character>, BasicValueConverter<Boolean, Character> {
	/**
	 * Singleton access
	 */
	@Override
	public Character convertToDatabaseColumn(Boolean attribute) {
		return toRelationalValue( attribute );
	}

	@Override
	public Boolean convertToEntityAttribute(Character dbData) {
		return toDomainValue( dbData );
	}

	@Override
	public JavaType<Boolean> getDomainJavaType() {
		return BooleanJavaType.INSTANCE;
	}

	@Override
	public JavaType<Character> getRelationalJavaType() {
		return CharacterJavaType.INSTANCE;
	}

	@Override
	public String getCheckCondition(String columnName, JdbcType jdbcType, Dialect dialect) {
		return dialect.getCheckCondition( columnName, getValues() );
	}

	@Override
	public String getSpecializedTypeDeclaration(JdbcType jdbcType, Dialect dialect) {
		return dialect.getEnumTypeDeclaration( getValues() );
	}

	protected abstract String[] getValues();
}
