pipeline {
    agent any
    stages {
        stage ('build') {
            steps {
                sh './gradlew -Penv=dev check assemble --parallel'
            }
        }
    }
}