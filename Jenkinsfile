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

        stage('Stop Existing Application') {
            steps {
                bat '''
                @echo off
                echo Checking for application running on port 9901...

                for /f "tokens=5" %%a in ('netstat -ano ^| findstr :9901') do (
                    echo Stopping existing application (PID: %%a)...
                    taskkill /PID %%a /F
                )

                exit /b 0
                '''
            }
        }

        stage('Build Application') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy Application') {
            steps {
                bat '''
                @echo off
                echo Starting Spring Boot Application...

                :: Prevent Jenkins from terminating the application
                set JENKINS_NODE_COOKIE=dontKillMe

                :: Start the Spring Boot application
                for %%f in (target\\*.jar) do (
                    echo Launching %%f...
                    start "" /B java -jar "%%f" > app.log 2>&1
                    goto :started
                )

                echo ERROR: No JAR file found in target folder.
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