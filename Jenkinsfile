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
                echo Starting Spring Boot Application...

                for %%f in (target\\*.jar) do (
                    start "" /B java -jar "%%f" > app.log 2>&1
                    goto :started
                )

                echo No jar file found
                exit /b 1

                :started
                timeout /t 10 > nul

                echo Application Started Successfully.
                exit /b 0
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