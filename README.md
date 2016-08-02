# Jenkins Enhanced Orphaned Item Strategy Plugin

This plugin adds enhanced Orphaned Item Strategy of [CloudBees Folders Plugin](https://wiki.jenkins-ci.org/display/JENKINS/CloudBees+Folders+Plugin).

## Development

To build the plugin locally:

    mvn clean verify

To release the plugin:

    mvn release:prepare release:perform

To test in a local Jenkins instance:

    mvn hpi:run

## License

Licensed under the [MIT](/LICENSE.txt) license.
