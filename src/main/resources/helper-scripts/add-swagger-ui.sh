SWAGGER_UI_VERSION=5.3.1

set -e

echo ">> START: add-swagger-ui.sh"
echo "Removing previously downloaded swagger-ui if present..."
if [ -d "target/swagger-ui" ]; then rm -fr target/swagger-ui; fi
echo -n "Downloading swagger-ui..."
mvn dependency:copy -Dartifact=org.webjars:swagger-ui:$SWAGGER_UI_VERSION
echo "OK"
echo -n "Renaming swagger-ui to swagger-ui.zip..."
mv target/dependency/swagger-ui-$SWAGGER_UI_VERSION.jar target/dependency/swagger-ui.zip
echo "OK"
echo "Removing existing swagger-ui if present..."
if [ -d "docs/swagger-ui" ]; then rm -fr docs/swagger-ui; fi
echo -n "Unzipping swagger-ui..."
unzip -q target/dependency/swagger-ui.zip -d target/swagger-ui
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
echo -n "  - removing the try it out button..."
sed -i -e '$a.try-out { display: none; }' docs/swagger-ui/index.css
echo "OK"
echo "Patching swagger-initializer.js"
echo -n " - replace link to petstore with ../api.yml ..."
sed -i -e 's#https://petstore.swagger.io/v2/swagger.json#../api.yml#' docs/swagger-ui/swagger-initializer.js
echo "OK"
echo ">> END: add-swagger-ui.sh"

