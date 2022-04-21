def getDockerInfo(def dockerContainer){

    def dockerDetails = dockerContainer.split('\n')

    def table = """
            <head>
                <style>
                table {
                font-family: arial, sans-serif;
                border-collapse: collapse;
                width: 100%;
                }

                td, th {
                border: 1px solid #dddddd;
                text-align: left;
                padding: 8px;
                }

                tr:nth-child(even) {
                background-color: #dddddd;
                }
                </style>
            </head>
        <table>
    """
    for(node in dockerDetails){
        println "node from body method: "+node
        def nd = node.split("  +")
        table = table+"""
                    <tr>
                    """
        for(def i = 0;i<nd.size();i++){
            
            if(i == 0){
                println "Headers: ${nd[i]}"
                table = table+"""
                    <th>${nd[i]}</th>
                """
            }else{
                println "Rows: ${nd[i]}"
                table = table+"""
                    <td>${nd[i]}</td>
                """
            }
            
        }
        table = table+"""
                </tr>
            """
    }
    table = table+"""</table>"""

    return table
}

def topNLsize(def dirs){

    sh"""
        du -sh /var/lib/jenkins/*  | sort -hr | head -n ${dirs} > topdirs.txt
        echo "-----Top largest file in directory-----"    
    """
}

def findRemoveEar(){
    sizesEar()
    sh'''
        loc_to_look='/var/lib/jenkins/workspace'
        
        echo 'Removing all the EAR files from the path: ' $loc_to_look

        echo "" >> removedPaths.txt
        echo "Removing all the EAR files from the path: " $loc_to_look >> removedPaths.txt

        find $loc_to_look -type f -name "*.ear" >> removedPaths.txt
        
        find $loc_to_look -type f -name "*.ear" -exec rm -f {} +
        
    '''
}


def findRemoveWar(){
    sizesWar()
    sh'''
        loc_to_look='/var/lib/jenkins/workspace'
        
        echo 'Removing all the WAR files from the path: ' $loc_to_look

        echo "" >> removedPaths.txt
        echo "Removing all the WAR files from the path: " $loc_to_look >> removedPaths.txt

        find $loc_to_look -type f -name "*.war" >> removedPaths.txt

        find $loc_to_look -type f -name "*.war" -exec rm -f {} +
        
    '''
}

def findRemoveZip(){
    sizesZip()
    sh'''
        loc_to_look='/var/lib/jenkins/workspace'
        
        echo 'Removing all the ZIP files from the path: ' $loc_to_look

        echo "" >> removedPaths.txt
        echo "Removing all the ZIP files from the path: " $loc_to_look >> removedPaths.txt

        find $loc_to_look -type f -name "*.zip" >> removedPaths.txt
        
        find $loc_to_look -type f -name "*.zip" -exec rm -f {} +

    '''
}

def duplicateWorkspaces(){
    sh'''
        echo '================Duplicate workspaces created with @2, @3 under /var/lib/jenkins/workspace =============='

        loc_to_look='/var/lib/jenkins/workspace'

        file_list_at2=$(find $loc_to_look -type d -name "*@2")

        total_size_at2=$(du -ch $file_list_at2 | tail -1 | cut -f 1)

        echo '================================================================'
        echo '>>>>>>>>>>> Total size of at 2 workspace: ' $total_size_at2
        echo '================================================================'

        file_list_at3=$(find $loc_to_look -type d -name "*@3")

        total_size_at3=$(du -ch $file_list_at3 | tail -1 | cut -f 1)

        echo '================================================================'
        echo '>>>>>>>>>>> Total size of at 3 workspace: ' $total_size_at3
        echo '================================================================'

        echo ' ' >> earwarzip.txt
        echo '>>>>>>>>>>> Total size of @2 workspace directories: ' $total_size_at2 >> earwarzip.txt
        echo ' ' >> earwarzip.txt
        echo '>>>>>>>>>>> Total size of @3 workspace directories: ' $total_size_at3 >> earwarzip.txt
        echo ' ' >> earwarzip.txt

    '''
}

def sizesEar(){
    sh'''
       echo '================EAR Files list from the path /var/lib/jenkins/workspace =========================='
        
        loc_to_look='/var/lib/jenkins/workspace'

        file_list=$(find $loc_to_look -type f -name "*.ear")

        total_ear_size=$(du -ch $file_list | tail -1 | cut -f 1)

        echo '================================================================'
        echo '>>>>>>>>>>> Total size of all EAR files is: ' $total_ear_size
        echo '================================================================'

        echo ' ' >> earwarzip.txt
        echo '>>>>>>>>>>> Total size of all EAR files is: ' $total_ear_size >> earwarzip.txt
        echo ' ' >> earwarzip.txt
        
        echo '===Find the each ear path details above==='
        
        #find $loc_to_look -type f -name "*.ear"

    '''
}

def sizesWar(){
    sh'''
       echo '================WAR Files list from the path /var/lib/jenkins/workspace =========================='
        
        #find /var/lib/jenkins/workspace -type f -name "*-sources.jar"

        loc_to_look='/var/lib/jenkins/workspace'

        file_list=$(find $loc_to_look -type f -name "*.war")

        total_war_size=$(du -ch $file_list | tail -1 | cut -f 1)

        echo '================================================================'
        echo '>>>>>>>>>>> Total size of all WAR files is: ' $total_war_size
        echo '================================================================'
        
        echo ' ' >> earwarzip.txt
        echo '>>>>>>>>>>> Total size of all WAR files is: ' $total_war_size >> earwarzip.txt
        echo ' ' >> earwarzip.txt

        echo '===Find the each war path details above==='
        
        #file_list=$(find $loc_to_look -type f -name "*sources.jar")

        #total_sourceJar_size=$(du -ch $file_list | tail -1 | cut -f 1)

        #echo '================================================================'
        #echo '>>>>>>>>>>> Total size of all source files is: ' $total_sourceJar_size
        #echo '================================================================'

        #echo '===Find the each source jar path details above==='
        
        #find $loc_to_look -type f -name "*.war"

    '''
}

def sizesZip(){
    sh'''
       echo '================ZIP Files list from the path /var/lib/jenkins/workspace =========================='
        
        loc_to_look='/var/lib/jenkins/workspace'

        file_list=$(find $loc_to_look -type f -name "*.zip")

        total_zip_size=$(du -ch $file_list | tail -1 | cut -f 1)

        echo '================================================================'
        echo '>>>>>>>>>>> Total size of all ZIP files is: ' $total_zip_size
        echo '================================================================'

        echo ' ' >> earwarzip.txt
        echo '>>>>>>>>>>> Total size of all ZIP files is: ' $total_zip_size >> earwarzip.txt
        echo ' ' >> earwarzip.txt

        echo '===Find the each zip path details above==='
        
        #find $loc_to_look -type f -name "*.zip"

    '''
}

def sizeOfDir(def checkPath){
    sh"""
        if [ -d ${checkPath} ]; then
            echo "Size fo the given directory"
            du -sh ${checkPath} >> sizeDirPaths.txt

        else
        
            echo "Error: Directory ${checkPath} does not exists."  >> sizeDirPaths.txt

        fi
    """
}

def removingDir(def path){

    sh"""
        rm -rf ${path}
    """

}

node("${params.nodeName}"){
    stage("clean"){

        checkout scm
        
        println "Hello...from stage ${params.nodeName}" 
        println "===================== Input Values ======================="
        println ("nodeName: ${params.nodeName}")
        println ("sizepath: ${params.sizepath}")
        println ("earSize: ${params.earSize}")
        println ("warSize: ${params.warSize}")
        println ("zipSize: ${params.zipSize}")
        println ("removeEar: ${params.removeEar}")
        println ("removeWar: ${params.removeWar}")
        println ("removeZip: ${params.removeZip}")
        println ("sizeOfEachFolder: ${params.sizeOfEachFolder}")
        println ("topNlargest: ${params.topNlargest}")
        println ("dockerNode: ${params.dockerNode}")
        println ("duplicateWorkspace: ${params.duplicateWorkspace}")


        sh"""
            rm -rf sizeDirPaths.txt upace.txt sizeOfAllDir.txt earwarzip.txt removedPaths.txt
        """
        
        sh"""
            echo "Used space in this node"
            #df -h "/var/lib/jenkins/"

            df -h "/var/lib/jenkins/" > upace.txt
            
        """

        def checkPaths = "${params.sizepath}".split(" ")
        
        for (def checkPath: checkPaths) {
            println("checkPath: "+checkPath)
            sizeOfDir("/var/lib/jenkins/workspace/"+checkPath)
        }
        
        if("${params.removeSizepath}" == "true"){
            sh"""
                echo ""  >> removedPaths.txt
                echo "============ Removing the given paths ====================" >> removedPaths.txt
                echo ${params.sizepath} >> removedPaths.txt
                #rm -rf ${params.sizepath}
            """
            for (def removeDir: checkPaths) {
                println("Removing the directory: "+removeDir)
                removingDir("/var/lib/jenkins/workspace/"+removeDir)
            }
        }
        
        if("${params.sizeOfEachFolder}" == "true"){
            sh '''
                echo '================Size of each directory under the path /var/lib/jenkins/workspace=========================='
                sizeofEachDir=$(du -shc /var/lib/jenkins/workspace/*)
                echo ${sizeofEachDir} > sizeOfAllDir.txt
            '''
        }
        if("${params.duplicateWorkspace}" == "true"){
            duplicateWorkspaces()
        }
        
        
        def earsSize = ""
        def warsSize = ""
        def zipsSize = ""
        
        if("${params.removeEar}" == "true"){
            earsSize = "false"
            
            findRemoveEar()
            
        }
        
        if("${params.removeWar}" == "true"){
            warsSize = "false"
            
            findRemoveWar()
            
        }

        
        if("${params.removeZip}" == "true"){
            zipsSize = "false"
            
            findRemoveZip()
            
        }
        
        if("${params.removeEar}" == "false" && "${params.earSize}" == "true"){
            earsSize = "true"
        }
        if("${params.removeWar}" == "false" && "${params.warSize}" == "true"){
            warsSize = "true"
        }  
        if("${params.removeZip}" == "false" && "${params.zipSize}" == "true"){
            zipsSize = "true"
        }
        println ("earsSize: ${earsSize}")
        println ("warsSize: ${warsSize}")
        println ("zipsSize: ${zipsSize}")

        if("${earsSize}" == "true"){
        	sizesEar()
        }
        if("${warsSize}" == "true"){
        	sizesWar()
        }
        if("${zipsSize}" == "true"){
        	sizesZip()
        }

        topNLsize("${params.topNlargest}")

        println "subDirsList: ${params.subDirsList}"

        if("${params.subDirsList}" == "true"){

            sh"""
                echo Subdirectories and files details ${params.sizepath}
                ls -R /var/lib/jenkins/workspace/${params.sizepath}
            """
        }

        println "=======================Final Report on Jenkins node ${params.nodeName}======================="
        def emailBody = "=======================Final Report on Jenkins node ${params.nodeName}=======================<br><br>"
        

        echo "-----------------------------Input Values------------------------------"
        echo "Node name: ${params.nodeName}"
        echo "Given Paths: ${params.sizepath}"
        echo "removeSizepath: ${params.removeSizepath}"
        echo "earSize: ${params.earSize}"
        echo "warSize: ${params.warSize}"
        echo "zipSize: ${params.zipSize}"
        echo "sizeOfEachFolder: ${params.sizeOfEachFolder}"
        echo "removeEar: ${params.removeEar}"
        echo "removeWar: ${params.removeWar}"
        echo "removeZip: ${params.removeZip}"
        echo "topNlargest: ${params.topNlargest}"
        echo "dockerNode: ${params.dockerNode}"
        echo "------------------------------------------------------------------------"

        emailBody = emailBody + "-----------------------------Input Values------------------------------<br>"
        emailBody = emailBody + "Node name: ${params.nodeName}<br>"
        emailBody = emailBody + "Given Paths: ${params.sizepath}<br>"
        emailBody = emailBody + "removeSizepath: ${params.removeSizepath}<br>"
        emailBody = emailBody + "earSize: ${params.earSize}<br>"
        emailBody = emailBody + "warSize: ${params.warSize}<br>"
        emailBody = emailBody + "zipSize: ${params.zipSize}<br>"
        emailBody = emailBody + "sizeOfEachFolder: ${params.sizeOfEachFolder}<br>"
        emailBody = emailBody + "removeEar: ${params.removeEar}<br>"
        emailBody = emailBody + "removeWar: ${params.removeWar}<br>"
        emailBody = emailBody + "removeZip: ${params.removeZip}<br>"
        emailBody = emailBody + "topNlargest: ${params.topNlargest}<br>"
        emailBody = emailBody + "------------------------------------------------------------------------<br><br>"

        echo "--------------------------Space utilization----------------------------"
        println readFile('upace.txt')
        echo "------------------------------------------------------------------------"
        echo "--------------------------Size of given paths---------------------------"
        println readFile('sizeDirPaths.txt')

        emailBody = emailBody + "--------------------------Space utilization----------------------------<br>"
        def filePath = readFile('upace.txt')
        def lines = filePath.readLines() 

        for (line in lines) {
            emailBody = emailBody + "     "+line+"<br>"  
        }
        emailBody = emailBody + "------------------------------------------------------------------------<br><br>"

        emailBody = emailBody + "--------------------------Size of given paths---------------------------<br>"
        filePath = readFile('sizeDirPaths.txt')
        lines = filePath.readLines() 

        for (line in lines) {
            emailBody = emailBody + "     "+line+"<br>"            
        }
        emailBody = emailBody + "------------------------------------------------------------------------<br><br>"

        if("${params.removeSizepath}" == "true"){
            println "--------------------Remove given paths ${params.sizepath}--------"
            println "You choosen to remove the path(s) ${params.sizepath}"

            emailBody = emailBody + "--------------------Remove given paths ${params.sizepath}--------<br>"
            emailBody = emailBody + "You choosen to remove the path(s) ${params.sizepath}, see the attached file removedPaths.txt <br>"
            emailBody = emailBody + "------------------------------------------------------------------------<br><br>"

        }

        if("${params.sizeOfEachFolder}" == "true"){
            println "-----Size of each directory under the path /var/lib/jenkins/workspace-----"
            println readFile('sizeOfAllDir.txt')

            emailBody = emailBody + "-----Size of each directory under the path /var/lib/jenkins/workspace-----<br>"
            filePath = readFile('sizeOfAllDir.txt')
            lines = filePath.readLines() 

            for (line in lines) {
                emailBody = emailBody + "     "+line+"<br>"            
            }
            emailBody = emailBody + "------------------------------------------------------------------------<br><br>"
        }

        println ("earSize: ${params.earSize}")
        println ("warSize: ${params.warSize}")
        println ("zipSize: ${params.zipSize}")
        println ("removeEar: ${params.removeEar}")
        println ("removeWar: ${params.removeWar}")
        println ("removeZip: ${params.removeZip}")

        if("${params.earSize}" == "true" || "${params.warSize}" == "true" || "${params.zipSize}" == "true"){
            println readFile('earwarzip.txt')

            filePath = readFile('earwarzip.txt')
            lines = filePath.readLines() 

            for (line in lines) {
                emailBody = emailBody + "     "+line+"<br>"            
            }
            emailBody = emailBody + "------------------------------------------------------------------------<br><br>"
        }
        if("${params.removeEar}" == "true" || "${params.removeWar}" == "true" || "${params.removeZip}" == "true"){
            println "You choosen to remove ear|war|zip files. See above console output"

        }

        echo "--------------------------Top ${params.topNlargest} largest directories--------------------------"
        println readFile('topdirs.txt')
        emailBody = emailBody + "--------------------------Top ${params.topNlargest} largest directories--------------------------<br>"
        filePath = readFile('topdirs.txt')
        lines = filePath.readLines() 

        for (line in lines) {
            emailBody = emailBody + "     "+line+"<br>"            
        }
        emailBody = emailBody + "------------------------------------------------------------------------<br><br>"

        if("${params.dockerNode}" == "true"){

                println("${params.dockerNode}")

                def dockerContainer=sh(returnStdout:true , script:"docker ps -a -s")

                if(dockerContainer.contains("Created") || dockerContainer.contains("Exited") || dockerContainer.contains("Up")){
                    created = dockerContainer.count("Created")
                    exited = dockerContainer.count("Exited")
                    up = dockerContainer.count("Up")

                    println("There are created/exited/up containers in the slave: "+created+"/"+exited+"/"+up)
                }

                println("------------------Docker containers Details-----------------")
                emailBody = emailBody + "------------------Docker containers Details-----------------<br><br>"
                println(dockerContainer)
                def dockerContainerInfo = getDockerInfo(dockerContainer)
                emailBody = emailBody + dockerContainerInfo + "<br>" 

                def dockerImages=sh(returnStdout:true , script:"docker images -a")
                println("------------------Docker images Details-----------------")
                emailBody = emailBody + "------------------Docker images Details-----------------<br><br>"
                println(dockerImages)
                def dockerImagesInfo = getDockerInfo(dockerImages)
                emailBody = emailBody + dockerImagesInfo + "<br>" 

                def dockerSystemInfo=sh(returnStdout:true , script:"docker system df")
                println("------------------Docker System Info Details-----------------")
                emailBody = emailBody + "------------------Docker System Info Details-----------------<br><br>"
                println(dockerSystemInfo)
                def dockerSystemsInfo = getDockerInfo(dockerSystemInfo)
                emailBody = emailBody + dockerSystemsInfo + "<br>" 

                def dkInf = dockerSystemInfo.split( '\n' )
                println(dkInf[1])
                def r1 = dkInf[1].split()
                println("Type: "+r1[0])
                println("Image Size: "+r1[3])

                println(dkInf[2])
                def r2 = dkInf[2].split()
                println("Type: "+r2[0])
                println("Container Size: "+r2[3])
            }

        if("${params.removeEar}" == "true" || "${params.removeWar}" == "true" || "${params.removeZip}" == "true" || "${params.removeSizepath}" == "true"){

            emailBody = emailBody + "You choosen to remove either ear|war|zip files. See the console output from ${env.BUILD_URL}<br>"
            emailBody = emailBody + "------------------------------------------------------------------------<br><br>"

            sh"""
                echo "After removal the Used space in this node"
                #df -h "/var/lib/jenkins/"

                df -h "/var/lib/jenkins/" > afterupace.txt

                cat afterupace.txt
                
            """

            emailBody = emailBody + "--------------------------Space utilization after removal----------------------------<br>"
            filePath = readFile('afterupace.txt')
            lines = filePath.readLines() 

            for (line in lines) {
                emailBody = emailBody + "     "+line+"<br>"  
            }
            emailBody = emailBody + "------------------------------------------------------------------------<br><br>"
            
            emailext attachmentsPattern: 'removedPaths.txt', body: emailBody, subject: "${env.JENKINS_URL} ${params.nodeName} files removed", mimeType: 'text/html', from: "info@email.com", to: "EMAIL1@email.com email2@email.com"
        }else{
            emailext body: emailBody, subject: "${env.JENKINS_URL} ${params.nodeName} filesystem", mimeType: 'text/html', from: "info@email.com", to: "toemail@email.com"
        }
    }
}
