Cyber Game
==========


Development Setup
-----------------

**Libraries**

This project has several dependencies, but they are all included in the source tree.

Here is a listing of the project dependencies:

* [Restlet](http://www.restlet.org/) -  Is an easy to use web framework. It powers the web service. It is similar to Java Servlets, but much simpler. Basically, you define 'routes' which are URI paths to a resource (e.g., '/questions'). A resources is just any content accessible by a URL. Once you define a route to a resource, you define a Resource class, which is a handler. When a visitor comes to a certain route, Restlet looks up the handler and executes that code. So, it is a simple system for defining URLs and having chunks of code handle those URLs.

* [JavaMaildir](http://javamaildir.sourceforge.net/) - [MailDir](http://en.wikipedia.org/wiki/Maildir) is a very common format for storing email messages on disk. JavaMailDir is an implementation of this format for JavaMail. We use JavaMail to download a user's email, and JavaMailDir to store that email to disk. We also use JavaMailDir to read that email again later for processing.

* [SqliteJDBC](http://www.zentus.com/sqlitejdbc/) - SQLiteJDBC is a Java JDBC driver for SQLite. SQLite is a self-contained, serverless SQL database that is stored in a file. Using it for development is simpler than using a large database like MySQL. Though, it should be easy to switch from SQLite to a more robust database later on in the process.

* [Joda Time](http://joda-time.sourceforge.net/) - Joda Time is a library for handling dates and times. It is used in the question generation process.


**Setup**

The following instructions are for those using Eclipse. They explain how to setup your Eclipse environment to recognize all the dependencies. 

1. **Install the Eclipse SDK and Eclipse Platform SDK**

    This allows us to set the Target Platform in the third step

    * Click:  Help > Install New Software > "Add"
    * Enter  Name: `The Eclipse Project Updates` and URL: `http://download.eclipse.org/eclipse/updates/3.6`
    * Select the new entry, and check the *Eclipse SDK* and *Eclipse Platform SDK* boxes, then install them.


2. **Create the Eclipse Project**

    * In Eclipse: File > New > Project...
    * Select "Plug-in Project"
        * Use default settings.
        * Yes, using a 'plug-in' project is strange, but necessary
    * Once it is created, copy all the files from the git repo (including the .git/ dir) into the project folder.
        * Replace any pre-existing files.
    * Refresh the project in eclipse (right click on project name > Refresh)

3. **Change the Target Platform**

    This makes Eclipse aware of all the *Restlet* dependencies

    * Click Window > Preferences
    * Navigate to Plug-in Development > Target Platform
    * Click "Add"
    * Click "Next"
        * Name the target *Restlet*
        * Click "Add" and choose "Directory".
        * Browse to `$source_repo>\CyberGame\target\restlet`
        * Finish
    * It should find 73 plugins and look like this: [http://i.imgur.com/iO0NF.png](http://i.imgur.com/iO0NF.png)
    * Finish
    * Select the new Target (Restlet) and click "Apply"

4. **Add the Remaining Libraries to the Build Path**

    * With the CyberGame project open, select: Project > Properties in the menu
    * On the left side choose 'Java Build Path'
    * Click 'Add Jars' on the right side
    * Using the tree browser expand the `CyberGame/lib` directory
    * Highlight all the *.jar files and click OK
    * Click "Add Library"
    * Choose JUnit
        * -> Next -> Choose JUnit library version 4 -> Finish
    * Your Build Path should look like this: [http://i.imgur.com/Zyp8l.png](http://i.imgur.com/Zyp8l.png)


Cyber Game Modules
------------------

This project consists of three separate modules all contained in the `src/yao` directory.

**gamelib - Game Library**

This module includes the source code for:

* fetching datasets
    * currently implemented: email fetching
        * `src/yao/gamelib/FetchDataset.java`
* abstract question generation
    * `src/yao/gamelib/QuestionGenerator.java`
* implementations of various types of questions
    * currently implemented: email
        * see `src/yao/gamelib/EmailQuestion.java` and sub-classes

**gamecli - Game Command Line Interface**

This is a standalone program that interface with the `gamelib` via the command line. Currently it implements the following features:

* input user credentials to download *and store* email data
* from the downloaded email data, generate questions
* store generated questions in a database

**gameweb - Game Web Module**

This module contains all the code relating to the web service and interface. The javadocs should be self-explanatory.

One thing to note is the templating system.

Rather than embed HTML within Java code (that would get messy quick), we are using the [Freemarker template engine](http://freemarker.sourceforge.net/). The templates are stored in `gameweb/util/templates/` as `.ftl` files. They allow us to embed little snippets of code into an HTML page, and then process those templates in our Java code and output a fuly featured page. Freemarker is included in the Restlet library.

Database browser: (SQLite Database Browser](http://sqlitebrowser.sourceforge.net/)
