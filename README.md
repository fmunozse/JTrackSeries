# JTrackSeries

Application created to keep the tracking of your favorites series.

This application was generated using JHipster, you can find documentation and help at [https://jhipster.github.io](https://jhipster.github.io). Created using JHipster. It's a [Yeoman generator](http://yeoman.io/), used to create a [Spring Boot](http://projects.spring.io/spring-boot/) + [AngularJS project](https://angularjs.org/). Used [api-thetvdb](https://github.com/Omertron/api-thetvdb) by Omertron for scratch series from [TvDb](http://thetvdb.com/).

## Main enhancements
Basically, the system can create your series and its episodes when is aired, and it is possible to mark the episode like viewed or not.
![serie-detail](https://raw.githubusercontent.com/fmunozse/JTrackSeries/master/src/main/webapp/assets/images/serie-detail.png)

In order to make easier the creation Series and Episodes, it's possible to use the modeule "scratch from TvDb" which allow to find series by name and **import** the serie and its episodes in the system.
The system, everyday try to update the series in case new episodes or new updates has been done in TvDb. Due to the application is installed in a free server, it's possible that the cron is not trigger, therefore, it was develop a button to can do it from list of Series.
![scratch-search](https://raw.githubusercontent.com/fmunozse/JTrackSeries/master/src/main/webapp/assets/images/scratch-search.png)

From Calendar, it's possible to see your episodes and if you click in the episode you can see the description and mark easly like viewed.
![calendar-month](https://raw.githubusercontent.com/fmunozse/JTrackSeries/master/src/main/webapp/assets/images/calendar-month.png)


## Configuration 
Check the files application-dev.yml and -prod where is found the configuration. By default it is used
* ddbb: POSTGRESQL . You can find at [http://www.postgresql.org](http://www.postgresql.org)
  * Url: jdbc:postgresql://localhost:5432/JTrackSeries
  * Create user JTrackSeries and Create ddbb JTrackSeries  (Could use pgadmin3 at [http://www.pgadmin.org](http://www.pgadmin.org)
  * 
* email: smtp.sendgrid.net Check to use your information

On file application.yml, it's set up the token of [http://thetvdb.com/](http://thetvdb.com/)
* It's need generate a token and pass like param. Check how is possible to do [https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html)
  * It can set ` --jtrackseries.tvdb.token=MYTOKEN ` in the command line
  * It can set like system variables ` JTRACKSERIES_TVDB_TOKEN=MYTOKEN `
* To generate the token, at [http://thetvdb.com/?tab=apiregister](http://thetvdb.com/?tab=apiregister)

## Deploy on [heroku](https://www.heroku.com/)
Check the documentation on jhispter [http://jhipster.github.io/heroku/](http://jhipster.github.io/heroku/). 
* You must install the [Heroku toolbelt](https://toolbelt.heroku.com/), and have a Heroku account created.
* You must also create a Heroku account and log in with the toolbelt by running the following command:

    **$ heroku login**
    Enter your Heroku credentials.
    Email: YOUR_EMAIL
    Password (typing will be hidden): YOUR_PASSWORD
    Authentication successful.
 
* Set up email
  * SENDGRID_PASSWORD
  * SENDGRID_USERNAME
  
Here a shortcut one that it has been do it.
* Prepare a war to can install `mvn install -Pprod -DskipTests` . Notices that it's recommended set the version in the pom.xml    
* Deploy `heroku deploy:jar --jar target/jtrackseries-0.0.6.war` 

# DEV set up
## Initial Set up
Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools (like
[Bower][] and [BrowserSync][]). You will only need to run this command when dependencies change in package.json.

    npm install

We use [Grunt][] as our build system. Install the grunt command-line tool globally with:

    npm install -g grunt-cli

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    mvn
    grunt

Bower is used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in `bower.json`. You can also run `bower update` and `bower install` to manage dependencies.
Add the `-h` flag on any command to see how you can use it. For example, `bower update -h`.

## Building for production

To optimize the JTrackSeries client for production, run:

    mvn -Pprod clean package

This will concatenate and minify CSS and JavaScript files. It will also modify `index.html` so it references
these new files.

To ensure everything worked, run:

    java -jar target/*.war --spring.profiles.active=prod

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

# Testing

Unit tests are run by [Karma][] and written with [Jasmine][]. They're located in `src/test/javascript` and can be run with:

    grunt test



# Continuous Integration

To setup this project in Jenkins, use the following configuration:

* Project name: `JTrackSeries`
* Source Code Management
    * Git Repository: `git@github.com:xxxx/JTrackSeries.git`
    * Branches to build: `*/master`
    * Additional Behaviours: `Wipe out repository & force clone`
* Build Triggers
    * Poll SCM / Schedule: `H/5 * * * *`
* Build
    * Invoke Maven / Tasks: `-Pprod clean package`
* Post-build Actions
    * Publish JUnit test result report / Test Report XMLs: `build/test-results/*.xml`

[JHipster]: https://jhipster.github.io/
[Node.js]: https://nodejs.org/
[Bower]: http://bower.io/
[Grunt]: http://gruntjs.com/
[BrowserSync]: http://www.browsersync.io/
[Karma]: http://karma-runner.github.io/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html
[Protractor]: https://angular.github.io/protractor/
