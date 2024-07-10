@Library('jenkins-shared-library')
def gv

pipeline {
    agent any
    tools {
        maven 'maven-3.9.8'
    }
    stages {
        stage("init") {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }
        stage("test"){
           steps{
               script {
                   gv.test()
               }
           }
        }
        stage("build jar") {
            when {
                expression {
                    BRANCH_NAME == 'master' || BRANCH_NAME == 'jenkins-shared-lib'
                }
            }
            steps {
                script {
                    buildJar()
                }
            }
        }
        stage("build image") {
            when {
                expression {
                    BRANCH_NAME == 'master' || BRANCH_NAME == 'jenkins-shared-lib'
                }
            }
            steps {
                script {
                    buildImage('zzhoho0110/java-maven-app:jma-1.1.3')
                }
            }
        }
        stage("deploy") {
            when {
                expression {
                    BRANCH_NAME == 'master' ||  BRANCH_NAME == 'jenkins-shared-lib'
                }
            }
            steps {
                script {
                    gv.deployApp()
                }
            }
        }
    }   
}
