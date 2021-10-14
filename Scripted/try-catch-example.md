### Script-1


    node {
      stage('security scan') {

          try {
              if (params.skipScan == 'true') {
                  error('skipping security scanning, but this is mandatory, dont skip')
              } else {
                  echo 'security scanning started....'
              }
          }catch (exc) {
                echo 'Something failed!'
            }
      }
    }


### Script-2


    node {
      stage('security scan') {

          try {
              if (params.skipScan == 'true') {
                  error('skipping security scanning, but this is mandatory, dont skip')
              } else {
                  echo 'security scanning started....'
              }
          }catch (Exception e) {
                echo 'Exception occurred: ' + e.toString()
            }
      }
    }
