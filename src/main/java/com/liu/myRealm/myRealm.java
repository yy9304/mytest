package com.liu.myRealm;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.liu.model.Blogger;
import com.liu.service.BloggerService;

 /** 
 * @ClassName: myRealm 
 * @author: lyd
 * @date: 2017��10��10�� ����9:56:17 
 * @describe:Shiro�Զ���Realm
 */
public class myRealm  extends AuthorizingRealm{
	@Resource 
	private BloggerService bloggerService;

	@Override
//	��Ȩ��ѯ�ص�����
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
//	��֤�ص�����,���е�½��֤
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username=(String)token.getPrincipal();//��ȡ�û���
		Blogger blogger=bloggerService.getByUsername(username);
		if(blogger!=null)
		{
			SecurityUtils.getSubject().getSession().setAttribute("currentUser", blogger);
			AuthenticationInfo authenticationInfo=new SimpleAuthenticationInfo(blogger.getUsername(),blogger.getPassword(),"myRealm");
			return authenticationInfo;
		}
		return null;
	}
	
}
