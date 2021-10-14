### Script

    node{
        stage("build"){
            echo "build stage"
        }
        stage('Test') {
            parallel 'linux': {
                stage('Linux') {
                    echo "running linux schell script"
                }
            }, 'windows': {
                stage('Windows') {
                    echo "running windows batch script"
                }
            }
        }
    }
