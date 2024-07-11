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
    echo 'deploying the application...'
}

def commit() {
    echo "committing the version update..."
    withCredentials([usernamePassword(credentialsId: '8be61f3a-aed1-464e-8244-003123375044', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
        sh 'git config --global user.email "jenkins@example.com"'
        sh 'git config --global user.name "jenkins"'

        sh 'git status'
        sh 'git branch'
        sh 'git config --list'

        sh 'git remote set-url origin https://${USERNAME}:${PASSWORD}@github.com/zzhoho0110/java-maven-app.git'
        sh 'git add .'
        sh 'git commit -m "CI: version bump"'
        sh 'git push origin HEAD:jenkins-jobs'
    }
}

return this
