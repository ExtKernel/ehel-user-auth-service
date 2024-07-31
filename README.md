# Ebay User Authentication/Authorization service
A service to handle authentication and authorization in the context of the eBay Developer API. This service helps to build an abstraction on eBay's API, which might be misleading sometimes. 

## Endpoints:

### eBay User Endpoints

**Base path:** `/user`

#### Save a new eBay user
- **Method:** `POST`
- **Path:** `/user`
- **Request body example:**
    ```json
    {
        "username": "johndoe",
        "clientId": "your-ebay-client-id",
        "clientSecret": "your-ebay-client-secret"
    }
    ```

#### Generate a refresh token for a user
- **Method:** `POST`
- **Path:** `/user/refresh-token/generate/{userId}`
- **Example:** `/user/refresh-token/generate/123`

#### Save a refresh token for a user
- **Method:** `POST`
- **Path:** `/user/refresh-token/save/{userId}`
- **Example:** `/user/refresh-token/save/123`
- **Request body example:**
    ```json
    {
        "token": "your-refresh-token",
        "expiresIn": 3600
    }
    ```

#### Find an eBay user by ID
*Note: This endpoint operates within this service and only accesses the service's database, not the eBay Developer API.*
- **Method:** `GET`
- **Path:** `/user/{userId}`
- **Example:** `/user/123`

#### Delete an eBay user by ID
- Method: `DELETE`
- Path: `/user/{userId}`
- Example: `/user/123`

### Auth Code Endpoints
Base path: `/auth-code`

#### Save a new auth code
You can enter this endpoint in the "Your auth accepted" field on the "User Tokens" page of your eBay Developer account.
- Method: `GET`
- Path: `/auth-code`
- Query parameters:
    - `code` (required)
    - `expires_in` (required)
- Example: `/auth-code?code=your-auth-code&expires_in=3600`

#### Find the latest auth code
- Method: `GET`
- Path: `/auth-code/latest`

## Environment Variables

The following environment variables are used to configure the Product Retriever Service. They should be set in your environment or configuration files to ensure proper operation.

### eBay API URLs
- **EBAY_BROWSE_API_ITEM_SUMMARY_URL**
    - This environment variable specifies the URL for the eBay Browse API's item summary endpoint.

- **EBAY_BROWSE_API_GET_ITEM_URL**
    - This variable defines the URL for the eBay Browse API's get item endpoint.

### Token Retrieval
- **ACCESS_TOKEN_RETRIEVE_URI**
    - URL used to retrieve the access token from eBay’s OAuth2 system.

- **REFRESH_TOKEN_RETRIEVE_URI**
    - URL used to retrieve the refresh token from eBay’s OAuth2 system.

### Security and Roles
- **PRINCIPAL_ROLE_NAME**
  - Specifies the role name for the principal user in the application. The user for whom you will obtain an access token from your OAuth2 provider should have this role.

### Service Discovery
- **EUREKA_URI**
    - The URL for the Eureka service registry. Default: `http://localhost:8761/eureka`.

### Server Configuration
- **SERVER_PORT**
    - The port on which the application server will run. Default: `8000`.

### Spring Cloud and Security
- **OAUTH2_PROVIDER_ISSUER_URL**
    - The issuer URI for the OAuth2 provider.

- **OAUTH2_PROVIDER_CLIENT_ID**
    - Client ID for the OAuth2 provider's opaque token configuration.

- **OAUTH2_PROVIDER_CLIENT_SECRET**
    - Client secret for the OAuth2 provider's opaque token configuration.

- **OAUTH2_PROVIDER_INTROSPECTION_URL**
    - URL for the introspection endpoint of the OAuth2 provider.

### Additional Configuration
- **LIVERELOAD_PORT**
    - Port used for live reload functionality. Default: `35730`.

Ensure these variables are properly set to avoid runtime issues.