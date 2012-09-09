package yourcv.xing;

public class Reste {
//	package yourcv.xing;
//
//	import java.net.MalformedURLException;
//	import java.net.URL;
//	import java.net.URLEncoder;
//
//	import com.google.appengine.api.urlfetch.HTTPMethod;
//	import com.google.appengine.api.urlfetch.HTTPRequest;
//	import com.google.appengine.api.urlfetch.HTTPResponse;
//	import com.google.appengine.api.urlfetch.URLFetchService;
//	import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
//	import com.google.appengine.api.users.User;
//	import com.google.appengine.api.users.UserService;
//	import com.google.appengine.api.users.UserServiceFactory;
//
//	public class XingOAuth {
//		public static void getAuthRequestToken() {
//			URLFetchService urlfetcher = URLFetchServiceFactory
//					.getURLFetchService();
//			try {
//				UserService userService = UserServiceFactory.getUserService();
//				User user = userService.getCurrentUser();
//				UserPrefs userPrefs = UserPrefs.getPrefsForUser(user);
//				String signature;
//				signature = XingConfig._CONSUMERSECRET + "%26";
//
//				HTTPRequest frage = new HTTPRequest(new URL(
//						XingConfig._BASEURL+"request_token"), HTTPMethod.POST);
//				frage.setPayload(("oauth_consumer_key=" + XingConfig._CONSUMERKEY
//						+ "&oauth_callback="
//						+ URLEncoder.encode(XingConfig._CALLBACKURL, "UTF-8")
//						+ "&oauth_signature_method="+XingConfig._SIGNATUREMETHOD+"&oauth_signature=" + signature)
//						.getBytes());
//				HTTPResponse antwort = urlfetcher.fetch(frage);
//				if (antwort.getResponseCode() != 201) {
//					byte[] antwortstring = antwort.getContent();
//					System.out.print("RequestToken" + new String(antwortstring));
//				} else {
//
//					try {
//						byte[] antwortstring = antwort.getContent();
//
//						String[] tokens = new String(antwortstring).split("&");
//						for (String s : tokens) {
//							String[] keyvaluepair = s.split("=");
//							if (keyvaluepair[0].equalsIgnoreCase("oauth_token")) {
//								userPrefs.setOAuthReqToken(keyvaluepair[1]);
//							} else if (keyvaluepair[0]
//									.equalsIgnoreCase("oauth_token_secret")) {
//								userPrefs
//										.setOAuthRequestTokenSecret(keyvaluepair[1]);
//							}
//						}
//						userPrefs.save();
//					} catch (NumberFormatException nfe) {
//
//					}
//
//				}
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//
//		public static void getAccessToken() {
//			URLFetchService urlfetcher = URLFetchServiceFactory
//					.getURLFetchService();
//
//			UserService userService = UserServiceFactory.getUserService();
//			User user = userService.getCurrentUser();
//			UserPrefs userPrefs = UserPrefs.getPrefsForUser(user);
//			HTTPRequest frage;
//			try {
//				frage = new HTTPRequest(new URL(
//						XingConfig._BASEURL+"access_token"), HTTPMethod.POST);
//				String signature = XingConfig._CONSUMERSECRET + "%26"
//						+ userPrefs.getOAuthRequestTokenSecret();
//				String anfragestring = ("oauth_consumer_key="
//						+ XingConfig._CONSUMERKEY
//						+ "&oauth_signature_method="+XingConfig._SIGNATUREMETHOD+"" + "&oauth_token="
//						+ userPrefs.getOAuthReqToken() + "&oauth_verifier="
//						+ userPrefs.getOAuthReqTokenVerifier()
//						+ "&oauth_signature=" + signature);
//				frage.setPayload(anfragestring.getBytes());
//				System.out.print(anfragestring);
//				HTTPResponse antwort = urlfetcher.fetch(frage);
//				if (antwort.getResponseCode() != 201) {
//					System.out.print("AccessToken"
//							+ new String(antwort.getContent()));
//				} else {
//					try {
//						byte[] antwortstring = antwort.getContent();
//
//						String[] tokens = new String(antwortstring).split("&");
//						for (String s : tokens) {
//							String[] keyvaluepair = s.split("=");
//							if (keyvaluepair[0].equalsIgnoreCase("oauth_token")) {
//								userPrefs.setOAuthToken(keyvaluepair[1]);
//							} else if (keyvaluepair[0]
//									.equalsIgnoreCase("oauth_token_secret")) {
//								userPrefs
//										.setOAuthAccessTokenSecret(keyvaluepair[1]);
//							} else if (keyvaluepair[0].equalsIgnoreCase("user_id")) {
//								userPrefs.setXingUserID(keyvaluepair[1]);
//							}
//						}
//						userPrefs.save();
//					} catch (NumberFormatException nfe) {
//
//					}
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
//	package yourcv.xing;
//
//	import java.io.IOException;
//	import java.io.PrintWriter;
//	import java.net.URL;
//
//	import javax.servlet.http.HttpServlet;
//	import javax.servlet.http.HttpServletRequest;
//	import javax.servlet.http.HttpServletResponse;
//
//	import com.google.appengine.api.urlfetch.HTTPResponse;
//	import com.google.appengine.api.urlfetch.URLFetchService;
//	import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
//	import com.google.appengine.api.users.User;
//	import com.google.appengine.api.users.UserService;
//	import com.google.appengine.api.users.UserServiceFactory;
//
//	@SuppressWarnings("serial")
//	public class XingServlet extends HttpServlet {
//		public void doGet(HttpServletRequest req, HttpServletResponse resp)
//				throws IOException {
//
//			UserService UserService = UserServiceFactory.getUserService();
//			User user = UserService.getCurrentUser();
//			String navbar = "";
//
//			if (user == null) {
//				navbar = "<p>Welcome, you can <a href='"
//						+ UserService.createLoginURL("/") + "'>sign in</a>.</p>";
//
//			} else {
//				UserPrefs userPrefs = UserPrefs.getPrefsForUser(user);
//				if (userPrefs.getOAuthReqToken() == null) {
//					XingOAuth.getAuthRequestToken();
//					resp.sendRedirect("/");
//					// OAuth 1.0 STEP 1
//
//				} else {
//					if (userPrefs.getOAuthToken() == null
//							&& userPrefs.getOAuthReqTokenVerifier() == null) {
//						String parameter = req.getParameter("oauth_verifier");
//						if (parameter == null) {
//							resp.sendRedirect(XingConfig._BASEURL
//									+ "authorize?oauth_token="
//									+ userPrefs.getOAuthReqToken());
//							// OAuth 1.0 STEP 2
//						} else {
//							userPrefs.setOAuthReqTokenVerifier(parameter);
//							userPrefs.save();
//							XingOAuth.getAccessToken();
//							resp.sendRedirect("/");
//							// OAuth 1.0 STEP 3
//						}
//					}
//
//				}
//
//				if (userPrefs.getOAuthToken() != null) {
//					// User Authentifiziert!
//					URLFetchService fetcher = URLFetchServiceFactory
//							.getURLFetchService();
//					HTTPResponse antwort = fetcher.fetch(new URL(
//							XingConfig._BASEURL + "users/me?oauth_consumer_key="
//									+ XingConfig._CONSUMERKEY + "&oauth_token="
//									+ userPrefs.getOAuthToken()
//									+ "&oauth_signature_method="
//									+ XingConfig._SIGNATUREMETHOD
//									+ "&oauth_signature="
//									+ XingConfig._CONSUMERSECRET + "%26"
//									+ userPrefs.getOAuthAccessTokenSecret()));
//					navbar += new String(antwort.getContent());
//					antwort = fetcher.fetch(new URL(XingConfig._BASEURL
//							+ "users/65029_30fc24"));
//					navbar += new String(antwort.getContent());
//
//				}
//
//				navbar += "<p>Welcome " + user.getEmail() + ", you can <a href='"
//						+ UserService.createLogoutURL("/") + "'>sign out</a>.";
//
//				navbar += "</p>";
//
//			}
//			resp.setContentType("text/html");
//			PrintWriter out = resp.getWriter();
//
//			out.println(navbar);
//
//		}
//	}
//	/*
//	 * different estimate strategies for effort (decision by standardverteilung)
//	 * -site number based 23, 24, 60, 27 -> split up 60 to 30,30 --> have 5 lecture
//	 * parts -topic based chapter1 chapter2 chapter3.1 chapter 3.2 --> stay that way
//	 * -progress based 30% --> 30% 30% 10%/ 35% 35% -user based --> own estimates
//	 * -hour based --> how many wochenstunden needs several views: -detail view
//	 * -mindmap view -learning view -comment view ticketing by estimate? app could
//	 * "buy" from good sources only see good documents? only see baad documents? see
//	 * parts of good? see everything? you say the course -we check for course,
//	 * -provide prof, lectures -other similar courses (more detailed notes --> need
//	 * rating) prof can have aliases
//	 */

}
