pipeline {
    agent any
    environment {
        GIT_CREDENTIALS_ID = 'github-credentials-id'
        GC_KEY = 'gke-credentials-id'
        REGISTRY_URI = 'asia-south1-docker.pkg.dev'
        PROJECT_ID = 'reflection01-431417'
        ARTIFACT_REGISTRY = 'reflection-artifacts'
        CLUSTER_NAME = 'reflection-cluster-1'
        ZONE = 'asia-south1'
        REPO_URL = "${env.REGISTRY_URI}/${env.PROJECT_ID}/${env.ARTIFACT_REGISTRY}"
    }
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/BorisSolomonia/reflection-auth-server.git', branch: 'master', credentialsId: "${env.GIT_CREDENTIALS_ID}"
            }
        }
        stage('Build and Push Image') {
            steps {
                withCredentials([file(credentialsId: "${env.GC_KEY}", variable: 'GC_KEY_FILE')]) {
                    script {
                        withEnv(["GOOGLE_APPLICATION_CREDENTIALS=${GC_KEY_FILE}"]) {
                            sh "gcloud auth activate-service-account --key-file=${GC_KEY_FILE}"
                            sh "gcloud auth configure-docker ${REGISTRY_URI}"
                        }
                        def mvnHome = tool name: 'maven', type: 'maven'
                        def mvnCMD = "${mvnHome}/bin/mvn"
                        sh "${mvnCMD} clean install jib:build -DREPO_URL=${REPO_URL}"
                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    sh "sed -i 's|IMAGE_URL|${REPO_URL}|g' auth-server-deployment.yaml"
                    withCredentials([file(credentialsId: "${env.GC_KEY}", variable: 'GC_KEY_FILE')]) {
                        echo "Using credentials ID: ${env.GC_KEY}"
                        step([
                            $class: 'KubernetesEngineBuilder', 
                            projectId: env.PROJECT_ID, 
                            cluster: "${env.CLUSTER_NAME} (${env.ZONE})", // Ensure correct format
                            location: env.ZONE, 
                            manifestPattern: 'auth-server-deployment.yaml', 
                            credentialsId: env.GC_KEY, 
                            verifyDeployments: true
                        ])
                    }
                }
            }
        }
    }
}
