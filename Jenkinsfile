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
                powershell '''
                Write-Host "Checking application on port 9901..."

                $process = Get-NetTCPConnection -LocalPort 9901 -ErrorAction SilentlyContinue |
                           Select-Object -ExpandProperty OwningProcess -Unique

                if ($process) {
                    Write-Host "Stopping process $process..."
                    Stop-Process -Id $process -Force
                    Start-Sleep -Seconds 3
                }
                else {
                    Write-Host "No application running on port 9901."
                }
                '''
            }
        }

        stage('Deploy Application') {
            steps {
                powershell '''
                Write-Host "Starting Spring Boot Application..."

                if (Test-Path "app.log") {
                    Remove-Item "app.log"
                }

                $jar = Get-ChildItem "target\\*.jar" | Where-Object {
                    $_.Name -notlike "*.original"
                } | Select-Object -First 1

                if (-not $jar) {
                    Write-Host "No JAR file found."
                    exit 1
                }

                Write-Host "Starting $($jar.Name)"

                Start-Process `
                    -FilePath "java" `
                    -ArgumentList "-jar `"$($jar.FullName)`"" `
                    -RedirectStandardOutput "app.log" `
                    -RedirectStandardError "app-error.log" `
                    -WindowStyle Hidden

                Start-Sleep -Seconds 15

                Write-Host "Checking port 9901..."

                $port = Get-NetTCPConnection -LocalPort 9901 -ErrorAction SilentlyContinue

                if (-not $port) {
                    Write-Host "Application failed to start."

                    if (Test-Path "app.log") {
                        Get-Content app.log
                    }

                    if (Test-Path "app-error.log") {
                        Get-Content app-error.log
                    }

                    exit 1
                }

                Write-Host "Application started successfully on port 9901."
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