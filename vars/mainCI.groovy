def call () {
    node('ci-server') {
        stage('CodeCheckout') {
            sh "find ."
            sh "find . | sed -e '1d' |xargs rm -rf"
            if(env.TAG_NAME ==~ ".*") {
                env.branch_name = "refs/tags/${env.TAG_NAME}"
            } else {
                env.branch_name = "${env.BRANCH_NAME}"
            }
            checkout scmGit(
                    branches: [[name: "${branch_name}"]],
                    userRemoteConfigs: [[url: "https://github.com/lalith-org/expense-${component}"]]
            )
        }
        if (env.TAG_NAME ==~ ".*") {
            stage('build application') {
                sh 'docker build -t 730335477956.dkr.ecr.us-east-1.amazonaws.com/expense-${component}:${TAG_NAME} .'
            }
            stage('release') {
                sh 'aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 730335477956.dkr.ecr.us-east-1.amazonaws.com'
                sh 'docker push 730335477956.dkr.ecr.us-east-1.amazonaws.com/expense-${component}:${TAG_NAME}'
            }
        } else {
            stage('lint code') {
                print 'lint code'
            }
            if (env.BRANCH_NAME != "main") {
                stage('code review') {
                    print 'sonarqube code review'
                }
                stage('unit tests') {
                    print 'unit tests'
                }
            }
            stage('integration tests') {
                print 'integration tests'
            }
        }
    }
}