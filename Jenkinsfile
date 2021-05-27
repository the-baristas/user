pipeline {
    agent any
    environment {
        COMMIT_HASH = "${sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()}"
        AWS_ID = '135316859264'
    }
    stages {
        stage('Clean and test target') {
            steps {
                sh 'mvn clean test'
            }
        }
        stage('Test and Package') {
            steps {
                sh 'mvn package'
            }
        }
        stage('Code Analysis: SonarQube') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
  //      stage('Quality gate') {
 //           steps {
//                waitForQualityGate abortPipeline: true
//            }
//        }
        stage('Docker Build') {
            steps {
                echo 'Deploying....'
                 //sh "aws ecr ........."
                sh "docker build --tag user-service:$COMMIT_HASH ."
                 sh "docker tag user-service:$COMMIT_HASH $AWS_ID.dkr.ecr.us-east-2.amazonaws.com/user-service:$COMMIT_HASH"
                 //sh "docker push $AWS_ID.dkr.ecr.us-east-2.amazonaws.com/user-service:$COMMIT_HASH"
            }
        }
    }
    post {
        always {
            sh 'mvn clean'
            sh 'docker system prune -f'
        }
    }
}
