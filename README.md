## 1. Description
Technical Interview Repository Project for the Senior SDET position. __You are allowed
to use any technology that you have expressed on your resume to automate the APIs
and the UI. However, please create a feature branch and commit all of your code in here.__

:warning: __Do not add dependencies in any other repository than this one.__  

## 2. Installation

#### 2.1 GitHub Account

You must have a [GitHub](https://github.com/) account to access the technical interview dependencies. After you
have shared your GitHub account. You will be invited to
the interview repositories.

#### 2.2 NPM and Node.js Installation

Install Node.js version [v14.17](https://nodejs.org/en/download/) or earlier. (3 minutes)

Install the Angular CLI [v12.3.3](https://www.npmjs.com/package/@angular/cli) globally. (4 minutes)

#### 2.3 API setup.

The APIs are hosted using the npm package json-server.

1. Run ```git clone https://github.com/MLenneman/technical-interview-api.git``` or ```git clone git@github.com:MLenneman/technical-interview-api.git``` (1 minute)

2. Run ```npm install``` in project root. (3 minutes)

3. Follow the README.md instructions in technical-interview-api to start the server. (2 minutes)

#### 2.4 Angular setup.

A very simple UI was created to test your front-end automation knowledge.

1. Run ```git clone https://github.com/MLenneman/technical-interview-angular.git``` or ```git clone git@github.com:MLenneman/technical-interview-angular.git``` over SSH (1 minute)

2. Run ```npm install``` in project root. (1 minute)

3. Follow the README.md instructions in technical-interview-angular to start the server. (2 minutes)

## 3. Exam

#### 3.1 Git (3 minutes)

1. Create a feature branch from your name and the date e.g john-doe-2021-08-30.
2. Switch to that branch and use this as your development branch.

#### 3.2 API Automation (10 minutes)

You do not have to be exhaustive here. A positive and negative scenario for each API will suffice.

1. Write Automated API tests for GET /devices
2. Write Automation API tests for PUT /devices/:id
3. Write Automation API tests for POST /devices

#### 3.3 UI Automation (20 minutes)

By now you should have the technical-interview-api server started and the technical-interview-angular server started. This section will test basic knowledge of UI automation.

There are ids on __most__ of the critical items.

1. Navigate to the /grid route by clicking on the menu -> Grid icon. Write some functional tests for the table.

2. Navigate to the /widgets route by clicking on the menu -> Widgets icon. Write some functional tests for the Widgets.

3. Navigate to the /form route by clicking on the menu -> Form icon. Write some functional tests for the Form.

#### 3.4 Docker (10 minutes)

1. Create a Docker file for your testing application.

2. Explain concisely how you would go about getting a container from the Docker image running remotely. Add this file to your commits named ```deployment.txt```

#### 3.5 Commit.

1. Commit all of your ```source code```, the ```Dockerfile``` and the ```deployment.txt``` file just as you would in a real production environment. Be sure that the artifacts are
   set in a feature branch and not ```main```.
