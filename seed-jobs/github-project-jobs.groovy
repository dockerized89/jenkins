def github_api = new URL("https://api.github.com/users/dockerized89/repos")
def projects = new groovy.json.JsonSlurper().parse(githubApi.newReader())

projects.each {
  def job_name=it.name
  def github_name=it.full_name
  def git_url=it.ssh_url
  println "Creating Job ${job_name} for ${git_url}"

  job("GitHub-${job_name}") {
    logRotator(-1, 10)
    scm {
        github(github_name, 'master')
    }
    triggers {
        githubPush()
    }
  }
}

listView('My GitHub Jobs') {
    description('')
    filterBuildQueue()
    filterExecutors()
    jobs {
        regex(/GitHub-.*/)
    }
    columns {
        status()
        buildButton()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
    }
}
