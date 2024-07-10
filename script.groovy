def buildJar() {
    echo "building the application..."
    sh 'mvn package'
}

def test(){
    echo "testing..."
    sh 'mvn test'
}

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
        sh 'docker build -t zzhoho0110/java-maven-app:jma-1.1.2 .'
        sh "echo $PASSWORD | docker login -u $USERNAME --password-stdin"
        sh 'docker push zzhoho0110/java-maven-app:jma-1.1.2'
    }
}

def deployApp() {
    echo 'deploying the application...'
}

return this