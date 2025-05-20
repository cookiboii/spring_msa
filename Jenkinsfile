pipeline {
    agent any

    environment {
        // 공백 제거된 서비스 목록
        SERVICE_DIRS = "config-service,discovery-service,gateway-service,user-service,product-service,ordering-service"
    }

    stages {

        stage('Pull code from GitHub') {
            steps {
                checkout scm
            }
        }

        stage('Build code with Gradle') {
            steps {
                script {
                    def serviceDirs = env.SERVICE_DIRS.split(",")

                    serviceDirs.each { rawService ->
                        def service = rawService.trim()

                        sh """
                            echo "🔧 Building ${service}..."
                            cd ${service}
                            chmod +x gradlew      # ✅ 실행 권한 부여
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
