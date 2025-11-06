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
        jdk 'jdk22'       // Sesuaikan dengan konfigurasi di Jenkins
        maven 'maven3'    // Gunakan Maven yang sudah dikonfigurasi di Jenkins
        allure 'allure'   // Pastikan plugin Allure sudah diinstall
    }

    environment {
        ALLURE_RESULTS_DIR = "allure-results"  // Satu folder default untuk hasil Cucumber
    }

    stages {
        stage('Checkout') {
            steps {
                echo " Cloning repository..."
                git branch: 'main', url: 'https://github.com/kennyRamadhan/automationCucumber.git'
            }
        }

        stage('Prepare Environment') {
            steps {
                echo "🧹 Cleaning previous reports..."
                sh "rm -rf ${env.ALLURE_RESULTS_DIR} || true"
                sh "mkdir -p ${env.ALLURE_RESULTS_DIR}"
            }
        }

        stage('Run Cucumber Tests') {
            steps {
                script {
                    echo "Running Cucumber tests with tag: ${params.CUCUMBER_TAG}"
                    echo "Browser mode: ${params.BROWSER_MODE}"

                    sh """
                        mvn clean test \
                        -Dcucumber.filter.tags="${params.CUCUMBER_TAG}" \
                        -Dallure.results.directory=${env.ALLURE_RESULTS_DIR} \
                        -DBROWSER_MODE=${params.BROWSER_MODE}
                    """
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                echo "Generating Allure report..."
                allure([
                    includeProperties: false,
                    jdk: '',
                    results: [[path: "${env.ALLURE_RESULTS_DIR}"]]
                ])
            }
        }

        stage('Archive Allure Report') {
            steps {
                script {
                    def tagName = params.CUCUMBER_TAG.replace('@','')
                    echo "📁 Archiving report for tag: ${tagName}"
                    sh "cp -r allure-report allure-report-${tagName}"
                    archiveArtifacts artifacts: "allure-report-${tagName}/**", fingerprint: true
                }
            }
        }
    }

    post {
        success {
            echo " Build & Cucumber tests completed successfully!"
        }
        failure {
            echo "Build failed! Check logs and Allure results for details."
        }
        always {
            echo "Pipeline finished — cleaning up workspace..."
        }
    }
}
