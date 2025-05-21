pipeline {
    agent any
    environment {
        SERVICE_DIRS = "config-service,discovery-service,gateway-service,user-service,ordering-service,product-service"
    }
    stages {
        stage('Pull Codes from Github') {
            steps {
                checkout scm
            }
        }

        stage('Detect Changes') {
            steps {
                script {
                    def changedFiles = sh(script: "git diff --name-only HEAD~1 HEAD", returnStdout: true)
                                        .trim()
                                        .split('\n')
                    echo "Changed files: ${changedFiles}"

                    def changedServices = []
                    def serviceDirs = env.SERVICE_DIRS.split(",")

                    serviceDirs.each { service ->
                        if (changedFiles.any { it.startsWith(service + "/") }) {
                            changedServices.add(service)
                        }
                    }

                    env.CHANGED_SERVICES = changedServices.join(",")
                    if (env.CHANGED_SERVICES == "") {
                        echo "No changes detected in service directories. Skipping build and deployment."
                        currentBuild.result = 'SUCCESS'
                    }
                }
            }
        }

        stage('Build Changed Services') {
            when {
                expression { env.CHANGED_SERVICES != "" }
            }
            steps {
                script {
                    def changedServices = env.CHANGED_SERVICES.split(",")

                    withCredentials([string(credentialsId: 'dev-yml-secret', variable: 'DEV_YML')]) {
                        changedServices.each { service ->
                            sh """
                            echo "Building ${service}..."

                            cd ${service}/src/main/resources

                            echo "\$DEV_YML" > application-dev.yml
                            echo "[INFO] application-dev.yml created in ${service}/src/main/resources"

                            cd ../../../
                            cd ${service}

                            ./gradlew clean build -x test
                            ls -al ./build/libs

                            cd ..
                            """
                        }
                    }
                }

                sshagent(credentials: ["deploy-key"]) {
                    sh """
                    scp -o StrictHostKeyChecking=no docker-compose.yml ubuntu@${deployHost}:/home/ubuntu/docker-compose.yml

                    ssh -o StrictHostKeyChecking=no ubuntu@${deployHost} '
                      cd /home/ubuntu &&
                      docker-compose pull ${env.CHANGED_SERVICES} &&
                      docker compose up -d ${env.CHANGED_SERVICES}
                    '
                    """
                }
            }
        }
    }
}
