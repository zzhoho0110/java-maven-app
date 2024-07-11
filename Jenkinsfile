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
        stage("increment version") {
            steps {
                script {
                    gv.increment()
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
            steps {
                script {
                    gv.buildJar()
                }
            }
        }
        stage("build image") {
            steps {
                script {
                    gv.buildImage()
                }
            }
        }
        stage("deploy") {
            steps {
                script {
                    gv.deployApp()
                }
            }
        }
        stage("commit") {
            steps {
                script {
                    gv.commit()
                }
            }
        }
    }   
}
