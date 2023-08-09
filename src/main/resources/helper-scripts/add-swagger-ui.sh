SWAGGER_UI_VERSION=5.3.1

echo "Removing previously downloaded swagger-ui if present..."
if [ -d "target/swagger-ui" ]; then rm -fr target/swagger-ui; fi
echo -n "Downloading swagger-ui..."
mvn dependency:get -Dartifact=org.webjars:swagger-ui:$SWAGGER_UI_VERSION -Ddest=target/swagger-ui.zip
echo "OK"
echo "Removing existing swagger-ui if present..."
if [ -d "docs/swagger-ui" ]; then rm -fr docs/swagger-ui; fi
echo -n "Unzipping swagger-ui..."
unzip -q target/swagger-ui.zip -d target/swagger-ui
echo "OK"
echo -n "Copying the relevant files to docs/swagger-ui..."
cp -r target/swagger-ui/META-INF/resources/webjars/swagger-ui/$SWAGGER_UI_VERSION docs/swagger-ui
echo "OK"
echo "Patching the index.css"
echo -n "  - removing the top bar..."
sed -i -e '$a.topbar { display: none; }' docs/swagger-ui/index.css
echo "OK"
echo -n "  - removing the link to api.yml..."
sed -i -e '$a.link { display: none; }' docs/swagger-ui/index.css
echo "OK"
echo -m "  - removing the try it out button..."
sed -i -e '$a.try-out { display: none; }' docs/swagger-ui/index.css
echo "OK"
echo "Patching swagger-initializer.js"
echo " - replace link to petstore with ../api.yml"
sed -i -e 's#https://petstore.swagger.io/v2/swagger.json#../api.yml#' docs/swagger-ui/swagger-initializer.js
echo "OK"

echo "DONE"




