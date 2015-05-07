publishTo := Some("Artifactory Realm" at "http://artifactory.registered-traveller.homeoffice.gov.uk/artifactory/libs-snapshot-local")

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")