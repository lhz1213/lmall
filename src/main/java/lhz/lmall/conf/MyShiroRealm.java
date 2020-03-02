package lhz.lmall.conf;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import lhz.lmall.entity.Permission;
import lhz.lmall.entity.Role;
import lhz.lmall.entity.User;
import lhz.lmall.service.UserService;

public class MyShiroRealm extends AuthorizingRealm {

	@Autowired
	@Lazy
	private UserService us;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String username = (String) principals.getPrimaryPrincipal();
		User user = us.getByName(username);
		if (user == null) {
			return null;
		}
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		for (Role role : user.getRoles()) {
			simpleAuthorizationInfo.addRole(role.getName());
			for (Permission p : role.getPermissions()) {
				simpleAuthorizationInfo.addStringPermission(p.getName());
			}
		}

		return simpleAuthorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
		User user = us.getByName(username);
		if (user == null) {
			return null;
		}
		String passwordInDB = user.getPassword();
		String salt = user.getSalt();
		SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, passwordInDB,
				ByteSource.Util.bytes(salt), getName());

		return simpleAuthenticationInfo;
	}

}
