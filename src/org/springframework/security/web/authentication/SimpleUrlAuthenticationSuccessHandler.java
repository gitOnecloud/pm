package org.springframework.security.web.authentication;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;

import com.baodian.service.role.SecuManager;
import com.baodian.service.util.StaticData;

/**
 * <tt>AuthenticationSuccessHandler</tt> which can be configured with a default URL which users should be
 * sent to upon successful authentication.
 * <p>
 * The logic used is that of the {@link AbstractAuthenticationTargetUrlRequestHandler parent class}.
 *
 * @author Luke Taylor
 * @since 3.0
 */
public class SimpleUrlAuthenticationSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler implements AuthenticationSuccessHandler {

    public SimpleUrlAuthenticationSuccessHandler() {
    }
    /**
     * Constructor which sets the <tt>defaultTargetUrl</tt> property of the base class.
     * @param defaultTargetUrl the URL to which the user should be redirected on successful authentication.
     */
    public SimpleUrlAuthenticationSuccessHandler(String defaultTargetUrl) {
        setDefaultTargetUrl(defaultTargetUrl);
    }
    /**
     * Calls the parent class {@code handle()} method to forward or redirect to the target URL, and
     * then calls {@code clearAuthenticationAttributes()} to remove any leftover session data.
     */
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
    	//登录成功后载入菜单
    	if(request.getParameter("j_ajax") != null) {//如果有"j_ajax"标记位，那么forward回去
    		SecuManager sm = (SecuManager) StaticData.context.getBean("secuManager");
    		JSONObject json = new JSONObject();
    		json.put("status", 0);
    		json.put("mess", "登录成功！");
    		if(request.getParameter("j_onlyLogin") == null) {
    			org.springframework.security.core.userdetails.User u = SecuManager.currentUser();
    			json.put("id", u.getId()[0]);
    			json.put("account", u.getUsername());
    			json.put("name", u.getStr()[0]);
    			json.put("depm", u.getDepartment());
    			json.put("menu", sm.findMenu());
    		}
    		//包含特殊字符(&#34;)时传对象过去
    		request.setAttribute("json", json.toString());
    		request.getRequestDispatcher("/json.jsp").forward(request, response);
    	} else {
    		handle(request, response, authentication);
    	}
        clearAuthenticationAttributes(request);
    }

    /**
     * Removes temporary authentication-related data which may have been stored in the session
     * during the authentication process.
     */
    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
