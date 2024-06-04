# Keycloak custom client REST endpoint

```shell

podman compose down && podman compose up

KC_SERVER=http://localhost:8180
KC_REALM=master
KC_ISSUER=$KC_SERVER/realms/$KC_REALM
KC_USERNAME=admin
KC_PASSWORD=admin
KC_CLIENT_ID=admin-cli

KC_RESPONSE=$( curl \
-d "client_id=$KC_CLIENT_ID" \
-d "username=$KC_USERNAME" \
-d "password=$KC_PASSWORD" \
-d "grant_type=password" \
-d "scope=profile roles" \
"$KC_ISSUER/protocol/openid-connect/token")

KC_ACCESS_TOKEN=$(echo $KC_RESPONSE | jq -r .access_token)

echo $KC_ACCESS_TOKEN

KC_CLIENT_RESPONSE=$( \
curl -v \
-H "Authorization: Bearer $KC_ACCESS_TOKEN" \
"$KC_SERVER/admin/realms/master/custom-client-rest-resource/clients/admin-cli" \
)

echo $KC_CLIENT_RESPONSE | jq -C .

```

**custom-client-rest-resource** is the name of the component that provides the custom rest endpoint

*The name of the customer being searched for is the last value in the path* **admin-cli**
