pipeline {
    agent any

    parameters {
        choice(
            name: 'CUCUMBER_TAG',
            choices: ['@all', '@smoke', '@regression'],
            description: 'Pilih tag Cucumber yang ingin dijalankan'
        )

        choice(
            name: 'BROWSER_MODE',
            choices: ['normal', 'headless'],
            description: 'Pilih mode browser untuk WebUI tests'
        )
    }

    tools {
        jdk 'jdk22'      // sesuai konfigurasi JDK di Jenkins
        maven 'maven3'   // sesuai konfigurasi Maven di Jenkins
        allure 'allure'  // Allure plugin di Jenkins
    }

    environment {
        // Set folder Allure default
        ALLURE_RESULTS_WEB = "allure-results-web"
        ALLURE_RESULTS_API = "allure-results-api"
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Cloning repository..."
                git branch: 'main', url: 'https://github.com/kennyRamadhan/automationCucumber.git'
            }
        }

        stage('Prepare Allure Folder') {
            steps {
                script {
                    sh "mkdir -p ${env.ALLURE_RESULTS_WEB}"
                    sh "mkdir -p ${env.ALLURE_RESULTS_API}"
                }
            }
        }

        stage('Run Cucumber Tests') {
            steps {
                script {
                    echo "Running Cucumber tests with tag: ${params.CUCUMBER_TAG}"
                    def allureFolder = env.ALLURE_RESULTS_WEB  // ubah jika ingin pisah Web/API
                    sh """
                        mvn clean test \
                        -Dcucumber.options="--tags ${params.CUCUMBER_TAG}" \
                        -Dallure.results.directory=${allureFolder} \
                        -DBROWSER_MODE=${params.BROWSER_MODE}
                    """
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                script {
                    def allureFolder = env.ALLURE_RESULTS_WEB
                    echo "Generating Allure report from ${allureFolder}..."
                    allure([
                        includeProperties: false,
                        jdk: '',
                        results: [[path: allureFolder]]
                    ])
                }
            }
        }

        stage('Archive Allure Report') {
            steps {
                script {
                    def suiteName = params.CUCUMBER_TAG.replace('@','')
                    sh "cp -r allure-report allure-report-${suiteName}"
                    archiveArtifacts artifacts: "allure-report-${suiteName}/**", fingerprint: true
                }
            }
        }
    }

    post {
        success {
            echo "Build and Cucumber tests completed successfully!"
        }
        failure {
            echo "Build failed! Check logs and allure-results for details."
        }
    }
}
