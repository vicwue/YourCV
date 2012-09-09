package yourcv.xing;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceException;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.User;

@SuppressWarnings("serial")
@Entity(name = "UserPrefs")
public class UserPrefs implements Serializable {
	@Transient
	private static Logger logger = Logger.getLogger(UserPrefs.class.getName());
	@Id
	private String Userid;

	private String OAuthToken = null;

	private String OAuthReqToken = null;

	private String OAuthReqTokenVerifier = null;

	private String XingUserID;

	public String getOAuthReqTokenVerifier() {
		return OAuthReqTokenVerifier;
	}

	public void setOAuthReqTokenVerifier(String oAuthReqTokenVerifier) {
		OAuthReqTokenVerifier = oAuthReqTokenVerifier;
	}

	public String getOAuthReqToken() {
		return OAuthReqToken;
	}

	public void setOAuthReqToken(String oAuthReqToken) {
		OAuthReqToken = oAuthReqToken;
	}

	private String OAuthRequestTokenSecret;
	private String OAuthAccessTokenSecret;

	public String getOAuthToken() {
		return OAuthToken;
	}

	public void setOAuthToken(String oAuthToken) {
		OAuthToken = oAuthToken;
	}

	public String getXingUserID() {
		return XingUserID;
	}

	public void setXingUserID(String xingUserID) {
		XingUserID = xingUserID;
	}

	public String getOAuthRequestTokenSecret() {
		return OAuthRequestTokenSecret;
	}

	public void setOAuthRequestTokenSecret(String oAuthRequestTokenSecret) {
		OAuthRequestTokenSecret = oAuthRequestTokenSecret;
	}

	public String getOAuthAccessTokenSecret() {
		return OAuthAccessTokenSecret;
	}

	public void setOAuthAccessTokenSecret(String oAuthAccessTokenSecret) {
		OAuthAccessTokenSecret = oAuthAccessTokenSecret;
	}

	@Basic
	private User user;

	public UserPrefs(String userId) {
		this.Userid = userId;
	}

	public String getUserId() {
		return Userid;
	}

	public String getUserid() {
		return Userid;
	}

	public void setUserid(String userid) {
		Userid = userid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@SuppressWarnings("unchecked")
	public static UserPrefs getPrefsForUser(User myUser) {
		UserPrefs userPrefs = null;
		String cacheKey = "UserPrefs:" + myUser.getUserId();
		try {
			MemcacheService memcache = MemcacheServiceFactory
					.getMemcacheService();
			if (memcache.contains(cacheKey)) {

				logger.warning("CACHE HIT");
				// userPrefs = (UserPrefs) memcache.get(cacheKey);
				// return userPrefs;
			}
			logger.warning("CACHE MISS");
			// If the UserPrefs object isn't in memcache,
			// fall through to the datastore.
		} catch (MemcacheServiceException e) {
			// If there is a problem with the cache,
			// fall through to the datastore.
		}
		EntityManager em = EMF.get().createEntityManager();
		try {
			userPrefs = em.find(UserPrefs.class, myUser.getUserId());
			if (userPrefs == null) {
				userPrefs = new UserPrefs(myUser.getUserId());
				userPrefs.setUser(myUser);
			} else {
				try {
					MemcacheService memcache = MemcacheServiceFactory
							.getMemcacheService();
					memcache.put(cacheKey, userPrefs);
				} catch (MemcacheServiceException e) {
					// Ignore cache problems, nothing we can do.
				}
			}
		} finally {
			em.close();
		}
		return userPrefs;
	}

	public void save() {
		EntityManager em = EMF.get().createEntityManager();
		try {
			em.persist(this);

		} catch (Exception e) {
			em.merge(this);
		} finally {
		
			em.close();
		}
	}

}
