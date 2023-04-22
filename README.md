Ktor Server Android
======================================================

This project includes a Netty server configuration for a Ktor server in Android platform.
This is only a template for a server inside an android app, with plugins that are not out of the
box, such as CallLogging, Authentication and SSL Connector.

Getting Started
---------------

To get started with this project, follow these steps:

1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Build and run the project.

Server Configuration
--------------------

The Netty server is configured in the `Environment` object in the `Environment.kt` file. The server
is configured with several plugins, including CallLogging, Authentication, and RateLimit.

### CallLogging

The CallLogging plugin is used to log incoming requests and outgoing responses. This can be useful
for debugging and troubleshooting issues with the server. CallLogging is installed and configured in
the `install(CallLogging)` block within the `module` block of the `Environment` object.

### Authentication

The Authentication plugin is used to authenticate incoming requests from clients. This can be useful
for ensuring that only authorized users can access certain endpoints on the server. Authentication
is installed and configured in the `install(Authentication)` block within the `module` block of
the `Environment` object. Two types of JWT authentication are configured: RS256 and HS256.

### RateLimit

The RateLimit plugin is used to limit the number of requests that can be made to the server within a
specified time period. This can be useful for preventing excessive traffic or abuse of the server.
RateLimit is installed and configured in the `install(RateLimit)` block within the `module` block of
the `Environment` object.

### SSL Connector

The SSLConnector plugin is used to enable HTTPS connections to the server. This provides an
additional layer of security for communication between clients and the server. SSLConnector is not
enabled in the current configuration, but you can uncomment the code block in the `connector` block
to enable it.

Endpoints
---------

This server includes example for several endpoints that can be accessed by clients. These endpoints
are
defined in the `routing` block within the `module` block of the `Environment` object and include:

- `/hello`: Returns a simple "Hello, world!" message.
- `/helloAuth`: Requires authentication and returns a message indicating that the request was
  successful.
- `.well-known/keys.json`: This route serves a JSON file that is relevant only for the
  authentication of RS256. The file contains public keys that can be used to verify the JWT token
  signature. However, it is important to note that this specific implementation will not work on
  devices running API level 30 or higher, as external storage access is restricted in these versions
  of Android. Therefore, you may need to consider other options for storing and accessing this file,
  such as storing it in the app's internal storage or in a cloud-based storage service.
