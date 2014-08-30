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

package org.springframework.security.core.userdetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

import com.baodian.model.user.User_Role;
import com.baodian.service.role.SecuManager;
import com.baodian.service.util.StaticData;


/**
 * Models core user information retrieved by a {@link UserDetailsService}.
 * <p>
 * Developers may use this class directly, subclass it, or write their own {@link UserDetails} implementation from
 * scratch.
 * <p>
 * {@code equals} and {@code hashcode} implementations are based on the {@code username} property only, as the
 * intention is that lookups of the same user principal object (in a user registry, for example) will match
 * where the objects represent the same user, not just when all the properties (authorities, password for
 * example) are the same.
 * <p>
 * Note that this implementation is not immutable. It implements the {@code CredentialsContainer} interface, in order
 * to allow the password to be erased after authentication. This may cause side-effects if you are storing instances
 * in-memory and reusing them. If so, make sure you return a copy from your {@code UserDetailsService} each time it is
 * invoked.
 *
 * @author Ben Alex
 * @author Luke Taylor
 */
public class User implements UserDetails, CredentialsContainer {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    //~ Instance fields ================================================================================================
    private int[] id;
    private String[] str;
    private String password;
    private final String username;
    private Set<GrantedAuthority> authorities;
    private Set<Integer> userMenu;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    //~ Constructors ===================================================================================================

    /**
     * Calls the more complex constructor with all boolean arguments set to {@code true}.
     */
    public User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this(username);
    }

    /**
     * Construct the <code>User</code> with the details required by
     * {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider}.
     *
     * @param username the username presented to the
     *        <code>DaoAuthenticationProvider</code>
     * @param password the password that should be presented to the
     *        <code>DaoAuthenticationProvider</code>
     * @param enabled set to <code>true</code> if the user is enabled
     * @param accountNonExpired set to <code>true</code> if the account has not
     *        expired
     * @param credentialsNonExpired set to <code>true</code> if the credentials
     *        have not expired
     * @param accountNonLocked set to <code>true</code> if the account is not
     *        locked
     * @param authorities the authorities that should be granted to the caller
     *        if they presented the correct username and password and the user
     *        is enabled. Not null.
     *
     * @throws IllegalArgumentException if a <code>null</code> value was passed
     *         either as a parameter or as an element in the
     *         <code>GrantedAuthority</code> collection
     */
    public User(String username) {

        if ((username == null) || "".equals(username)) {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
        }
        
        this.username = username;
        this.password = "";
        this.enabled = true;
        this.accountNonExpired = true;
        this.credentialsNonExpired = true;
        this.accountNonLocked = true;
    }

    //~ Methods ========================================================================================================

    public Collection<GrantedAuthority> getAuthorities() {
    	if(this.authorities == null) {//延迟读取用户角色等信息
    		Set<GrantedAuthority> auths = new HashSet<GrantedAuthority>();
    		this.userMenu = new HashSet<Integer>();
    		SecuManager sm = (SecuManager) StaticData.context.getBean("secuManager");
			com.baodian.model.user.User user = sm.findUserR_ByU_a(username);
			int did = user.getDpm().getId();
			//用户id
			this.id = new int[] {user.getId(), did};
			//用户真实姓名、头像
			this.str = new String[] {user.getName(), user.getHimage()};
			//用户角色
	        Iterator<User_Role> user_Roles = user.getUser_roles().iterator();
	        String rids = "";
	        while(user_Roles.hasNext()) {
	        	String rId = String.valueOf(user_Roles.next().getRole().getId());
	        	rids = rids.concat(rId + ",");
	        	auths.add(new SimpleGrantedAuthority(rId));
	        }
	        if(!rids.isEmpty()) {
	        	userMenu.addAll(sm.findMenuByRIds(rids.substring(0, rids.length()-1)));
	        }
	        this.authorities = Collections.unmodifiableSet(sortAuthorities(auths));
	        /*System.out.println("----延迟读取用户角色------");
	        Iterator<GrantedAuthority> authes = this.authorities.iterator();
	        System.out.print("-[ "+username+" ] -> ");
	        while(authes.hasNext()) {
	        	System.out.print("_"+authes.next().getAuthority());
	        }
	        Iterator<Integer> menus = this.userMenu.iterator();
	        System.out.print(" -> menu -> ");
	        while(menus.hasNext()) {
	        	System.out.print("_"+menus.next());
	        }
	        System.out.println();*/
		}
        return authorities;
    }
    /**
     * 用户的权限菜单id
     * @return
     */
    public Set<Integer> getUserMenu() {
    	return userMenu;
    }
    public static long getSerialversionuid() {
		return serialVersionUID;
	}
    /**
     * 用户id等等
     * @return 0->id 1->dpmId
     */
	public int[] getId() {
		return id;
	}
	/**
	 * 用户姓名等等
	 * @return 0->真实姓名. 1->头像
	 */
	public String[] getStr() {
		return str;
	}
	public String getPassword() {
        return password;
    }
	/**
	 * 用户账号
	 */
    public String getUsername() {
        return username;
    }
    /**
     * 部门
     */
    public String getDepartment() {
    	return StaticData.depts.get(id[1]).getName();
    }
	public boolean isEnabled() {
        return enabled;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void eraseCredentials() {
        password = null;
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        // Ensure array iteration order is predictable (as per UserDetails.getAuthorities() contract and SEC-717)
        SortedSet<GrantedAuthority> sortedAuthorities =
            new TreeSet<GrantedAuthority>(new AuthorityComparator());

        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }

        return sortedAuthorities;
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {
        private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            // Neither should ever be null as each entry is checked before adding it to the set.
            // If the authority is null, it is a custom authority and should precede others.
            if (g2.getAuthority() == null) {
                return -1;
            }

            if (g1.getAuthority() == null) {
                return 1;
            }

            return g1.getAuthority().compareTo(g2.getAuthority());
        }
    }

    /**
     * Returns {@code true} if the supplied object is a {@code User} instance with the
     * same {@code username} value.
     * <p>
     * In other words, the objects are equal if they have the same username, representing the
     * same principal.
     */
    @Override
    public boolean equals(Object rhs) {
        if (rhs instanceof User) {
            return username.equals(((User) rhs).username);
        }
        return false;
    }

    /**
     * Returns the hashcode of the {@code username}.
     */
    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(": ");
        sb.append("Username: ").append(this.username).append("; ");
        sb.append("Password: [PROTECTED]; ");
        sb.append("Enabled: ").append(this.enabled).append("; ");
        sb.append("AccountNonExpired: ").append(this.accountNonExpired).append("; ");
        sb.append("credentialsNonExpired: ").append(this.credentialsNonExpired).append("; ");
        sb.append("AccountNonLocked: ").append(this.accountNonLocked).append("; ");

        if (!authorities.isEmpty()) {
            sb.append("Granted Authorities: ");

            boolean first = true;
            for (GrantedAuthority auth : authorities) {
                if (!first) {
                    sb.append(",");
                }
                first = false;

                sb.append(auth);
            }
        } else {
            sb.append("Not granted any authorities");
        }

        return sb.toString();
    }
}
