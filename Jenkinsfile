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

        stage('Build Codes') {
            steps {
                script {
                    def serviceDirs = env.SERVICE_DIRS.split(",")
                    serviceDirs.each { service ->
                        sh """
                            echo "Building ${service}"
                            chmod +x ${service}/gradlew
                            cd ${service}
                            ./gradlew clean build -x test
                            ls -al ./build/libs
                            cd ..
                        """
                    }
                }
            }
        }
    }
}
