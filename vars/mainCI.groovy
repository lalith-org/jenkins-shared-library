def call () {
    node('ci-server') {
        if (env.TAG_NAME ==~ ".*") {
            stage('build application') {
                print 'npm install'
            }
            stage('release') {
                print 'release'
            }
        } else {
            stage('lint code') {
                sh 'env'
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