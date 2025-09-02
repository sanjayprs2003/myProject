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

		stage('Build Backend') {
			steps {
				script {
					sh 'docker build -t my-backend:latest ./Backend'
				}
			}
		}

		stage('Deploy Backend') {
			steps {
				script {
					sh 'docker rm -f backend-container || true'
					sh 'docker run -d --name backend-container -p 8081:8081 my-backend:latest'
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
