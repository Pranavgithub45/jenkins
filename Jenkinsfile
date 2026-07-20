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
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Stop Existing Application') {
            steps {
                bat '''
                @echo off

                for /f "tokens=5" %%a in ('netstat -ano ^| findstr :9901') do (
                    echo Stopping existing application (PID %%a)...
                    taskkill /F /PID %%a
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

                if exist app.log del app.log

                for %%f in (target\\*.jar) do (
                    echo Starting %%f

                    start "" cmd /c "java -jar ""%%f"" > app.log 2>&1"

                    goto :started
                )

                echo ERROR: No JAR file found.
                exit /b 1

                :started

                echo Waiting for application to start...
                timeout /t 15 > nul
                '''
            }
        }

        stage('Verify Deployment') {
            steps {
                bat '''
                @echo off

                echo Checking if application is listening on port 9901...

                netstat -ano | findstr :9901

                if %ERRORLEVEL% NEQ 0 (
                    echo.
                    echo =====================================
                    echo APPLICATION FAILED TO START
                    echo =====================================

                    if exist app.log (
                        echo.
                        echo ======= APPLICATION LOG =======
                        type app.log
                        echo ===============================
                    ) else (
                        echo app.log not found.
                    )

                    exit /b 1
                )

                echo.
                echo =====================================
                echo APPLICATION IS RUNNING SUCCESSFULLY
                echo URL:
                echo http://localhost:9901
                echo =====================================
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