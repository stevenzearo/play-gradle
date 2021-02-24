pipeline {
    agent any
    stages {
        stage ('build') {
            steps {
                sh 'gradle -Penv=dev check assemble --parallel'
            }
        }
    }
}