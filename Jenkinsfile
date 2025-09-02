pipeline {
	agent any

	options {
        disableConcurrentBuilds()     // prevent overlapping builds
        skipDefaultCheckout(false)    // always checkout fresh, even if no new commits
    }

	stages {
		stage('Clone Repo') {
			steps {
				git branch: 'main', url: 'https://github.com/sanjayprs2003/myProject.git'
			}
		}

		stage('Build Database Image') {
            steps {
                script {
                    dir('database') {
                        sh 'docker build -t my-mysql-db .'
                    }
                }
            }
        }

        stage('Build Backend') {
            steps {
                script {
                    dir('backend') {
                        sh 'docker build -t my-backend ./Backend'
                    }
                }
            }
        }

        stage('Deploy Database') {
            steps {
                script {
                    sh '''
                    docker network create my-net || true
                    docker rm -f mysql-db || true
                    docker run -d --name mysql-db --network my-net -p 8080:8080 my-mysql-db
                    '''
                }
            }
        }

        stage('Deploy Backend') {
            steps {
                script {
                    sh '''
                    docker rm -f my-backend || true
                    docker run -d --name my-backend --network my-net -p 8081:8081 my-backend:latest
                    '''
                }
            }
        }
		stage('Build Frontend') {
			steps {
				script {
					sh 'docker build -t my-frontend:latest ./Frontend'
				}
			}
		}

		stage('Deploy Frontend') {
			steps {
				script {
					sh 'docker rm -f frontend-container || true'
					sh 'docker run -d --name frontend-container -p 3000:3000 my-frontend:latest'
				}
			}
		}
	}
}
