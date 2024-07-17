def buildJar() {
    echo "building the application..."
    sh 'mvn clean package'
} 

def increment(){
   echo 'incrementing app version...'
   sh 'mvn build-helper:parse-version versions:set \
      -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
       versions:commit'
   def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
   def version = matcher[0][1]
   env.IMAGE_NAME = "$version-$BUILD_NUMBER"
}

def test(){
   echo "testing..."
   sh 'mvn test'
} 

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
        sh "docker build -t zzhoho0110/java-maven-app:${IMAGE_NAME} ."
        sh "echo $PASSWORD | docker login -u $USERNAME --password-stdin"
        sh "docker push zzhoho0110/java-maven-app:${IMAGE_NAME}"
    }
} 


def deployApp() {
    def dockerCmd = "docker run -p 8080:8080 -d zzhoho0110/java-maven-app:${IMAGE_NAME}"
    echo 'deploying the application...'
    sshagent(['ec2-server-key']){
        sh "ssh -o StrictHostKeyChecking=no ec2-user@57.180.34.187 ${dockerCmd}"
    }
}

def commit() {
    echo "committing the version update..."
    withCredentials([usernamePassword(credentialsId: 'github-access-token', passwordVariable: 'TOKEN', usernameVariable: 'USERNAME')]) {
        sh 'git config --global user.email "jenkins@example.com"'
        sh 'git config --global user.name "jenkins"'

        sh 'git status'
        sh 'git branch'
        sh 'git config --list'

        sh "git remote set-url origin https://${TOKEN}@github.com/${USERNAME}/java-maven-app.git"
        sh 'git add .'
        sh 'git commit -m "CI: version bump"'
        sh 'git push origin HEAD:jenkins-jobs'
    }
}

return this
