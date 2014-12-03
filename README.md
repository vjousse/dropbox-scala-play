# Installation

First, be sure to [install Play Framework](https://www.playframework.com/documentation/2.3.x/Installing). I'm using version 2.3.6.

Then, rename `conf/application.conf.dist` to `conf/application.conf` and change the `dropbox.app` values. 

    dropbox.app.key = "YOUR_APP_KEY"
    dropbox.app.secret = "YOUR_APP_SECRET";
    dropbox.app.name = "DropboxPlayExample"
    dropbox.app.version = "0.0.1"

You shouldn't modify the `redirectUri` param. The `key` and the `secret` values can be retrieved on the [dropbox website](https://www.dropbox.com/developers/apps). `name` and `version` can be changed to whatever you like. It will be sent as an header to the Dropbox API.

Launch the Play application and then go to this url [http://localhost:9000/dropbox](http://localhost:9000/dropbox) to link your dropbox account. Once you're redirected to the "Auth finished" page, you can display the content of your `/` directory by going to [http://localhost:9000/dropbox/list](http://localhost:9000/dropbox/list).

# Warning

This code is provided as a basic example to get you started with Dropbox and Play, it's not intended to be used in production as it is.

# External libraries

[dropbox4s](https://github.com/Shinsuke-Abe/dropbox4s)
