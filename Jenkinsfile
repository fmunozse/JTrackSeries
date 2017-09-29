def DOCKER_IMAGE="fmunozse/jtrackseries"
def GIT_PROJECT="github.com/fmunozse/JTrackSeries.git"
def GIT_USER_EMAIL="ci.fmunoze@gmail.com"
def GIT_USER_NAME="ci-fmunozse"
def credentialsId_git = "GIT_USERPWD"
def credentialsId_docker = "DOCKER_USERPWD"

node {
    // uncomment these 2 lines and edit the name 'node-4.4.7' according to what you choose in configuration
    def nodeHome = tool name: 'node-4.4.7', type: 'jenkins.plugins.nodejs.tools.NodeJSInstallation'
    env.PATH = "${nodeHome}/bin:${env.PATH}"
    def workspace = pwd();

    stage('check tools') { 
        sh "node -v"
        sh "npm -v"
        sh "bower -v"
        sh "gulp -v"
    }

    stage('checkout') {
        checkout scm
    }

    stage('npm install') {
        sh "npm install"
    }

    stage('clean') {
        sh "./mvnw clean"
        sh "git checkout ${workspace}/pom.xml"
    }

    //versions
    sh 'git rev-parse HEAD > GIT_COMMIT'
    def commitNumber = readFile('GIT_COMMIT').trim()
    def pomv = version();
    def buildVersion = "${pomv}_${env.BUILD_ID}"
    def dockerTag = "${DOCKER_IMAGE}:${buildVersion}"

    stage ('Set Version') {
        echo "Running ${buildVersion} - CommitNumber: ${commitNumber} on ${workspace}. dockerTag: ${dockerTag}. BranchName: ${env.BRANCH_NAME}"
        sh "./mvnw -B versions:set -DgenerateBackupPoms=false -DnewVersion=${buildVersion}"
    }


    stage('backend tests') {
        try {
            sh "./mvnw test"
        } catch(err) {
            //throw err
        } finally {
            step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
        }
    }

    stage('packaging') {
        sh "./mvnw package -Pprod -DskipTests"
    }

    stage ('versioning and docker?') {
        timeout(time: 1, unit: 'HOURS') {
          input 'Generate docker version and push?'
        }
    }

    stage ('Set GitHub Version') {
        withCredentials([[$class: 'UsernamePasswordMultiBinding',
                      credentialsId: credentialsId_git,
                      usernameVariable: 'GIT_USERNAME',
                      passwordVariable: 'GIT_PASSWORD']]) {

            sh "git config user.email '${GIT_USER_EMAIL}'"
            sh "git config user.name '${GIT_USER_NAME}'"
            sh "git tag -a ${buildVersion} -m 'Raise version ${buildVersion}'"
            sh "git push https://${env.GIT_USERNAME}:${env.GIT_PASSWORD}@${GIT_PROJECT} --tags"

        }
    }

    stage('creating docker') {
        sh "./mvnw -Pprod docker:build"

        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: credentialsId_docker, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
            sh "docker login --username $USERNAME --password $PASSWORD"
        }

        echo "pushing to ${DOCKER_IMAGE}:latest"
        sh "docker push ${DOCKER_IMAGE}:latest"

        sh "docker tag ${DOCKER_IMAGE}:latest ${dockerTag}"

        echo "pushing to ${dockerTag}"
        sh "docker push ${dockerTag}"

    }


}

//getting the 2º tag <version>
def version() {
    String path = pwd();
    def matcher = readFile("${path}/pom.xml") =~ '<version>(.+)</version>'
    return matcher ? matcher[1][1] : null
}
