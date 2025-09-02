
pipeline {
    agent any

    stages {
        stage('Build Database') {
            steps {
                // Root level DB Dockerfile
                sh 'docker build -t my-mysql-db -f Dockerfile.db .'  
            }
        }

        stage('Deploy Database') {
            steps {
                sh 'docker run -d --name my-mysql-db -p 3306:3306 my-mysql-db'
            }
        }

        stage('Build Backend') {
            steps {
                dir('Backend') {
                    sh 'docker build -t my-backend .'
                }
            }
        }

        stage('Deploy Backend') {
            steps {
                sh 'docker run -d --name my-backend -p 8081:8081 --link my-mysql-db my-backend'
            }
        }

        stage('Build Frontend') {
            steps {
                dir('Frontend') {
                    sh 'docker build -t my-frontend .'
                }
            }
        }

        stage('Deploy Frontend') {
            steps {
                sh 'docker run -d --name my-frontend -p 3000:3000 my-frontend'
            }
        }
    }
}

