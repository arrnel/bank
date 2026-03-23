#!/bin/bash

source ./docker.properties

# --------------------------------------------------------
# ENV VARS
# --------------------------------------------------------
export ALLURE_DIR="./allure-results"
export COMPOSE_FILE=docker-compose.yaml
export IMAGE_PREFIX=${IMAGE_PREFIX}
export IMAGE_NAME="bank-tests"
export PROFILE="docker"


# --------------------------------------------------------
# Step 1: Clear allure-results dir
# --------------------------------------------------------
echo "### CLEAR ALLURE RESULTS"
rm -rf $ALLURE_DIR
mkdir -p $ALLURE_DIR


# --------------------------------------------------------
# Step 2: Cleanup previous run
# --------------------------------------------------------
echo "### Stopping all services ###"
docker compose -f $COMPOSE_FILE down
docker compose -f $COMPOSE_FILE rm -f $IMAGE_NAME || true


# --------------------------------------------------------
# Step 3: Check and remove test images
# --------------------------------------------------------
echo "### Checking for existing test images ###"
FULL_IMAGE_NAME="${IMAGE_PREFIX:+$IMAGE_PREFIX/}$IMAGE_NAME"
echo "### Searching images with: docker images --format '{{.Repository}}:{{.Tag}}' | grep \"$FULL_IMAGE_NAME\" ###"

test_images=$(docker images --format '{{.Repository}}:{{.Tag}}' | grep "$FULL_IMAGE_NAME" ) || {
  echo "### ERROR: Failed to list Docker images! Check 'docker images' manually. ###"
  echo "### Continuing without removing test images ###"
  test_images=""
}

if [ -n "$test_images" ]; then
  echo "### Found test images: ###"
  echo "$test_images"
  echo "### Removing test images ###"
  docker rmi $test_images || echo "### Some images could not be removed (in use?). Skipping. ###"
else
  echo "### No test images with name containing '$FULL_IMAGE_NAME' found. Skipping removal. ###"
fi

# --------------------------------------------------------
# Step 4: Build & push images
# --------------------------------------------------------
bash ./gradlew clean

if [ "$1" = "push" ] || [ "$2" = "push" ]; then
  echo "### Build & push images ###"
  bash ./gradlew jib -x :tests:test
else
  echo "### Build images ###"
  bash ./gradlew jibDockerBuild -x :tests:test
fi


# --------------------------------------------------------
# Step 5: Start all containers with rebuild
# --------------------------------------------------------
echo "### Starting all containers ###"
docker compose -f $COMPOSE_FILE up -d

echo "### TEST CONTAINERS ###"
docker ps -a