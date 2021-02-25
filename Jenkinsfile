pipeline {
    agent any
    stages {
        stage ('build') {
            steps {
                sh 'gradle check assemble -Penv=dev -Dconfig.resource=dev.conf'
            }
        }
    }
}