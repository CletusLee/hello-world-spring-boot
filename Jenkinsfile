pipeline {
    agent any

    options {
        timeout(time: 1, unit: 'HOURS')
    }
    environment {
        clusterName = 'chc-microservice'
        serviceName = 'chc-cih'
        regionName = 'us-west-2'
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
                sh 'mvn clean compile'
            }
        }
        stage('Unit Test') {
            agent {
                docker {
                    image 'maven:3.5.3-jdk-8'
                    reuseNode true
                }
            }
            steps {
                sh 'mvn test'
            }
        }
        stage('Package') {
            agent {
                docker {
                    image 'maven:3.5.3-jdk-8'
                    reuseNode true
                }
            }
            steps {
                sh 'mvn package -Dmaven.test.skip=true'
            }
        }
        stage('Publish Reports') {
            steps {
               publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'target/site/jacoco', reportFiles: 'index.html', reportName: 'Code Coverage', reportTitles: ''])
               junit 'target/surefire-reports/TEST-*.xml'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    sh("eval \$(aws ecr get-login --no-include-email --region ${regionName} | sed 's|https://||')")
                    docker.withRegistry('https://988532124766.dkr.ecr.us-west-2.amazonaws.com/chc-cih') {
                        customImage = docker.build("${serviceName}:${env.BUILD_ID}")
                        customImage.push()
                        customImage.push('latest')
                    }
                }
            }
        }
        stage('Deploy to ECS cluster') {
            input {
                message "Deploy to ECS?"
            }
            steps {
                withAWS(region:'us-west-2', credentials:'aws') {
                    sh 'ecs-deploy -c ${clusterName} -n ${serviceName} -i 988532124766.dkr.ecr.us-west-2.amazonaws.com/${serviceName}:${BUILD_ID} -t 6000'
                }
            }
        }
    }
}