pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/CletusLee/hello-world-spring-boot'
            }
        }
        stage('Build') {
            agent {
                docker {
                    image 'maven:3.5.3-jdk-8'
                    reuseNode true
                }
            }
            steps {
                sh 'mvn clean install'
            }
        }
        stage('Publish Reports') {
            steps {
               publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'target/site/', reportFiles: 'index.html', reportName: 'Code Coverage', reportTitles: ''])
               junit 'target/surefire-reports/TEST-*.xml'
            }
        }
        stage('Build Docker Image') {
            steps {
                docker.build("my-image:${env.BUILD_ID}")
            }
        }
    }
}