def github_api = new URL("https://api.github.com/users/dockerized89/repos")
def projects = new groovy.json.JsonSlurper().parse(github_api.newReader())

projects.each {
	def job_name = it.name
	def github_name = it.full_name
	def github_url = it.ssh_url
	println "Creating job ${job_name} for GitHub project ${github_url}"

	job("Github-${job_name}") {
		logRotator(-1, 10)
		scm {
			github(github_name, 'master')
		}
		trigger {
			githubPush()
		}
	}
}

listView('My Github Jobs') {
	description('Seed job for setting up all my Github projects as jobs in Jenkins')
	filterBuildQueue()
	filterExecutors()
	jobs {
		regex(/Github-.*/)
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