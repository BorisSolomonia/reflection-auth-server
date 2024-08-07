node {
    def repourl = "${REGISTRY_URI}/${PROJECT_ID}/${ARTIFACT_REGISTRY}"
    def mvnHome = tool name: 'maven', type: 'maven'
    def mvnCMD = "${mvnHome}/bin/mvn"
    stage('Checkout'){
        checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: 'git',
        url: 'https://github.com/BorisSolomonia/reflection-auth-server.git']]])
    }
    stage('Build and Push Image'){
        withCredentials([file(credentialsId: '100727566614300737035', variable: 'GC_KEY')]){
            sh("gcloud auth activate-service-account --key-file=${GC_KEY}")
            sh 'gcloud auth configure-docker asia-south1-docker.pkg.dev'
            sh "${mvnCMD} clean install jib:build -DREPO_URL=${REGISTRY_URI}/${PROJECT_ID}/${ARTIFACT_REGISTRY}"
        }     

    }
    stage('Deploy'){
        sh "sed -i 's|IMAGE_URL|${repourl}|g' auth-server-deployment.yaml"
        step([$class: 'KubernetesEngineBuilder', projectId: env.PROJECT_ID, 
        cluster: env.CLUSTER, 
        location: env.ZONE, 
        manifestPattern: 'auth-server-deployment.yaml', 
        credentialsId: env.PROJECT_ID, 
        verifyDeployments: true])
    }
}

// pipeline {
//     agent any
//     environment {
//         GIT_CREDENTIALS_ID = 'github-credentials-id' // Your GitHub credentials ID
//         GC_KEY = 'gke-credentials-id' // Your Google Cloud JSON key file ID
//         DOCKER_CREDENTIALS_ID = 'dockerhub-credentials-id' // Your DockerHub credentials ID
//         REGISTRY_URI = 'your-docker-registry-uri'
//         PROJECT_ID = 'your-gcp-project-id'
//         ARTIFACT_REGISTRY = 'your-artifact-registry'
//         CLUSTER = 'your-gke-cluster'
//         ZONE = 'your-gke-zone'
//         REPO_URL = "${env.REGISTRY_URI}/${env.PROJECT_ID}/${env.ARTIFACT_REGISTRY}"
//     }
//     stages {
//         stage('Checkout') {
//             steps {
//                 git url: 'https://github.com/BorisSolomonia/reflection-auth-server.git', branch: 'master', credentialsId: "${env.GIT_CREDENTIALS_ID}"
//             }
//         }
//         stage('Build and Push Image') {
//             steps {
//                 withCredentials([file(credentialsId: "${env.GC_KEY}", variable: 'GC_KEY')]) {
//                     script {
//                         sh "gcloud auth activate-service-account --key-file=${GC_KEY}"
//                         sh 'gcloud auth configure-docker'
//                     }
//                 }
//                 withCredentials([usernamePassword(credentialsId: "${env.DOCKER_CREDENTIALS_ID}", passwordVariable: 'DOCKER_PASS', usernameVariable: 'DOCKER_USER')]) {
//                     script {
//                         def mvnHome = tool name: 'maven', type: 'maven'
//                         def mvnCMD = "${mvnHome}/bin/mvn"
//                         sh "${mvnCMD} clean install jib:build -DREPO_URL=${REPO_URL} -Djib.to.auth.username=${DOCKER_USER} -Djib.to.auth.password=${DOCKER_PASS}"
//                     }
//                 }
//             }
//         }
//         stage('Deploy') {
//             steps {
//                 script {
//                     sh "sed -i 's|IMAGE_URL|${REPO_URL}|g' auth-server-deployment.yaml"
//                     step([$class: 'KubernetesEngineBuilder', projectId: env.PROJECT_ID, cluster: env.CLUSTER, location: env.ZONE, manifestPattern: 'auth-server-deployment.yaml', credentialsId: env.GC_KEY, verifyDeployments: true])
//                 }
//             }
//         }
//     }
// }
