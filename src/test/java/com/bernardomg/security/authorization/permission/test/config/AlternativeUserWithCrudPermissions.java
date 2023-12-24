
package com.bernardomg.security.authorization.permission.test.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.test.context.jdbc.Sql;

@Sql({ "/db/queries/security/user/alternative.sql", "/db/queries/security/relationship/user_alternative_role.sql" })
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AlternativeUserWithCrudPermissions {

}
