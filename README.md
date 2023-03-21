# BKBouncingDVD
![Top Language](https://img.shields.io/github/languages/top/bbayu123/bkbouncingdvd-poc)
<a href="https://www.gnu.org/licenses/gpl-3.0.html">![GitHub](https://img.shields.io/github/license/bbayu123/bkbouncingdvd-poc)</a>
<a href="https://www.spigotmc.org/resources/bkcommonlib.39590/">![BKCommonLib Version](https://img.shields.io/spiget/version/39590?label=BKCommonLib)</a>
<a href="https://github.com/bbayu123/bkminesweeper-poc">![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/bbayu123/bkbouncingdvd-poc?label=BKBouncingDVD)</a>
<a href="https://discord.gg/wvU2rFgSnw">![Discord](https://img.shields.io/discord/415909893233442826?label=Discord&logo=Discord)</a>

BKBouncingDVD is a SpigotMC plugin that shows a bouncing DVD logo on maps.

This plugin is an example of how to use the MapDisplay system in [BKCommonLib][1].

Three different implementations of the same outcome can be found in this project, click each hyperlink to see
* [The MapWidget implementation](src/main/java/io/github/bbayu123/bkbouncingdvd/mapwidget_version/BouncingDVD.java)
* [The MapCanvas implementation](src/main/java/io/github/bbayu123/bkbouncingdvd/mapcanvas_version/BouncingDVD.java)
* [The Graphics2D implementation](src/main/java/io/github/bbayu123/bkbouncingdvd/graphics2d_version/BouncingDVD.java)

![Showcase Image](cover.png)

## Installation

Compile the plugin using [maven](https://maven.apache.org/).

Navigate to the root of the project, and type

```bash
mvn
```

This plugin requires [BKCommonLib][2] to function. You need to add BKCommonLib to your Spigot server 
before adding this plugin. 
This plugin should support all versions from version 1.13.2 onwards.


## Usage

To obtain a bouncing DVD logo, type either

```
/bouncingdvd-mw get
/bouncingdvd-mc get
/bouncingdvd-jg get
```

to get the MapWidget, MapCanvas, or Graphics2D version respectively.

## Support

If you want more information on MapDisplays, head to the wiki page: <https://wiki.traincarts.net/p/Map_Display>

If you would like to see more examples, or want to ask for help when creating your own MapDisplay project,
reach out to us in the TeamBergerHealer discord server: <https://discord.gg/wvU2rFgSnw>

## License

This project uses the [GNU General Public License Version 3](https://www.gnu.org/licenses/gpl-3.0.html). 
For more information, see [LICENSE](LICENSE).

[1]: https://github.com/bergerkiller/BKCommonLib "Click to go to the GitHub page for BKCommonLib"
[2]: https://www.spigotmc.org/resources/bkcommonlib.39590/ "Click to go to the SpigotMC page for BKCommonLib"