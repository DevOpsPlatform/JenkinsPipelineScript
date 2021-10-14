
### Skip the stage

    pipeline{
        agent any

        parameters{
            string(name: 'skipSonar', defaultValue: 'true', description: '')
            string(name: 'skipScan', defaultValue: 'true', description: '')
        }
        stages{
            stage('code checkout'){

                steps{
                    println "code checkout"
                }
            }
            stage('build'){

                steps{
                    println "build"
                }
            }
            stage('Sonar Analysis'){
                when {
                    expression { params.skipSonar != 'false' }
                }
                steps{
                    println "sonar analysis"
                }
            }
            stage('Security scan'){
                when {
                    expression { params.skipScan != 'false' }
                }
                steps{
                    println "security scanning"
                }
            }
            stage('deploy'){

                steps{
                    println "deploy"
                }
            }
            stage('Regression Test'){

                steps{
                    println "Regression Test"
                }
            }
        }
    }

Build#3: Default values as per the script

Build#4: skipSonar = true

Build#5: skipSonar = true & skipScan = true

Output:

  ![image](https://user-images.githubusercontent.com/24622526/137259652-39e4184d-bce3-4120-af79-c17b447802ba.png)
