$tag=$args[0];
$DOCKER_TAG=$tag;
$DOCKERFILE_PATH='Dockerfile';
$IMAGE_NAME='dravelopsstargateservice';
$DOCKER_REPO='blackforestsolutions';
$targetImage=$DOCKER_REPO + '/' + $IMAGE_NAME
$targetImageTagged=$targetImage + ':' +$DOCKER_TAG

echo "docker tag is $DOCKER_TAG"

docker login

docker build --build-arg DOCKER_TAG=$DOCKER_TAG -f $DOCKERFILE_PATH -t $IMAGE_NAME .

$sourceId= docker images --filter=reference=$IMAGE_NAME --format "{{.ID}}";

echo "sourceId is $sourceId"

docker tag $sourceId $targetImageTagged

docker push $targetImageTagged
