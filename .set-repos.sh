#!/bin/bash

echo -en "[repositories]\n  local\n  lib-releases: https://artifactory.digital.homeoffice.gov.uk/artifactory/libs-release/\n  lib-snapshots: https://artifactory.digital.homeoffice.gov.uk/artifactory/libs-snapshot/\n" > /app/.sbt/repositories
