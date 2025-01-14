/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.type;

import org.hibernate.dialect.Dialect;
import org.hibernate.metamodel.model.convert.spi.BasicValueConverter;
import org.hibernate.type.descriptor.java.BooleanJavaType;
import org.hibernate.type.descriptor.java.IntegerJavaType;
import org.hibernate.type.descriptor.java.JavaType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.hibernate.type.descriptor.jdbc.JdbcType;

/**
 * Handles conversion to/from Boolean as `0` (false) or `1` (true)
 *
 * @author Steve Ebersole
 */
@Converter
public class NumericBooleanConverter implements AttributeConverter<Boolean, Integer>,
		BasicValueConverter<Boolean, Integer> {
	/**
	 * Singleton access
	 */
	public static final NumericBooleanConverter INSTANCE = new NumericBooleanConverter();

	@Override
	public Integer convertToDatabaseColumn(Boolean attribute) {
		return toRelationalValue( attribute );
	}

	@Override
	public Boolean convertToEntityAttribute(Integer dbData) {
		return toDomainValue( dbData );
	}

	@Override
	public Boolean toDomainValue(Integer relationalForm) {
		if ( relationalForm == null ) {
			return null;
		}

		if ( 1 == relationalForm ) {
			return true;
		}

		if ( 0 == relationalForm ) {
			return false;
		}

		return null;
	}

	@Override
	public Integer toRelationalValue(Boolean domainForm) {
		if ( domainForm == null ) {
			return null;
		}

		return domainForm ? 1 : 0;
	}

	@Override
	public JavaType<Boolean> getDomainJavaType() {
		return BooleanJavaType.INSTANCE;
	}

	@Override
	public JavaType<Integer> getRelationalJavaType() {
		return IntegerJavaType.INSTANCE;
	}

	@Override
	public String getCheckCondition(String columnName, JdbcType jdbcType, Dialect dialect) {
		return columnName + " in (0,1)";
	}
}
