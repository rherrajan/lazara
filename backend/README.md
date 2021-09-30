lazara
=========

Description
----------------------------------------------------
location based game


Build the dynamic backend
-------------------------

```bash
	cd backend
```
```bash
	mvn install; heroku local
```

view http://localhost:5000

Deploy the dynamic backend
-------------------------

deploy the static frontend

create heroku app

```bash
	cd backend
	heroku create lazara
```

connect heroku app with github
	https://dashboard.heroku.com/apps/lazara/deploy/github

enable automatic deploys

test manual deploy
	click "Deploy Branch" and watch the logs
	wait for "Your app was successfully deployed"

Open your deployed app
	https://lazara.herokuapp.com/systeminfo
