
package com.bernardomg.security.permission.test.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.test.context.jdbc.Sql;

@Sql({ "/db/queries/security/resource/single.sql", "/db/queries/security/resource/alternative.sql",
        "/db/queries/security/action/crud.sql", "/db/queries/security/permission/crud.sql",
        "/db/queries/security/role/single.sql", "/db/queries/security/role/alternative.sql",
        "/db/queries/security/user/single.sql", "/db/queries/security/relationship/role_permissions_crud_granted.sql",
        "/db/queries/security/relationship/role_alternative_permissions_crud_not_granted.sql",
        "/db/queries/security/relationship/user_role.sql",
        "/db/queries/security/relationship/user_role_alternative.sql" })
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface UserWithCrudPermissionsGrantedAndNotGranted {

}
