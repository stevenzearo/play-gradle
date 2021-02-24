pipeline {
    agent any
    stages {
        stage ('build') {
            steps {
                sh 'chown 777 ./'
                sh 'chmod +x ./gradlew'
                sh './gradlew build'
            }
        }
    }
}