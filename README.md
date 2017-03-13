# Hyleria Network

The code powering all of Hyleria is contained within this single repository. From backend utlility scripts to user-facing games; it's here.

#### What the directories here do:

 - `bungee-plugin` Hook into our (currently single) BungeeCord proxy
 - `spigot-plugin` The mostly user oriented side of Hyleria. Contains our games, and in-game stuff.
 - `network-manager` A tool to control the Minecraft network automatically, and monitor it.
 - `script` A set of bash scripts that save some time on backend tasks
 - `config` Backend configuration files - either these are just examples with filler values, or local dev ones.
 - `lib` Granted we use some outdated tech we need to keep track of our stuff locally instead of on remote Maven repositories.


##### Important  Info
- [x]  [hyleria.com](https://hyleria.com) The public site for everyone to use, and enjoy
- [x] `mc.hyleria.com` A gateway into the Minecraft network (hits lobbies)
- [x] [Trello](https://trello.com/hyleria)

##### Code
The code within this repository is intended to be clean & easily to follow. Of course, you may not agree with that... I follow a fairly certain (and abnormal; for Java) style. If anyone contributes to the code here you _must_ follow the style of surrounding code or imma yell at you then make sure your mistake is corrected.
