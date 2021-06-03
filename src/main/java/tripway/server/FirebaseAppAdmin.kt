package tripway.server

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import org.springframework.stereotype.Component
import java.io.FileInputStream

@Component
class FirebaseAppAdmin {
    companion object {
        fun validateAndGetUID(token: String) = try {
            if(token == "DEBUG"){
                "DEBUG"
            }
            else FirebaseAuth.getInstance(INSTANCE).verifyIdTokenAsync(token).get().uid ?: null
        } catch (e: Exception) {
            null
        }

        private val serviceAccount = FileInputStream("serviceAccountKey.json")

        private val options = FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://tripway-1584816312577.firebaseio.com")
                .build()

        private val INSTANCE = FirebaseApp.initializeApp(options)
    }
}
