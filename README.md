# Hyleria Network

The code powering all of Hyleria (when it was a thing) is contained within this single repository. From backend utlility scripts to user-facing games; it's here.

#### What the directories here do:

 - `common` A collection of utilities & tools that may be useful to any Hyleria oriented application
 - `config` Backend configuration files - either these are just examples with filler values, or local dev ones.
 - `plugin` Plugins that run Hyleria - whether it be a proxy sided, game engine, or our core systems
 - `script` A set of bash scripts that save some time on backend tasks
 - `web-api` A node.js app for serving a RESTful web-based API. Both player & network data is hoped to be supported at some point.

#### Project Setup

In the case that you're using IntelliJ:
 - File
 - New Project from exiting sources
 - Select the `pom.xml` file within the `plugin` directory
 - Be sure to import projects recursively
 - have at it

If you're not then I wish you luck


#### Documentation
Java docs are right [here](https://outdatedversion.github.io/hyleria).



#### License: Mozilla Public License 2.0

See `LICENSE` for further details
