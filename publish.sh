#!/bin/sh

if [ $# -ne 2 ]; then
  echo 'Usage: ./publish.sh <TAG> <MESSAGE>' >&2
  exit 1
fi

if [ "$(git diff --stat)" != '' ]; then
  echo 'ERROR: repository is dirty.' >&2
  exit 1
fi

TAG="$1"
MESSAGE="$2"

if [ "$(git tag -l | grep $TAG)" != '' ]; then
  echo "ERROR: tag $TAG already exists." >&2
  exit 1
fi

git tag -a -m "$MESSAGE" "$TAG"
git push --tags

./gradlew publish

