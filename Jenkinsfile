pipeline {

    agent any

    tools {
        jdk 'jdk-21'
        maven 'mvn-3.9'
    }

    environment {
        DB_USERNAME = credentials('db-username')
        DB_PASSWORD = credentials('db-password')
    }

    stages {

        stage('Checkout Source Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Pranavgithub45/jenkins.git'
            }
        }

        stage('Compile') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('Package Application') {
            steps {
                bat 'mvn package -DskipTests'
            }
        }

        stage('Stop Existing Application') {
            steps {
                bat '''
                @echo off
                for /f "tokens=5" %%a in ('netstat -ano ^| findstr :9901') do (
                    echo Stopping existing application...
                    taskkill /PID %%a /F
                )
                exit /b 0
                '''
            }
        }

        stage('Deploy Application') {
            steps {
                bat '''
                @echo off
                echo Starting Spring Boot Application...

                :: Prevent Jenkins from terminating the application
                set JENKINS_NODE_COOKIE=dontKillMe

                :: Start the Spring Boot application in the background
                for %%f in (target\\*.jar) do (
                    start "" /B java -jar "%%f" > app.log 2>&1
                    goto :started
                )

                echo No JAR file found.
                exit /b 1

                :started
                echo Waiting for application startup...
                ping 127.0.0.1 -n 11 > nul

                echo Application Started Successfully.
                '''
            }
        }

    }

    post {

        success {
            echo 'Pipeline executed successfully.'
        }

        failure {
            echo 'Pipeline failed.'
        }

    }

}