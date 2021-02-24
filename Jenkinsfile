pipeline {
    agent any
    stages {
        stage ('build') {
            steps {
                sh 'chmod +x ./gradle'
                sh './gradle build'
            }
        }
    }
}