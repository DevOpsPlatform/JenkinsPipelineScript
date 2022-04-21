#!/usr/bin/env groovy

import jenkins.model.Jenkins

def getBody(def nodeDetails, def space){
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

    <table border="1">
    <tr>
        <th>Node Name</th>
        <th>Labels</th>
        <th>Used Space</th>
        <th>Size</th>
        <th>Containers</th>
        <th>Tools and Versions<br>(NI = Not Installed)</th>
    </tr>
    """
    for(node in nodeDetails){
        println "node from body method: "+node
        def nd = node.split(":")
        nd[1] = nd[1].replaceAll("\\[","").replaceAll("\\]","").trim()
        //nd[2] = nd[2].replaceAll("used space is","").trim()
        table = table+"""
        <tr>
            <td>${nd[0]}</td>
            <td>${nd[1]}</td>
            <td>${nd[2]}</td>
            <td>${nd[3]}</td>
            <td>${nd[4]}</td>
            <td>${nd[5]}</td>
        </tr>
        """
    }
    table = table+"""</table>"""

    def body = """
        <br><br>
        <b>Goal</b>:<br>
        ${env.JENKINS_URL} - Linux Nodes details which are used more than or equal to ${space}% space for Jenkins workspace
        <br><br><br>
        <b>Details</b>:<br>
        ${table}
        <br><br><br>
        <b>Note</b>:
        This is an auto-generated email from Jenkins job, please find more details at ${BUILD_URL}.
        <br><br><br>
        <br>
        SDPOS(S3Y)
    """

    return body
}

def getNodes(def space){
    def count = 1
    def nodeDetails = []
    def details = ""
    Jenkins.instance.computers.each{ node -> 
            println (count+". "+node.getDisplayName()+" : "+node.getAssignedLabels())
            details = size(node.getDisplayName(), node.getAssignedLabels().toString(),space,count)
            count = count + 1

            if(!details.isEmpty() && details !=null){
                nodeDetails.add(details)
            }
    }

    println "Total Nodes: "+count
    println "Linux Nodes details which are used more than ${space}% space for Jenkins workspace: "+nodeDetails
    
    if(!nodeDetails.isEmpty() && nodeDetails !=null){
        def body = getBody(nodeDetails,space)
        emailext body: body, subject: "${env.JENKINS_URL} - Linux Jenkins Nodes details used more than ${space}% space and containers details", mimeType: 'text/html', from: "info@email.com", to: "user.one@email.com user2.email.com"
    }
}

def onlineNode(def nodeName){
    def isonline = false
    if(nodeName.contains("master")){
        isonline = true
    }else if(hudson.model.Hudson.instance.getNode(nodeName).toComputer().isOnline()){
        isonline = true
    }else{
        isonline = false
    }

    return isonline
}

def size(def nodeName, def nodeLabels,def space,def count){
    def sendEmail = false
    def details = ""

    if (onlineNode(nodeName)) {

        println "node is online"

        node(nodeName){
            println "########################${count}. START - DETAILS on the NODE ${nodeName}########################"
            if (isUnix()) {
                    def created = 0
                    def exited = 0
                    def up = 0
                    def imagesSize = 0
                    def conatinerSize = 0
                if(nodeLabels.contains("docker")){
                    //sh 'docker ps -a -s'
                    //sh 'docker rm $(docker ps -a -f status=exited -q)'
                    //sh 'docker rm $(docker ps -a -f status=Created -q)'
                    //sh 'docker rm $(docker ps -aq) -f'

                    def dockerContainer=sh(returnStdout:true , script:"docker ps -a -s")

                    println "dockerContainer: "+dockerContainer

                    dockerContainer = dockerContainer.toString()

                    if(dockerContainer.contains("Created") || dockerContainer.contains("Exited") || dockerContainer.contains("Up")){
                        created = dockerContainer.count("Created")
                        exited = dockerContainer.count("Exited")
                        up = dockerContainer.count("Up")

                        println("There are created/exited/up containers in the slave: "+created+"/"+exited+"/"+up)

                        def dockerSystemInfo=sh(returnStdout:true , script:"docker system df")
                        println("------------------Docker System Info Details-----------------")
                        println(dockerSystemInfo)

                        def dkInf = dockerSystemInfo.split( '\n' )
                        println(dkInf[1])
                        def r1 = dkInf[1].split()
                        println("Type: "+r1[0])
                        println("Image Size: "+r1[3])
                        imagesSize = r1[3]

                        println(dkInf[2])
                        def r2 = dkInf[2].split()
                        println("Type: "+r2[0])
                        println("Container Size: "+r2[3])

                        conatinerSize = r2[3]
                    }
                }
                
                def dockerVersion=sh(returnStdout:true , script:"set +e && docker version --format '{{.Server.Version}}' ;set -e;echo ").trim()
                
                def javaVersion=sh(returnStdout:true , script:"set +e && java --version | head -1 | cut -f2 -d' ' ;set -e;echo ").trim()
                
                def npmVersion=sh(returnStdout:true , script:"set +e && npm -version ;set -e;echo ").trim()
                
                def nodeVersion=sh(returnStdout:true , script:"set +e && node --version ;set -e;echo ").trim()
                
                def gradleVersion=sh(returnStdout:true , script:"set +e && /opt/gradle/gradle-4.10.2/bin/gradle -version | grep Gradle | head -1 | cut -f2 -d' ' ;set -e;echo ").trim()
                
                def mavenVersion=sh(returnStdout:true , script:"set +e && mvn -v | head -1 | cut -f3 -d' ' ;set -e;echo ").trim()

                if(dockerVersion.size()==0){
                    dockerVersion="NI"
                }
                if(javaVersion.size()==0){
                    javaVersion="NI"
                }
                if(npmVersion.size()==0){
                    npmVersion="NI"
                }
                if(nodeVersion.size()==0){
                    nodeVersion="NI"
                }
                if(gradleVersion.size()==0){
                    gradleVersion="NI"
                }
                if(mavenVersion.size()==0){
                    mavenVersion="NI"
                }
                println("dockerVersion: "+dockerVersion)
                println("javaVersion: "+javaVersion)
                println("npmVersion: "+npmVersion)
                println("nodeVersion: "+nodeVersion)
                println("gradleVersion: "+gradleVersion)
                println("mavenVersion: "+mavenVersion)

                /*
                javaVersion=$(java --version)
                dockerVersion=$(docker version)
                npmVersion=$(npm -version)
                nodeVersion=$(node --version)
                gradleVersion=$(gradle -version)
                mavenVersion=$(mvn -v)

                echo dockerVersion: $dockerVersion
                echo javaVersion: $javaVersion
                echo npmVersion: $npmVersion
                echo gradleVersion: $gradleVersion
                echo mavenVersion: $mavenVersion
                */
                
                sh 'df -h /var/lib/jenkins'
                //sh 'df --output=avail -h "$PWD" | tail -n 1'

                def sizeOfEachDir = "${params.sizeOfEachFolder}"

                println "sizeOfEachFolder: ${params.sizeOfEachFolder}"

                if("${params.sizeOfEachFolder}" == "true"){
                    sh '''
                        echo '================Size of each directory under the path /var/lib/jenkins/workspace========================== '
                        dir_test="/var/lib/jenkins/workspace/"

                        if [ -d "$dir_test" ] && [ -x "$dir_test" ]; then
                            du -shc /var/lib/jenkins/workspace/*
                        fi
                    '''
                }

                sh '''

                    echo '================Used and Available space========================== '
                    df -h /var/lib/jenkins

                    #manualJobs=$(du -sh /var/lib/jenkins/workspace/Manual_Reusable_Jobs)
                    #downloadEars=$(du -sh /var/lib/jenkins/workspace/download_ear)

                    size=$(df -Ph  /var/lib/jenkins | tail -1 | awk '{print $2}')
                    echo ${size} > sizes.txt

                    used=$(df -Ph  /var/lib/jenkins | tail -1 | awk '{print $3}')
                    echo ${used} > useds.txt

                    avail=$(df -Ph  /var/lib/jenkins | tail -1 | awk '{print $4}')
                    echo ${avail} > avails.txt

                    useP=$(df -Ph  /var/lib/jenkins | tail -1 | awk '{print $5}')
                    echo ${useP} > usp.txt

                    dir_test="/var/lib/jenkins/workspace"

                    if [ -d "$dir_test" ] && [ -x "$dir_test" ]; then
                        

                        echo '================EAR Files list from the path /var/lib/jenkins/workspace =========================='
                        find /var/lib/jenkins/workspace -type f -name "*.ear"

                        loc_to_look='/var/lib/jenkins/workspace'

                        file_list=$(find $loc_to_look -type f -name "*.ear")

                        total_ear_size=$(du -ch $file_list | tail -1 | cut -f 1)

                        echo '>>>>>>>>>>> Total size of all EAR files is: '$total_ear_size

                        echo '================WAR Files list from the path /var/lib/jenkins/workspace =========================='
                        find /var/lib/jenkins/workspace -type f -name "*.war"

                        loc_to_look='/var/lib/jenkins/workspace'

                        file_list=$(find $loc_to_look -type f -name "*.war")

                        total_war_size=$(du -ch $file_list | tail -1 | cut -f 1)

                        echo '>>>>>>>>>>> Total size of all WAR files is: '$total_war_size


                        echo '================JAR Files list from the path /var/lib/jenkins/workspace =========================='
                        #find /var/lib/jenkins/workspace -type f -name "*.jar"

                        #loc_to_look='/var/lib/jenkins/workspace'

                        #file_list=$(find $loc_to_look -type f -name "*.jar")

                        #total_size=$(du -ch $file_list | tail -1 | cut -f 1)

                        #echo '>>>>>>>>>>> Total size of all JAR files is: '$total_size


                        echo '================ZIP Files list from the path /var/lib/jenkins/workspace =========================='
                        find /var/lib/jenkins/workspace -type f -name "*.zip"

                        loc_to_look='/var/lib/jenkins/workspace'

                        file_list=$(find $loc_to_look -type f -name "*.zip")

                        total_zip_size=$(du -ch $file_list | tail -1 | cut -f 1)

                        echo '>>>>>>>>>>> Total size of all ZIP files is: '$total_zip_size

                        if [ -d /var/lib/jenkins/workspace/Manual_Reusable_Jobs ]; then
                            echo "Size fo the given directory"
                            manualJobs=$(du -sh /var/lib/jenkins/workspace/Manual_Reusable_Jobs)
                            echo ${manualJobs} > manualJob.txt
                            echo "Size fo the given directory: ${manualJobs}"

                        else
                        
                            echo "Error: Directory /var/lib/jenkins/workspace/Manual_Reusable_Jobs does not exists."
                            echo 0 > manualJob.txt

                        fi

                        if [ -d /var/lib/jenkins/workspace/download_ear ]; then
                            echo "Size fo the given directory"
                            downloadEars=$(du -sh /var/lib/jenkins/workspace/download_ear)
                            echo ${downloadEar} > downloadEar.txt
                            echo "Size fo the given directory: ${downloadEars}"

                        else
                        
                            echo "Error: Directory /var/lib/jenkins/workspace/download_ear does not exists."
                            echo 0 > downloadEar.txt

                        fi

                        echo 'total_ear_size:' ${total_ear_size}
                        echo ${total_ear_size} > ear.txt
                        echo 'total_war_size:' ${total_war_size}
                        echo ${total_war_size} > war.txt
                        echo 'total_zip_size:' ${total_zip_size}
                        echo ${total_zip_size} > zip.txt

                        #cat ${env.WORKSPACE}/uspace.txt

                    else
                        echo $dir_test doest NOT exist in this node.
                        echo 0 > ear.txt
                        echo 0 > war.txt
                        echo 0 > zip.txt
                        echo 0 > downloadEar.txt
                        echo 0 > manualJob.txt
                    fi

                '''

                def ear = readFile('ear.txt').trim()
                println "ear size: "+ear
                def war = readFile('war.txt').trim()
                println "war size: "+war
                def zip = readFile('zip.txt').trim()
                println "zip size: "+zip
                def manualJobs = readFile('manualJob.txt').trim().split(" ")
                def mnlJobs = manualJobs[0]
                println "manualJobs size: "+manualJobs
                def downloadEars = readFile('downloadEar.txt').trim().split(" ")
                def dwnldEar = downloadEars[0]
                println "downloadEars size: "+dwnldEar
                
                def size = readFile('sizes.txt').trim()
                println "size: "+size
                def used = readFile('useds.txt').trim()
                println "used: "+used
                def avail = readFile('avails.txt').trim()
                println "avail: "+avail
                def useP = readFile('usp.txt').trim()
                println "useP: "+useP
                usSpace = useP.replaceAll("%","")
                println "usSpace: "+usSpace
                def uSpace = usSpace.toInteger()
                usdSpace = "Size("+size+")-Used("+used+")-Avail("+avail+")-Use%("+useP+")"
                if(uSpace >= space || (created>0 || exited>0 ||up>0)){
                    //def nodeNameStatus = nodeName+"-"+online"
                    details = nodeName+" : "+nodeLabels+" : "+usdSpace+" : earSize("+ear+")-warSize("+war+")-zipSize("+zip+")-dwnldEar("+dwnldEar+")-mnlJobs("+mnlJobs+") : Created("+created+")-Exited("+exited+")-Up("+up+")-imagesSize("+imagesSize+")-conatinerSize("+conatinerSize+") : dockerVersion("+dockerVersion+")-javaVersion("+javaVersion+")-npmVersion("+npmVersion+")-nodeVersion("+nodeVersion+")-gradleVersion("+gradleVersion+")-mavenVersion("+mavenVersion+")"

                }
            } else {
                //https://www.howtogeek.com/363639/how-to-use-the-dir-command-in-windows/
                bat"""

                    if exist "C:\\Jenkins\\workspace" ( dir "C:\\Jenkins\\workspace" ) ELSE ( echo C:\\Jenkins\\workspace Folder doesnot exists )

                    if exist "C:\\Jenkins\\workspace" ( dir "C:\\Jenkins\\workspace" /S) ELSE ( echo C:\\Jenkins\\workspace Folder doesnot exists )
                
                """
            }
            println "########################${count}. END - DETAILS on the NODE ${nodeName}########################"
        }

    }else{
        println nodeName + " node is offline"
        def nodeNameStatus = nodeName+"-offline"

        details = nodeNameStatus+": offline : offline : offline : offline : offline"
    }

    return details
}

pipeline {
    agent any

    parameters { 
        string(name: 'space', defaultValue: '0', description: 'Enter the value to get the list of nodes used this much of space')
        booleanParam(name: 'sizeOfEachFolder', defaultValue: true, description: 'size of each folder under the directory /var/lib/jenkins/workspace')
    }

    stages {
        stage("Print Node Details"){
            steps {
                script{
                    def spac = params.space
                    spac.toInteger()
                    getNodes(spac.toInteger())
                }
            }
        }
    }
    post {
        failure {
            emailext body: '''${SCRIPT, template="groovy-html.template"}''', 
                    subject: "${env.JENKINS_URL} ${env.JOB_NAME} - Build # ${env.BUILD_NUMBER} - Failed", 
                    mimeType: 'text/html', from: "info@email.com", to: "user.one@email.com user.two@email.com"
        }    
    }
}
