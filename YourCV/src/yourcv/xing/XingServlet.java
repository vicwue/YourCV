package yourcv.xing;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yourcv.model.UserPrefs;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;

@SuppressWarnings("serial")
public class XingServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		UserService UserService = UserServiceFactory.getUserService();
		User user = UserService.getCurrentUser();
		 String navbar = "";
		if (user == null) {
			 navbar = "<p>Welcome, you can <a href='"
			 + UserService.createLoginURL("/") + "'>sign in</a>.</p>";
		} else {
			UserPrefs userPrefs = UserPrefs.getPrefsForUser(user);
			if (userPrefs.getOAuthReqToken() == null) {
				XingOAuth.getAuthRequestToken();
				resp.sendRedirect("/XingAuth");
				// OAuth 1.0 STEP 1
			} else {
				if (userPrefs.getOAuthToken() == null
						&& userPrefs.getOAuthReqTokenVerifier() == null) {
					String parameter = req.getParameter("oauth_verifier");
					if (parameter == null) {
						resp.sendRedirect(XingConfig._BASEURL
								+ "authorize?oauth_token="
								+ userPrefs.getOAuthReqToken());
						// OAuth 1.0 STEP 2
					} else {
						userPrefs.setOAuthReqTokenVerifier(parameter);
						userPrefs.save();
						XingOAuth.getAccessToken();
						resp.sendRedirect("/XingAuth");
						// OAuth 1.0 STEP 3
					}
				}
			}
			if (userPrefs.getOAuthToken() != null) {
				// User Authentifiziert!
				Gson gson = new Gson();
				out.print(gson.toJson(userPrefs));
			}
		}
		out.print(navbar);
	}
}
