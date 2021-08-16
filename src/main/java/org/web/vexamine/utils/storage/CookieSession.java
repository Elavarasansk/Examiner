package org.web.vexamine.utils.storage;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Instantiates a new cookie session.
 *
 * @param format the format
 * @param time the time
 * @param webToken the web token
 * @param sessionId the session id
 * @param userName the user name
 * @param userRole the user role
 * @param remoteIp the remote ip
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CookieSession {

    /**
     * Instantiates a new cookie session.
     *
     * @param cookie the cookie
     */
    public CookieSession(CookieSession cookie) {
        this.time = format.format(new Date());
        this.webToken = cookie.getWebToken();
        this.sessionId = cookie.getSessionId();
        this.userName = cookie.getUserName();
        this.userRole = cookie.getUserRole();
        this.remoteIp = cookie.getRemoteIp();
    }

    /**
     * Gets the format.
     *
     * @return the format
     */
    @Getter(AccessLevel.PRIVATE)
    
    /**
     * Sets the format.
     *
     * @param format the new format
     */
    @Setter(AccessLevel.PRIVATE)
    private SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS z");

    /** The time. */
    private String time = format.format(new Date());

    /** The web token. */
    private String webToken;
    //private String msaToken;
    /** The session id. */
    /*authentication*/
    private String sessionId;
    
    /** The user name. */
    private String userName;
    
    /** The user role. */
    private String userRole;
    
    /** The remote ip. */
    private String remoteIp;

}
