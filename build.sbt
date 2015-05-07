publishTo := Some("Artifactory Realm" at "http://artifactory.registered-traveller.homeoffice.gov.uk/artifactory/libs-release-local")

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

isSnapshot := true       //This allows us to overwrite exisiting artifacts when publishing
