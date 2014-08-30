/* Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.cas.authentication;

import java.util.ArrayList;

import org.jasig.cas.client.validation.Assertion;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import com.baodian.model.user.User;

/**
 * Temporary authentication object needed to load the user details service.
 *
 * @author Scott Battaglia
 * @since 3.0
 */
public final class CasAssertionAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final Assertion assertion;

    private final String ticket;
    
	//用户注册信息 @update
    private User user;

    public CasAssertionAuthenticationToken(final Assertion assertion, final String ticket) {
        super(new ArrayList<GrantedAuthority>());

        this.assertion = assertion;
        this.ticket = ticket;
    }
    // @update
    public CasAssertionAuthenticationToken(final Assertion assertion, final String ticket,
    		User user) {
        super(new ArrayList<GrantedAuthority>());
        this.assertion = assertion;
        this.ticket = ticket;
        this.user = user;
    }
    
    public Object getPrincipal() {
        return this.assertion.getPrincipal().getName();
    }

    public Object getCredentials() {
        return this.ticket;
    }

    public Assertion getAssertion() {
        return this.assertion;
    }

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}