#!/bin/bash

echo "Running gradlew with TAG_EXPR: \"$TAG_EXPR\" and TEST_ARGS: \"$TEST_ARGS\""

exec ./gradlew test $TEST_ARGS