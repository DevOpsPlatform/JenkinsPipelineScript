**Practice-1**:

        pipeline {
            /* insert Declarative Pipeline here */
        }
        
        ----------------------------------------------

        pipeline {

            agent any
               
            stages{
                    stage('Stage 1') {
                        steps {
                            // One or more steps need to be included within the steps block.
                            println "stage-1"
                        }
                    }
             }
       }
