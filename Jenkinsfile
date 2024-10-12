pipeline {
    agent any
    environment {
        GIT_CREDENTIALS_ID = 'git'
        GC_KEY = 'gcp'
        REGISTRY_URI = 'us-east4-docker.pkg.dev'
        PROJECT_ID = 'brooks-437520'
        ARTIFACT_REGISTRY = 'brooks-artifacts'
        IMAGE_NAME = 'auth-server'
        CLUSTER = 'low-cost-cluster'
        ZONE = 'us-central1-a'
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64' // Set JAVA_HOME for WSL
        PATH = "${JAVA_HOME}/bin:${env.PATH}" // Add JAVA_HOME to the PATH for WSL
    }
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/BorisSolomonia/reflection-auth-server.git', branch: 'master', credentialsId: "${GIT_CREDENTIALS_ID}"
            }
        }
        stage('Build and Push Image') {
            steps {
                withCredentials([file(credentialsId: GC_KEY, variable: 'GC_KEY_FILE')]) {
                    script {
                        // Translate the key file path to be compatible with WSL
                        def wslKeyFilePath = GC_KEY_FILE.replace('\\', '/').replace('C:', '/mnt/c')

                        withEnv(["GOOGLE_APPLICATION_CREDENTIALS=${wslKeyFilePath}"]) {
                            // Authenticate with Google Cloud using WSL
                            bat "wsl -d Ubuntu-22.04 gcloud auth activate-service-account --key-file=${wslKeyFilePath} --verbosity=debug"
                            bat "wsl -d Ubuntu-22.04 gcloud auth configure-docker ${REGISTRY_URI}"
                        }

                        def mvnCMD = "/home/borissolomonia/maven/bin/mvn"

                        def imageTag = "v${env.BUILD_NUMBER}"
                        def imageFullName = "${REGISTRY_URI}/${PROJECT_ID}/${ARTIFACT_REGISTRY}/${IMAGE_NAME}:${imageTag}"

                        // Build and push Docker image using Jib
                        bat "wsl -d Ubuntu-22.04 ${mvnCMD} clean compile package"
                        bat "wsl -d Ubuntu-22.04 ${mvnCMD} com.google.cloud.tools:jib-maven-plugin:3.4.3:build -Dimage=${imageFullName}"

                        // Update deployment manifest with new image
                        bat "wsl -d Ubuntu-22.04 sed -i \"s|IMAGE_URL|${imageFullName}|g\" auth-server-deployment.yaml"
                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                withCredentials([file(credentialsId: GC_KEY, variable: 'GC_KEY_FILE')]) {
                    script {
                        // Translate the key file path to be compatible with WSL
                        def wslKeyFilePath = GC_KEY_FILE.replace('\\', '/').replace('C:', '/mnt/c')

                        // Authenticate and deploy to GKE using WSL
                        bat "wsl -d Ubuntu-22.04 gcloud auth activate-service-account --key-file=${wslKeyFilePath} --verbosity=debug"
                        bat "wsl -d Ubuntu-22.04 gcloud container clusters get-credentials ${CLUSTER} --zone ${ZONE} --project ${PROJECT_ID}"
                        bat "wsl -d Ubuntu-22.04 kubectl apply -f auth-server-deployment.yaml"
                    }
                }
            }
        }
        stage('Debug Maven Path') {
            steps {
                bat "echo Converted Maven Path: /home/borissolomonia/maven/bin/mvn"
            }
        }
    }
}
