### Script

     node{
        stage("build"){
            echo "build stage"
            dir('build'){
                sh '''
                    pwd
                    ls -l
                    echo hello > hello.txt
                    ls -l
                '''
            }
            stash includes: 'build/*', name: 'buildOutPut'
        }
        stage('deploy') {
            node('linux'){
                stage('deploy'){
                    dir('deploy'){

                        unstash 'buildOutPut'

                        if(fileExists('build/hello.txt')){

                            env.WORKSPACE = pwd()
                            def hello = readFile "${env.WORKSPACE}/build/hello.txt"

                            println(hello)

                        }else{
                            println "File doest not exists"
                        }

                        sh '''
                            pwd
                            ls -l
                            cd build
                            ls -l
                        '''
                    }
                }
            }
        }
    }
