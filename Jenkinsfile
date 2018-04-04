#!groovy

pipeline {
    agent any

    environment {
        customImage = ''
    }

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
                withCredentials([usernamePassword(credentialsId: 'DockerDocker', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                  // available as an env variable, but will be masked if you try to print it out any which way
                  // note: single quotes prevent Groovy interpolation; expansion is by Bourne Shell, which is what you want
                  sh 'echo $PASSWORD'
                  // also available as a Groovy variable
                  echo USERNAME
                  // or inside double quotes for string interpolation
                  echo "username is $USERNAME"
                }
                script {
                    docker.withRegistry('https://docker.io', 'DockerDocker') {
                        customImage = docker.build("cletus/hello-world:${env.BUILD_ID}")
                        customImage.push()
                    }

                }
            }
        }
    }
}