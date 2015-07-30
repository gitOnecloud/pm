package org.springframework.security.web.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import com.baodian.security.LoginTooOfenException;
import com.baodian.security.UserNotFoundException;
import com.baodian.security.UserOnLockException;
import com.baodian.security.RsaDecodeErrorException;
import com.baodian.service.user.UserManager;
import com.baodian.service.util.StaticData;
import com.baodian.util.StaticMethod;

/**
 * <tt>AuthenticationFailureHandler</tt> which performs a redirect to the value of the {@link #setDefaultFailureUrl
 * defaultFailureUrl} property when the <tt>onAuthenticationFailure</tt> method is called.
 * If the property has not been set it will send a 401 response to the client, with the error message from the
 * <tt>AuthenticationException</tt> which caused the failure.
 * <p>
 * If the {@code useForward} property is set, a {@code RequestDispatcher.forward} call will be made to
 * the destination instead of a redirect.
 *
 * @author Luke Taylor
 * @since 3.0
 */
public class SimpleUrlAuthenticationFailureHandler implements AuthenticationFailureHandler {
    protected final Log logger = LogFactory.getLog(getClass());
    //登录失败
    private String defaultFailureUrl;
    private String jsonFailureUrl;
    //未注册
    private String noUserFailureUrl;
	private String jsonNoUserFailureUrl;
	//账号被锁定
	private String lockFailureUrl;
	private String jsonLockFailureUrl;
	//rsa解码失败
	private String rsaFailureUrl;
	private String jsonRsaFailureUrl;
	//登录太频繁，不允许登录
	private String loginTooOfenUrl;
	private String jsonLoginTooOfenUrl;
	
	private boolean forwardToDestination = false;
    private boolean allowSessionCreation = true;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public SimpleUrlAuthenticationFailureHandler() {
    }

    public SimpleUrlAuthenticationFailureHandler(String defaultFailureUrl) {
        setDefaultFailureUrl(defaultFailureUrl);
    }

    /**
     * Performs the redirect or forward to the {@code defaultFailureUrl} if set, otherwise returns a 401 error code.
     * <p>
     * If redirecting or forwarding, {@code saveException} will be called to cache the exception for use in
     * the target view.
     */
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
    	
    	if(request.getParameter("j_ajax") != null) {//如果有"j_ajax"标记位，那么forward回去
    		
    		String url = jsonFailureUrl;
    		if(exception instanceof UserOnLockException) {
    			url = jsonLockFailureUrl;
    		} else if(exception instanceof UserNotFoundException) {
    			url = jsonNoUserFailureUrl;
    		} else if(exception instanceof RsaDecodeErrorException) {
    			url = jsonRsaFailureUrl;
    		} else if(exception instanceof LoginTooOfenException) {
        		url = jsonLoginTooOfenUrl;
    		} else if(exception instanceof BadCredentialsException) {
    			loginError(exception, request);
        	}
    		
    		request.getRequestDispatcher(url).forward(request, response);
    		return;
    	}
    	
        if (defaultFailureUrl == null) {
            logger.debug("No failure URL set, sending 401 Unauthorized error");

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed: " + exception.getMessage());
        } else {
            saveException(request, exception);
            
            String url = defaultFailureUrl;
            if(exception instanceof UserOnLockException) {
    			url = lockFailureUrl;
    		} else if(exception instanceof UserNotFoundException) {
    			url = noUserFailureUrl;
        	} else if(exception instanceof RsaDecodeErrorException) {
        		url = rsaFailureUrl;
        	} else if(exception instanceof LoginTooOfenException) {
        		url = loginTooOfenUrl;
        	} else if(exception instanceof BadCredentialsException) {
    			loginError(exception, request);
        	}
            
            if (forwardToDestination) {
                logger.debug("Forwarding to " + url);

                request.getRequestDispatcher(url).forward(request, response);
            } else {
                logger.debug("Redirecting to " + url);
                redirectStrategy.sendRedirect(request, response, url);
            }
        }
    }
    /**
     * 登录失败后的处理
     */
    @SuppressWarnings("deprecation")
	private void loginError(AuthenticationException exception, HttpServletRequest request) {
		String account = ((UserDetails) exception.getExtraInformation()).getUsername();
		UserManager um = (UserManager) StaticData.context.getBean("userManager");
		um.loginError(account, "IP: " + StaticMethod.getRemoteAddr(request) + 
				"，浏览器: " + StaticMethod.getBrowser(request) + "。");
	}

	/**
     * Caches the {@code AuthenticationException} for use in view rendering.
     * <p>
     * If {@code forwardToDestination} is set to true, request scope will be used, otherwise it will attempt to store
     * the exception in the session. If there is no session and {@code allowSessionCreation} is {@code true} a session
     * will be created. Otherwise the exception will not be stored.
     */
    protected final void saveException(HttpServletRequest request, AuthenticationException exception) {
        if (forwardToDestination) {
            request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
        } else {
            HttpSession session = request.getSession(false);

            if (session != null || allowSessionCreation) {
                request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
            }
        }
    }

    /**
     * The URL which will be used as the failure destination.
     *
     * @param defaultFailureUrl the failure URL, for example "/loginFailed.jsp".
     */
    public void setDefaultFailureUrl(String defaultFailureUrl) {
        Assert.isTrue(UrlUtils.isValidRedirectUrl(defaultFailureUrl),
                "'" + defaultFailureUrl + "' is not a valid redirect URL");
        this.defaultFailureUrl = defaultFailureUrl;
    }

    protected boolean isUseForward() {
        return forwardToDestination;
    }

    /**
     * If set to <tt>true</tt>, performs a forward to the failure destination URL instead of a redirect. Defaults to
     * <tt>false</tt>.
     */
    public void setUseForward(boolean forwardToDestination) {
        this.forwardToDestination = forwardToDestination;
    }

    /**
     * Allows overriding of the behaviour when redirecting to a target URL.
     */
    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }
    protected boolean isAllowSessionCreation() {
        return allowSessionCreation;
    }
    public void setAllowSessionCreation(boolean allowSessionCreation) {
        this.allowSessionCreation = allowSessionCreation;
    }
	public void setJsonFailureUrl(String jsonFailureUrl) {
		this.jsonFailureUrl = jsonFailureUrl;
	}
	 public void setNoUserFailureUrl(String noUserFailureUrl) {
		this.noUserFailureUrl = noUserFailureUrl;
	}
	public void setJsonNoUserFailureUrl(String jsonNoUserFailureUrl) {
		this.jsonNoUserFailureUrl = jsonNoUserFailureUrl;
	}
	public void setForwardToDestination(boolean forwardToDestination) {
		this.forwardToDestination = forwardToDestination;
	}
	public void setLockFailureUrl(String lockFailureUrl) {
		this.lockFailureUrl = lockFailureUrl;
	}
	public void setJsonLockFailureUrl(String jsonLockFailureUrl) {
		this.jsonLockFailureUrl = jsonLockFailureUrl;
	}
	public void setRsaFailureUrl(String rsaFailureUrl) {
		this.rsaFailureUrl = rsaFailureUrl;
	}
	public void setJsonRsaFailureUrl(String jsonRsaFailureUrl) {
		this.jsonRsaFailureUrl = jsonRsaFailureUrl;
	}
	public void setLoginTooOfenUrl(String loginTooOfenUrl) {
		this.loginTooOfenUrl = loginTooOfenUrl;
	}
	public void setJsonLoginTooOfenUrl(String jsonLoginTooOfenUrl) {
		this.jsonLoginTooOfenUrl = jsonLoginTooOfenUrl;
	}
}
