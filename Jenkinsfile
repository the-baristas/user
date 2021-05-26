pipeline {
    agent any
    environment {
        COMMIT_HASH = "${sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()}"
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
        stage('Quality gate') {
            steps {
                waitForQualityGate abortPipeline: true
            }
        }
        stage('Docker Build') {
            steps {
                echo 'Deploying....'
                // sh "aws ecr ........."
                sh "docker build --tag user-service:$COMMIT_HASH ."
                // sh "docker tag MicroServiceName:$COMMIT_HASH $AWS_ID/ECR Repo/MicroServiceName:$COMMIT_HASH"
                // sh "docker push $AWS_ID/ECR Repo/MicroServiceName:$COMMIT_HASH"
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
