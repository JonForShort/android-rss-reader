#!/usr/bin/env bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

ROOT_DIR=${SCRIPT_DIR}/../..

export COMPOSE_DOCKER_CLI_BUILD=1

export DOCKER_BUILDKIT=1

reader_build() {

    pushd ${ROOT_DIR}/rss-reader

    ./gradlew clean build

    popd
}

reader_test() {

    pushd ${ROOT_DIR}/rss-reader

    ./gradlew clean test

    popd
}

reader_build_cm() {

    set -ex

    pushd ${ROOT_DIR}

    docker-compose run rss-reader ./dev/scripts/env.sh "reader_build"

    popd

    set +ex
}

reader_test_cm() {

    set -ex

    pushd ${ROOT_DIR}

    docker-compose run rss-reader ./dev/scripts/env.sh "reader_test"

    popd

    set +ex
}

reader_clean() {

    git clean -fdx -e out
}

eval "$@"
