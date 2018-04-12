echo "Pushing server docker images to docker hub ...."
docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD
docker push cloudestimator/user-service:$BUILD_NAME