### Script-1

    node{
        stage("build"){
            echo "build stage"
        }
        stage('Testing on linux') {
            node('linux'){
                stage('Linux') {
                    echo "running linux schell script"                }
            }
        }
        stage('Testing on Windows'){
            node('windows'){
                stage('Windows') {
                    echo "running windows batch script"
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
                node('linux'){
                    stage('Linux') {
                        echo "running linux schell script"
                    }
                }
                node('windows'){
                    stage('Windows') {
                        echo "running windows batch script"
                    } 
                }
        }
    }
