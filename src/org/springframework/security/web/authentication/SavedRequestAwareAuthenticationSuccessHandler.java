package org.springframework.security.web.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.util.StringUtils;

import com.baodian.service.role.SecuManager;
import com.baodian.service.user.UserManager;
import com.baodian.service.util.StaticData;
import com.baodian.util.StaticMethod;

/**
 * An authentication success strategy which can make use of the {@link DefaultSavedRequest} which may have been stored in
 * the session by the {@link ExceptionTranslationFilter}. When such a request is intercepted and requires authentication,
 * the request data is stored to record the original destination before the authentication process commenced, and to
 * allow the request to be reconstructed when a redirect to the same URL occurs. This class is responsible for
 * performing the redirect to the original URL if appropriate.
 * <p>
 * Following a successful authentication, it decides on the redirect destination, based on the following scenarios:
 * <ul>
 * <li>
 * If the {@code alwaysUseDefaultTargetUrl} property is set to true, the {@code defaultTargetUrl}
 * will be used for the destination. Any {@code DefaultSavedRequest} stored in the session will be
 * removed.
 * </li>
 * <li>
 * If the {@code targetUrlParameter} has been set on the request, the value will be used as the destination.
 * Any {@code DefaultSavedRequest} will again be removed.
 * </li>
 * <li>
 * If a {@link SavedRequest} is found in the {@code RequestCache} (as set by the {@link ExceptionTranslationFilter} to
 * record the original destination before the authentication process commenced), a redirect will be performed to the
 * Url of that original destination. The {@code SavedRequest} object will remain cached and be picked up
 * when the redirected request is received
 * (See {@link org.springframework.security.web.savedrequest.SavedRequestAwareWrapper SavedRequestAwareWrapper}).
 * </li>
 * <li>
 * If no {@code SavedRequest} is found, it will delegate to the base class.
 * </li>
 * </ul>
 *
 * @author Luke Taylor
 * @since 3.0
 */
public class SavedRequestAwareAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    protected final Log logger = LogFactory.getLog(this.getClass());

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
    	//@update登录成功后的处理
    	org.springframework.security.core.userdetails.User u = SecuManager.currentUser();
		UserManager um = (UserManager) StaticData.context.getBean("userManager");
		um.loginSucc(u.getId()[0], u.getStr()[0] + "(" + u.getUsername() + ")登录成功，IP: " +
				StaticMethod.getRemoteAddr(request) + 
				"，浏览器: " + StaticMethod.getBrowser(request) + "。");
    	//end
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest == null) {
            super.onAuthenticationSuccess(request, response, authentication);

            return;
        }
        String targetUrlParameter = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl() || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
            requestCache.removeRequest(request, response);
            super.onAuthenticationSuccess(request, response, authentication);

            return;
        }

        clearAuthenticationAttributes(request);

        // Use the DefaultSavedRequest URL
        String targetUrl = savedRequest.getRedirectUrl();
        logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
        //@update 如果有"j_ajax"标记位，那么forward回去
        if(request.getParameter("j_ajax") != null) {
        	JSONObject json = new JSONObject();
    		json.put("status", 0);
    		json.put("mess", "登录成功！");
    		json.put("jumpUrl", targetUrl);//客户端跳转页面
    		request.setAttribute("json", json.toString());
    		request.getRequestDispatcher("/json.jsp").forward(request, response);
        } else {
        	getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
    }

    public void setRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;
    }
}
