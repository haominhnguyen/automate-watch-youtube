package com.service.impl;

import com.service.ChannelYoutubeService;
import org.springframework.stereotype.Service;


/**
 * Created by haotr on 01/12/2020.
 */

@Service
public class ChannelYoutubeServiceImpl implements ChannelYoutubeService {

//    private static final String CLIENT_SECRETS= "client_secret.json";
//    private static final Collection<String> SCOPES =
//            Arrays.asList("https://www.googleapis.com/auth/youtube.readonly");
//
//    private static final String APPLICATION_NAME = "API code samples";
//    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
//
//    /**
//     * Create an authorized Credential object.
//     *
//     * @return an authorized Credential object.
//     * @throws IOException
//     */
//    public static Credential authorize(final NetHttpTransport httpTransport) throws IOException {
//        // Load client secrets.
//        InputStream in = ApiExample.class.getResourceAsStream(CLIENT_SECRETS);
//        GoogleClientSecrets clientSecrets =
//                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
//        // Build flow and trigger user authorization request.
//        GoogleAuthorizationCodeFlow flow =
//                new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
//                        .build();
//        Credential credential =
//                new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
//        return credential;
//    }
//
//    /**
//     * Build and return an authorized API client service.
//     *
//     * @return an authorized API client service
//     * @throws GeneralSecurityException, IOException
//     */
//    public static YouTube getService() throws GeneralSecurityException, IOException {
//        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//        Credential credential = authorize(httpTransport);
//        return new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
//                .setApplicationName(APPLICATION_NAME)
//                .build();
//    }

}
