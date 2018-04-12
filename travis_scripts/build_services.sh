echo "Building with travis commit of $BUILD_NAME ..."
./mvnw clean package dockerfile:build -DskipTests