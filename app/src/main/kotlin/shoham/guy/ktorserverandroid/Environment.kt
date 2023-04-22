package shoham.guy.ktorserverandroid

import android.os.Environment
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.http.content.file
import io.ktor.server.http.content.static
import io.ktor.server.http.content.staticRootFolder
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.callloging.processingTimeMillis
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.util.date.getTimeMillis
import org.slf4j.event.Level
import org.slf4j.impl.StaticLoggerBinder
import java.io.File
import kotlin.time.Duration.Companion.seconds

object Environment {
    private var clock: () -> Long = { getTimeMillis() }
    private val environment = applicationEngineEnvironment {
        connector {
            port = 8080
        }

        /*  sslConnector(
              keyStore = SSLCredentials.getKeyStore(),
              keyAlias = SSLCredentials.getKeyAlias(),
              keyStorePassword = { SSLCredentials.getKeyPassword().toCharArray() },
              privateKeyPassword = { SSLCredentials.getAliasPassword().toCharArray() }
          ) { port = 8081 }*/

        module {
            install(CallLogging) {
                level = Level.INFO
                logger = StaticLoggerBinder.getSingleton().loggerFactory.getLogger(
                    MyApplication.INSTANCE.resources.getString(R.string.app_name)
                )
                format { call ->
                    "${call.response.status()}: ${call.request.httpMethod.value} - ${call.request.path()} in ${
                        call.processingTimeMillis(
                            clock
                        )
                    }ms"
                }
            }

            install(Authentication) {
                jwt("RS256") {
                    verifier("0.0.0.0:8080")
                    validate { jwtCredential -> JWTPrincipal(jwtCredential.payload) }
                }
                jwt("HS256") {
                    verifier(
                        JWT.require(Algorithm.HMAC256("secret"))
                            .build()
                    )
                    validate { jwtCredential -> JWTPrincipal(jwtCredential.payload) }
                }
            }

            install(RateLimit) {
                global { rateLimiter(limit = 5, refillPeriod = 1.seconds) }
            }



            routing {
                get("/hello") {
                    call.respond("Hello World!")
                }

                authenticate("RS256", "HS256") {
                    get("/helloAuth") {
                        call.respond("Hello World with Authentication!")
                    }
                }

                static(".well-known") {
                    staticRootFolder = File(Environment.getExternalStorageDirectory().absolutePath)
                    val fileName = "keys.json"
                    val file = File("$staticRootFolder/$fileName")
                    if (file.exists()) {
                        file(fileName)
                    }
                }
            }
        }
    }

    fun getEnvironment() = environment
}
