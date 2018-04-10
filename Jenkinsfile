#!groovy

pipeline {
    agent any

    environment {
        clusterName = 'chc-microservice'
        serviceName = 'hello-world'
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
                script {
                    withCredentials([usernamePassword(credentialsId: 'DockerDocker', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                        sh 'docker login --username=${USERNAME} --password="${PASSWORD}"'
                        docker.withRegistry('https://docker.io') {
                            customImage = docker.build("cletus/hello-world:${env.BUILD_ID}")
                            customImage.push()
                            customImage.push('latest')
                        }
                    }
                }
            }
        }
        stage('Approve') {
            steps {
                timeout(time:1, unit:'DAY') {
                    input message:'Approve deployment?', submitter: 'it-ops'
                }
            }
        }
        stage('Deploy to ECS cluster') {
            steps {
                withAWS(region:'us-west-2', credentials:'aws') {
                    sh 'aws ecs update-service --cluster ${clusterName} --service ${serviceName} --force-new-deployment'
                    sh 'aws ecs wait services-stable --cluster ${clusterName} --service ${serviceName}'
                }
            }
        }
    }
}