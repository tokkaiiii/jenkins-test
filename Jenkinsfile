pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/tokkaiiii/jenkins-test.git'
            }
            post {
                success {
                    sh 'echo "Successfully Cloned Repository"'
                }
                failure {
                    sh 'echo "Fail Cloned Repository"'
                }
            }
        }

        stage('Build') {
            steps {
                // gradlew이 있어야됨. git clone해서 project를 가져옴.
                sh 'chmod +x gradlew'
                sh './gradlew clean build'
                sh 'ls -al ./build'
            }
            post {
                success {
                    echo 'gradle build success'
                }
                failure {
                    echo 'gradle build failed'
                }
            }
        }

        stage('Test') {
            steps {
                echo '테스트 단계와 관련된 몇 가지 단계를 수행합니다.'
            }
        }

        stage('Deploy') {
            steps {
                sshagent(credentials: ['ssh_connection']) {
                    sh '''
                        ssh -o StrictHostKeyChecking=no root@172.17.0.3 uptime
                        scp /var/jenkins_home/workspace/jenkins-test-01/build/libs/jenkinstest-0.0.1-SNAPSHOT.jar root@172.17.0.3:/root/
                    '''
                }
            }
            post {
                success {
                    // 원격 서버에서 Ansible 명령을 실행
                    sshagent(credentials: ['ssh_connection']) {
                        sh '''
                            echo "Ansible create image"
                            ssh -o StrictHostKeyChecking=no root@172.17.0.3 "ansible-playbook create-image-playbook.yml"
                            echo "Image run Start"
                            ssh -o StrictHostKeyChecking=no root@172.17.0.3 "ansible-playbook create-container-playbook.yml"
                        '''
                    }
                }
                failure {
                    sshagent(credentials: ['ssh_connection']) {
                        sh '''
                            echo "Error: The deployment process has failed."
                            echo "Ansible playbook failed to execute or the SCP command did not succeed."
                            echo "Please verify the remote server's status and the playbook configuration."
                            echo "Detailed logs are available in the Jenkins job output."
                        '''
                    }
                }
            }
        }
    }
}
