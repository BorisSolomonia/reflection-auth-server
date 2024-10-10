// pipeline {
//     agent any
//     environment {
//         GIT_CREDENTIALS_ID = 'git'
//         GC_KEY = 'gcp'
//         REGISTRY_URI = 'us-east4-docker.pkg.dev'
//         PROJECT_ID = 'brooks-437520'
//         ARTIFACT_REGISTRY = 'brooks-artifacts'
//         IMAGE_NAME = 'auth-server'
//         CLUSTER = 'low-cost-cluster'
//         ZONE = 'us-central1-a'  // Ensure this matches the zone where your cluster is located
//     }
//     stages {
//         stage('Checkout') {
//             steps {
//                 git url: 'https://github.com/BorisSolomonia/reflection-auth-server.git', branch: 'master', credentialsId: "${GIT_CREDENTIALS_ID}"
//             }
//         }
//         stage('Build and Push Image') {
//             steps {
//                 withCredentials([file(credentialsId: "${GC_KEY}", variable: 'GC_KEY_FILE')]) {
//                     script {
//                         withEnv(["GOOGLE_APPLICATION_CREDENTIALS=${GC_KEY_FILE}"]) {
//                             // Authenticate with Google Cloud
//                             sh "gcloud auth activate-service-account --key-file=${GC_KEY_FILE} --verbosity=debug"
//                             sh "gcloud auth configure-docker ${REGISTRY_URI}"
//                         }
//                         def mvnHome = tool name: 'maven', type: 'maven'
//                         def mvnCMD = "${mvnHome}/bin/mvn"
//                         def imageTag = "v${env.BUILD_NUMBER}"
//                         def imageFullName = "${REGISTRY_URI}/${PROJECT_ID}/${ARTIFACT_REGISTRY}/${IMAGE_NAME}:${imageTag}"
                        
//                         // Build and push Docker image using Jib
//                         sh "${mvnCMD} clean compile package"
//                         sh "${mvnCMD} com.google.cloud.tools:jib-maven-plugin:3.4.3:build -Dimage=${imageFullName}"

//                         // Update deployment manifest with new image
//                         sh "sed -i 's|IMAGE_URL|${imageFullName}|g' auth-server-deployment.yaml"
//                     }
//                 }
//             }
//         }
//         stage('Deploy') {
//             steps {
//                 withCredentials([file(credentialsId: "${GC_KEY}", variable: 'GC_KEY_FILE')]) {
//                     script {
//                         // Authenticate and deploy to GKE
//                         sh "gcloud auth activate-service-account --key-file=${GC_KEY_FILE} --verbosity=debug"
//                         sh "gcloud container clusters get-credentials ${CLUSTER} --zone ${ZONE} --project ${PROJECT_ID}"
//                         sh "kubectl apply -f auth-server-deployment.yaml"
//                     }
//                 }
//             }
//         }
//     }
// }


// pipeline {
//     agent any
//     environment {
//         GIT_CREDENTIALS_ID = 'git'
//         GC_KEY = 'gcp'
//         REGISTRY_URI = 'us-east4-docker.pkg.dev'
//         PROJECT_ID = 'brooks-437520'
//         ARTIFACT_REGISTRY = 'brooks-artifacts'
//         IMAGE_NAME = 'auth-server'
//         CLUSTER = 'low-cost-cluster'
//         ZONE = 'us-central1-a'  // Ensure this matches the zone where your cluster is located
//     }
//     stages {
//         stage('Checkout') {
//             steps {
//                 git url: 'https://github.com/BorisSolomonia/reflection-auth-server.git', branch: 'master', credentialsId: "${GIT_CREDENTIALS_ID}"
//             }
//         }
//         stage('Build and Push Image') {
//             steps {
//                 withCredentials([file(credentialsId: "${GC_KEY}", variable: 'GC_KEY_FILE')]) {
//                     script {
//                         withEnv(["GOOGLE_APPLICATION_CREDENTIALS=${GC_KEY_FILE}"]) {
//                             // Authenticate with Google Cloud using WSL
//                             bat "wsl gcloud auth activate-service-account --key-file=${GC_KEY_FILE} --verbosity=debug"
//                             bat "wsl gcloud auth configure-docker ${REGISTRY_URI}"
//                         }
//                         def mvnHome = tool name: 'maven', type: 'maven'
//                         def mvnCMD = "${mvnHome}/bin/mvn"
//                         def imageTag = "v${env.BUILD_NUMBER}"
//                         def imageFullName = "${REGISTRY_URI}/${PROJECT_ID}/${ARTIFACT_REGISTRY}/${IMAGE_NAME}:${imageTag}"

//                         // Build and push Docker image using Jib
//                         bat "wsl ${mvnCMD} clean compile package"
//                         bat "wsl ${mvnCMD} com.google.cloud.tools:jib-maven-plugin:3.4.3:build -Dimage=${imageFullName}"

//                         // Update deployment manifest with new image
//                         bat "wsl sed -i 's|IMAGE_URL|${imageFullName}|g' auth-server-deployment.yaml"
//                     }
//                 }
//             }
//         }
//         stage('Deploy') {
//             steps {
//                 withCredentials([file(credentialsId: "${GC_KEY}", variable: 'GC_KEY_FILE')]) {
//                     script {
//                         // Authenticate and deploy to GKE using WSL
//                         bat 'wsl -d Ubuntu-22.04.5 gcloud auth activate-service-account --key-file=%GC_KEY% --verbosity=debug'
//                         //bat "wsl gcloud auth activate-service-account --key-file=${GC_KEY_FILE} --verbosity=debug"
//                         bat "wsl gcloud container clusters get-credentials ${CLUSTER} --zone ${ZONE} --project ${PROJECT_ID}"
//                         bat "wsl kubectl apply -f auth-server-deployment.yaml"
//                     }
//                 }
//             }
//         }
//     }
// }


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
        ZONE = 'us-central1-a'  // Ensure this matches the zone where your cluster is located
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

                        def mvnHome = tool name: 'maven', type: 'maven'
                        def mvnCMD = "${mvnHome}/bin/mvn"
                        def imageTag = "v${env.BUILD_NUMBER}"
                        def imageFullName = "${REGISTRY_URI}/${PROJECT_ID}/${ARTIFACT_REGISTRY}/${IMAGE_NAME}:${imageTag}"

                        // Build and push Docker image using Jib
                        bat "wsl -d Ubuntu-22.04 ${mvnCMD} clean compile package"
                        bat "wsl -d Ubuntu-22.04 ${mvnCMD} com.google.cloud.tools:jib-maven-plugin:3.4.3:build -Dimage=${imageFullName}"

                        // Update deployment manifest with new image
                        bat "wsl -d Ubuntu-22.04 sed -i 's|IMAGE_URL|${imageFullName}|g' auth-server-deployment.yaml"
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
    }
}
