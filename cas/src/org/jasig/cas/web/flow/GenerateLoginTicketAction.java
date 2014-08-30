/*
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.cas.web.flow;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.util.UniqueTicketIdGenerator;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.webflow.execution.RequestContext;

import com.baodian.util.rsa.RSACoder;


/**
 * Generates the login ticket parameter as described in section 3.5 of the
 * <a href="http://www.jasig.org/cas/protocol">CAS protocol</a>.
 *
 * @author Marvin S. Addison
 * @version $Revision$ $Date$
 * @since 3.4.9
 *
 */
public class GenerateLoginTicketAction {
    /** 3.5.1 - Login tickets SHOULD begin with characters "LT-" */
    private static final String PREFIX = "LT";

    /** Logger instance */
    private final Log logger = LogFactory.getLog(getClass());


    @NotNull
    private UniqueTicketIdGenerator ticketIdGenerator;

    public final String generate(final RequestContext context) {
    	//如果是检查登录状态，或者重新验证但失败，那么返回json数据 @update
        if(context.getRequestParameters().get("j_ajax_check") != null ||
        		context.getRequestParameters().get("j_ajax_validate") != null) {
        	System.out.println("Join to notLogin!");
        	return "loginCheck";
        }
        final String loginTicket = this.ticketIdGenerator.getNewTicketId(PREFIX);
        this.logger.debug("Generated login ticket " + loginTicket);
        WebUtils.putLoginTicket(context, loginTicket);
        //保存rsa密钥到flow @update
        KeyPair keyPair = RSACoder.initKey();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        context.getFlowScope().put("rsa_priKey", privateKey);
        context.getFlowScope().put("rsa_pubKey", publicKey.getPublicExponent().toString(16));
        context.getFlowScope().put("rsa_modKey", publicKey.getModulus().toString(16));
        //如果参数包含j_ajax，那么返回json数据 @update
        if(context.getRequestParameters().get("j_ajax") != null) {
        	System.out.println("Join to rsa!");
        	return "rsaCreate";
        }
        return "generated";
    }

    public void setTicketIdGenerator(final UniqueTicketIdGenerator generator) {
        this.ticketIdGenerator = generator;
    }
}
