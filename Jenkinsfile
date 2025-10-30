pipeline {
    agent any

    environment {
        SERVER_USER = 'root'                      // o el usuario que uses en tu VPS
        SERVER_HOST = '66.97.42.224'           // IP o dominio de tu VPS DonWeb
        PROJECT_PATH = '/root/DeployDonWeb/gimnasio_prueba'              // ruta donde vive tu proyecto en el VPS
        GIT_URL = 'https://github.com/FacundoLella/gimnasio_prueba.git'
    }

    stages {
        stage('Compilar y ejecutar tests') {
            steps {
                dir('gimnasio') {
                    sh './mvnw clean test'
                }
            }
        }

        stage('Build del JAR (solo si los tests pasan)') {
            when {
                expression { currentBuild.result == null || currentBuild.result == 'SUCCESS' }
            }
            steps {
                dir('gimnasio') {
                    sh './mvnw package -DskipTests=true'
                }
            }
        }

        

        stage('Deploy remoto con Docker Compose') {
            when {
                expression { currentBuild.result == null || currentBuild.result == 'SUCCESS' }
            }
            steps {
                sshagent(['server-ssh-cred']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no $SERVER_USER@$SERVER_HOST '
                            if [ ! -d "$PROJECT_PATH" ]; then
                                echo "📦 Clonando repositorio..."
                                git clone $GIT_URL $PROJECT_PATH
                            else
                                echo "🔄 Actualizando repositorio existente..."
                                cd $PROJECT_PATH
                                git pull
                            fi

                            cd $PROJECT_PATH
                            echo "⚙️ Construyendo y levantando servicios con Docker Compose..."
                            docker compose build
                            docker compose up -d
                        '
                    """
                }
            }
        }
    }

    post {
        success {
            echo '✅ Deploy completado con éxito, servicios levantados correctamente.'
        }
        failure {
            echo '❌ Falló el build o los tests. No se realizó el deploy.'
        }
    }
}
