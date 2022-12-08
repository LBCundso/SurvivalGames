# SurvivalGames-Plugin

Hi! Das hier ist mein SurvivalGames-Plugin, welches ich im Rahmen meiner (recht inaktiven) Serie auf TikTok, in welcher ich versucht habe, eine eigenes Minecraftplugin zu erstellen. (https://tiktok.com/@LBCundso)


# Infos
- Spigot-Plugin für die 1.19.2
- Benötigt wird:
      1. ein Rechteverwaltungsplugin wie LuckPerms.
      2. ein Plugin zum Laden von Welten.

- Es können im Spiel keine Blöcke platziert oder abgebaut werden.
- Ab 2 Spielenden fängt der Countdown von 60 Sekunden an.
- In einer Kiste gibt es 4 schwache, 2 mittelstarke und 1 starkes Item.
- Die Worldborder schrumpft auf 20 Blöcke mit einer Geschwindigkeit von 4 Sekunden pro Block.

## Commands ohne Permissions

/sgstats - Schaue dir deine Stats an.

## Commands mit Permissions
/sg enable/disable/addspawn/remove/setbordercenter/setlobby/spawnvillager/info [MAP_NAME/BORDERWIDTH] - Permission: sg.manage

	addspawn - Füge einen Spawn hinzu.
	remove - Entfernt die Map aus dem Argument aus der Config.
	setbordercenter - Setzt die Mitte der Map und den Radius der Border.
	setlobby - Setze die Wartelobby der Map (Muss in der Map sein.)
	spawnvillager - Setze den Villager, der dich in ein offenes Spiel hinzufügt.
	enable/disable - Aktiviere/Deaktiviere die Karte. (Achtung: Reload/Restart erforderlich!)
	info - Erhalte Informationen zur Welt.
  
## Hinzugefügte Items:

Schwach:
- Lederrüstung
- Holzschwert (entweder ohne Verzauberung oder mit Sharpness 1 bzw. Knockback 1)
- Brot

Mittelstark:
- Kettenrüstung
- Steinschwert (entweder ohne Verzauberung oder mit Sharpness 1/2 und/oder Knockback 1/2)
- Knockback 1 Stick
- Steak

Stark:
- Eisenrüstung
- Eisenschwert (entweder ohne Verzauberung oder mit Sharpness 1/2/3 und/oder Knockback 1/2)
- Diamantschwert
- Goldapfel
