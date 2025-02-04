/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

package org.hibernate.spatial.testing;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.engine.jdbc.connections.spi.JdbcConnectionAccess;
import org.hibernate.spatial.CommonSpatialFunction;
import org.hibernate.spatial.integration.SpatialTestDataProvider;

import org.hibernate.testing.orm.junit.DialectContext;
import org.hibernate.testing.orm.junit.SessionFactoryScope;
import org.hibernate.testing.orm.junit.SessionFactoryScopeAware;

import org.h2gis.functions.factory.H2GISFunctions;


public abstract class SpatialSessionFactoryAware extends SpatialTestDataProvider implements SessionFactoryScopeAware {
	protected SessionFactoryScope scope;
	protected Set<String> supportedFunctions;

	@Override
	public void injectSessionFactoryScope(SessionFactoryScope scope) {
		this.scope = scope;
		//scope is set to null during test cleanup
		if ( scope != null ) {
			this.supportedFunctions = scope.getSessionFactory()
					.getQueryEngine()
					.getSqmFunctionRegistry()
					.getFunctions()
					.keySet();
			if ( DialectContext.getDialect() instanceof H2Dialect ) {
				initH2GISExtensionsForInMemDb();
			}
		}
	}

	public boolean isSupported(CommonSpatialFunction function) {
		return supportedFunctions.contains( function.name() );
	}

	protected void initH2GISExtensionsForInMemDb() {
		this.scope.inSession( session -> {
			final JdbcConnectionAccess jdbcConnectionAccess = session.getJdbcConnectionAccess();
			Connection connection = null;
			try {
				H2GISFunctions.load( connection = jdbcConnectionAccess.obtainConnection() );
			}
			catch (SQLException e) {
				throw new RuntimeException( e );
			}
			finally {
				try {
					jdbcConnectionAccess.releaseConnection( connection );
				}
				catch (SQLException ignore) {
				}
			}
		} );
	}
}
