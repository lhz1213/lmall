package lhz.lmall.conf;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lhz.lmall.dao.PermissionDao;
import lhz.lmall.entity.Permission;

@Configuration
public class ShiroConf {
	private List<Permission> permissions;

	private PermissionDao pd;

	public ShiroConf(PermissionDao pd) {
		super();
		this.pd = pd;
	}

	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		hashedCredentialsMatcher.setHashAlgorithmName("md5");
		hashedCredentialsMatcher.setHashIterations(2);
		return hashedCredentialsMatcher;
	}

	@Bean
	public MyShiroRealm myShiroRealm() {
		MyShiroRealm myShiroRealm = new MyShiroRealm();
		myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
		return myShiroRealm;
	}

	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(myShiroRealm());
		return securityManager;
	}

	@Bean
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {

		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");
		shiroFilterFactoryBean.setLoginUrl("/login");
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		filterChainDefinitionMap.put("/bootstrap/**", "anon");
		filterChainDefinitionMap.put("/css/**", "anon");
		filterChainDefinitionMap.put("/img/**", "anon");
		filterChainDefinitionMap.put("/js/**", "anon");
		filterChainDefinitionMap.put("/foreregister", "anon");
		filterChainDefinitionMap.put("/forelogin", "anon");
		filterChainDefinitionMap.put("/forecheckLogin", "anon");
		filterChainDefinitionMap.put("/forehome", "anon");
		filterChainDefinitionMap.put("/foreproduct/**", "anon");
		filterChainDefinitionMap.put("/forecategory/**", "anon");
		filterChainDefinitionMap.put("/foresearch/**", "anon");
		filterChainDefinitionMap.put("/home", "anon");
		filterChainDefinitionMap.put("/register", "anon");
		filterChainDefinitionMap.put("/category", "anon");
		filterChainDefinitionMap.put("/login", "anon");
		filterChainDefinitionMap.put("/product", "anon");
		filterChainDefinitionMap.put("/registerSuccess", "anon");
		filterChainDefinitionMap.put("/search", "anon");
		if (permissions == null) {
			permissions = pd.findAll();
		}
		permissions.forEach(p -> filterChainDefinitionMap.put(p.getUrl(), "perms[" + p.getName() + "]"));
		filterChainDefinitionMap.put("/admin/**", "roles[normal]");
		filterChainDefinitionMap.put("/**", "authc");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

}
