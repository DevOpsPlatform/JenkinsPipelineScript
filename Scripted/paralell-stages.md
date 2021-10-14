### Script-1

    node{
        stage("checkout"){
            echo "code checkout stage"
        }
        stage("build"){
            echo "build stage"
        }
        stage('code quality') {
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

### Script-2

    node{
        stage("build"){
            echo "build stage"
        }
        stage('Test') {
            parallel 'linux': {
                node('linux'){
                    stage('Linux') {
                        echo "running linux schell script"
                    }
                }
            }, 'windows': {
                node('windows'){
                    stage('Windows') {
                        echo "running windows batch script"
                    } 
                }
            }
        }
    }
