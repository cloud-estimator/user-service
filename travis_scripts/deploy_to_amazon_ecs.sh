echo "Launching $BUILD_NAME IN AMAZON ECS"
ecs-cli configure profile --access-key $AWS_ACCESS_KEY_ID --secret-key $AWS_SECRET_ACCESS_KEY
ecs-cli configure --region us-west-2 --cluster cloud-estimator
ecs-cli compose --file ./docker-compose.yml up