package pt.uc.dei.servlets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import pt.uc.dei.implement.UserImpl;
import pt.uc.dei.model.User;

/**
 * Servlet implementation class GoogleAuthentication
 */
@WebServlet("/GoogleAuthentication")
public class GoogleAuthentication extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger.getLogger(GoogleAuthentication.class);

	@Inject
	private UserImpl userImpl;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GoogleAuthentication() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	// (Receive authCode via HTTPS POST)
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getHeader("X-Requested-With") == null) {
			// Without the `X-Requested-With` header, this request could be
			// forged. Aborts.
		}

		System.out.println(request.getContentType());

		String authCode = getBody(request);
		logger.debug("authCode: " + authCode);

		// Set path to the Web application client_secret_*.json file you
		// downloaded from
		// the
		// Google API Console:
		// https://console.developers.google.com/apis/credentials
		// You can also find your Web application client ID and client secret
		// from the
		// console and specify them directly when you create the
		// GoogleAuthorizationCodeTokenRequest
		// object.
		String CLIENT_SECRET_FILE = getServletContext()
				.getRealPath("/WEB-INF/classes/google/authentication/client_secret.json");

		// Exchange auth code for access token
		GoogleClientSecrets clientSecrets;
		try {
			clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(),
					new FileReader(CLIENT_SECRET_FILE));

			String REDIRECT_URI = "http://localhost:8080";

			logger.debug("REDIRECT_URI: " + REDIRECT_URI);
			GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(),
					JacksonFactory.getDefaultInstance(), "https://www.googleapis.com/oauth2/v4/token",
					clientSecrets.getDetails().getClientId(), clientSecrets.getDetails().getClientSecret(), authCode,
					REDIRECT_URI) // Specify the same redirect URI that you use
									// with your web
									// app. If you don't have a web version of
									// your app, you can
									// specify an empty string.
							.execute();

			String accessToken = tokenResponse.getAccessToken();

			// // Use access token to call API
			// GoogleCredential credential = new
			// GoogleCredential().setAccessToken(accessToken);
			// Drive drive = new Drive.Builder(new NetHttpTransport(),
			// JacksonFactory.getDefaultInstance(), credential)
			// .setApplicationName("Auth Code Exchange Demo").build();
			// File file = drive.files().get("appfolder").execute();

			// Get profile info from ID token
			GoogleIdToken idToken = tokenResponse.parseIdToken();
			GoogleIdToken.Payload payload = idToken.getPayload();
			String userId = payload.getSubject(); // Use this value as a key to
													// identify a user.
			String email = payload.getEmail();
			boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
			String name = (String) payload.get("name");
			String pictureUrl = (String) payload.get("picture");
			String locale = (String) payload.get("locale");
			String familyName = (String) payload.get("family_name");
			String givenName = (String) payload.get("given_name");

			User userLogged = userImpl.isRegistered(email, null, true);
			if (userLogged != null) {
				if (userLogged.getIsactive()) {
					logger.info("LOGIN USER: " + userLogged.getEmail());
					logger.warn("LOGIN USER: " + userLogged.getEmail());
					response.getWriter().append("pages/beginArea.xhtml?faces-redirect=true");
				} else {
					response.getWriter().append("pages/visitorArea.xhtml?faces-redirect=true");
				}
			} else {
				response.getWriter().append("");
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.getWriter().append("");
		}
	}

	public static String getBody(HttpServletRequest request) throws IOException {

		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}

		body = stringBuilder.toString();
		return body;
	}

}
