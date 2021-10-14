### Script

    node{
        stage("checkout"){
            echo "code checkout stage"
        }
        stage("build"){
            echo "build stage"
        }
        stage('Test') {
            parallel 'sonar': {
                stage('sonar') {
                    echo "running sonar analysis"
                }
            }, 'security scan': {
                stage('security scan') {
                    echo "running security scan script"
                }
            }
        }
    }
