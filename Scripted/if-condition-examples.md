
### Script-1

    node {
      stage('security scan') {
          if (params.skipScan == 'true') {
              echo 'skipping security scanning'
          } else {
              echo 'security scanning started....'
          }
      }
    }
  

### Script-2

    node {
      stage('security scan') {

          if (params.skipScan == 'true') {
              error('skipping security scanning, but this is mandatory, don't skip')
          } else {
              echo 'security scanning started....'
          }
      }
    }

