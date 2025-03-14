[icon]: https://raw.githubusercontent.com/inc0g-repoz/kvadratik/main/client/src/main/resources/assets/icon.png
[java.awt]: https://docs.oracle.com/javase/7/docs/api/java/awt/package-summary.html
[sprites-resource]: https://www.spriters-resource.com/pc_computer/omori/?source=genre
<!-- The stuff above is invisible -->

![icon]
**kvadratik**

# What's this?
kvadratik is a custom 2D game engine that only uses [java.awt] to render in-game objects.
Players are able to connect to your server that provides a direct link for downloading all necessary assets for joining it.

A custom dynamically typed script engine is included for writing JIT compiled scripts for servers and clients, wrapping and managing the exact same entities that a running game (or server) does. Currently, discontinued, since I'm working on a better replacement for the used implementation.

# Building
Simply run Maven with goals `clean package` for the root project.

# What about the assets?
I don't own any of the sprites used in this project. They are all placeholders I took from [here][sprites-resource].
