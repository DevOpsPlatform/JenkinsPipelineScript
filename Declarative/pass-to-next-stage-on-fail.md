
### Script

    pipeline {
        agent any
        stages {
            stage('Stage-1') {
                steps {
                    sh 'exit 0'
                }
            }
            stage('Stage-2') {
                steps {
                    catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                        sh "exit 1"
                    }
                }
            }
            stage('Stage-3') {
                steps {
                    sh 'exit 0'
                }
            }
        }
    }
