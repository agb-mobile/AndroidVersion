pipeline {
  agent any
  stages {
    stage('Check') {
      steps {
        sh './gradlew check'
      }
    }

    stage('Publish') {
      when {
        branch "master"
      }

      steps {
        sh './gradlew clean publishPlugins'
      }
    }
  }
}