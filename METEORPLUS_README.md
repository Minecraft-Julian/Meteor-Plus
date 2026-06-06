# MeteorPlus Scratch-Scripting System

## 🎯 Ziel

MeteorPlus ist ein visuelles Scratch-ähnliches Scripting-System für Minecraft Java Edition, entwickelt als Meteor Client Addon.

Es soll ermöglichen:
- visuelles Programmieren mit Blöcken (ähnlich wie Scratch)
- Scripts zu speichern, exportieren und importieren
- Minecraft-Aktionen im Survival-Modus ohne Cheats zu automatisieren
- mehrere Scripts zu erstellen und zu verwalten
- alle Scripts im Meteor-Menü anzuzeigen und auszuwählen

---

## 🔵 1. Idee

MeteorPlus soll einen Einstieg in die Minecraft-Automatisierung bieten, ohne dass Nutzer Java-Code schreiben müssen. Stattdessen werden Aktionen als farbige Blöcke organisiert, die sich per Drag & Drop zusammenfügen lassen.

---

## 🔷 2. Block-Kategorien

MeteorPlus orientiert sich an Scratch und nutzt neun Kategorien für Minecraft-spezifische Automatisierung:

| # | Kategorie | Farbe | Deutsche Beschreibung |
|---|-----------|-------|----------------------|
| 1 | Bewegung | Dunkelblau `#4A6FA5` | Spieler bewegen, drehen, schauen |
| 2 | Aussehen | Lila `#FF6B6B` | Chat-Nachrichten, Items geben |
| 3 | Klang | Rosa `#FF66FF` | Sound-Effekte abspielen |
| 4 | Ereignisse | Gelb `#FFDD33` | Script-Trigger (When started, etc.) |
| 5 | Steuerung | Orange `#FF6633` | Schleifen, Bedingungen, Verzögerungen |
| 6 | Fühlen | Hellblau `#0099CC` | Sensoren, Block-Kontakt, Tastatur-Input |
| 7 | Operatoren | Hellgrün `#99FF99` | Mathematische Operationen (+, -, *, /, %) |
| 8 | Variablen | Dunkelorange `#CC6600` | Variablen speichern und verändern |
| 9 | Meine Blöcke | Dunkelrot | Custom Blocks (geplant für Phase 2) |

---

## 🏗️ 3. Architektur & Klassen-Struktur

### Kern-System

- `Variable.java`
  - Speichert Daten mit Typen wie `ITEM`, `NUMBER`, `TEXT`, `PLAYER`, `POSITION`, `ROTATION`, `COMMAND`, `BOOLEAN`

- `Block.java` (Abstrakt)
  - Basis-Interface für alle Blöcke
  - Methoden: `execute()`, `getInputSlots()`, `setParameter()`

- `ExecutionContext.java`
  - Laufzeit-Umgebung mit Spieler-Referenz
  - Variable-Storage
  - Minecraft-Interaktions-Methoden
  - Thread-Control (`pause`, `resume`, `stop`)

- `ScriptExecutor.java`
  - Führt Scripts in getrenntem Thread aus
  - Listener für Start/Complete/Error/Stop Events

- `BlockRegistry.java`
  - Verwaltet verfügbare Block-Typen
  - Factory für Block-Erstellung

- `ScriptData.java`
  - Datenstruktur für Script-Serialisierung
  - Konvertierung in das `.meteorplus` Format

- `ScriptManager.java`
  - Speichern/Laden von `.meteorplus` Dateien
  - Import/Export
  - Script-Verwaltung (Liste, Löschen, etc.)

### ScriptBuilder Modul

- `ScriptBuilder.java`
  - Hauptmodul für Meteor Client
  - Einstellungen für Script-Auswahl
  - Auto-Run, Loop-Optionen
  - Integration mit Meteor UI

### Block-Implementierungen

Geplante Block-Klassen strukturieren sich nach Kategorien:

- `blocks/movement/`
  - `MoveForwardBlock.java`
  - `TurnBlock.java`
  - `LookAtBlock.java`
- `blocks/actions/`
  - `GiveItemBlock.java`
  - `SayBlock.java`
  - `WaitBlock.java`
- `blocks/control/`
  - `LoopBlock.java`
  - `IfBlock.java`
  - `InfiniteLoopBlock.java`
- `blocks/sensing/`
  - `TouchingBlock.java`
  - `KeyPressedBlock.java`
- `blocks/variables/`
  - `SetVariableBlock.java`
  - `ChangeVariableBlock.java`
- `blocks/operators/`
  - `MathBlock.java`
- `blocks/events/`
  - `OnStartBlock.java`
- `blocks/sound/`
  - `PlaySoundBlock.java`

---

## 🎮 4. Survival-Kompatibilität

MeteorPlus ist so ausgelegt, dass es wie ein Meteor Client Addon funktioniert und möglichst viele Aktionen über Spieler-Input simuliert, nicht über Cheats.

Wichtige Methoden im `ExecutionContext` sollen sein:

- `executeMovement(direction, distance)`
- `rotatePlayer(yaw, pitch)`
- `lookAtCoordinates(x, y, z)`
- `attackBlock()`
- `useBlock()`
- `isPlayerTouchingBlock(blockName)`
- `isKeyPressed(key)`
- `executeCommand(command)`
- `sendChat(message)`
- `waitSeconds()` / `waitTicks()`

Damit bleiben die Kernfunktionen ohne OP/Cheat nutzbar.

---

## 💾 5. Script-Speicherung: `.meteorplus`

### Format

- Datei-Endung: `.meteorplus`
- Inhalt: JSON-ähnliche Script-Daten in einer JavaScript-Wrapper-Datei
- Speicherort: `meteorplus/scripts/`

### Beispiel

```javascript
/*
 * MeteorPlus Script: AutoKiller
 */
const scriptData = {
  "name": "AutoKiller",
  "description": "Auto aim and kill",
  "createdAt": 1717678800000,
  "lastModified": 1717678900000,
  "blocks": [
    {
      "id": "on_start",
      "params": {},
      "nextBlockIndex": 1
    },
    {
      "id": "move_forward",
      "params": {"steps": 10},
      "nextBlockIndex": 2
    },
    {
      "id": "wait",
      "params": {"seconds": 1},
      "nextBlockIndex": -1
    }
  ]
};
```

### Features

- Speichert und lädt Scripts
- Import/Export möglich
- Unterstützt mehrere Scripts
- Organisiert in einem Scripts-Ordner

---

## 📋 6. Meteor Client Integration

Das `ScriptBuilder`-Modul soll in den Meteor Client eingebunden werden und dort unter einem eigenen Menüpunkt erscheinen.

### Settings

- `selected-script`: Dropdown mit verfügbaren Scripts
- `auto-run`: Startet Script beim Aktivieren des Moduls
- `loop`: Wiederholt das Script endlos

### Funktionen

- `startScript()`
- `stopScript()`
- `createScript()`
- `deleteScript()`
- `getScriptList()`

---

## 🔗 7. Systemfluss

### Nutzerfluss

1. Menü → MeteorPlus → Script Builder
2. Neues Script erstellen
3. Script speichern als `.meteorplus`
4. Script auswählen und ausführen
5. Scripts verwalten, löschen, importieren

---

## 📚 8. Block-Details

### Bewegung

- `Gehe [10] Schritte nach vorne`
- `Drehe dich um [45] Grad nach rechts`
- `Schau auf Koordinaten [100] [64] [200]`

### Aussehen

- `Schreibe [Hallo!] in den Chat`
- `Gib mir [diamond]`

### Klang

- `Spiele Klang [SOUND]`

### Ereignisse

- `Wenn Script gestartet`

### Steuerung

- `Wiederhole [5] mal`
- `Falls [bedingung] dann`
- `Wiederhole dauerhaft`

### Fühlen

- `Wird [stone] berührt?`
- `Wird Taste [W] gedrückt?`

### Operatoren

- `[5] [+] [3]`

### Variablen

- `Setze [myVar] auf [100]`
- `Ändere [counter] um [1]`

---

## 🚀 9. Beispiel-Scripts

### AutoKiller

- Wenn Script gestartet
  - Chat-Nachricht
  - Wiederhole dauerhaft
    - Prüfe, ob Spieler berührt wird
    - Wenn ja: schau auf Spieler, klicke, warte 0.1 Sek.
    - Warte 0.05 Sek.

### FastBridge

- Wenn Script gestartet
  - Chat-Nachricht
  - Wiederhole dauerhaft
    - Prüfe, ob `SPACE` gedrückt ist
    - Wenn ja: platziere Block, gehe vorwärts, warte 0.05 Sek.

### AFK-Script

- Wenn Script gestartet
  - Setze `counter` auf 0
  - Wiederhole dauerhaft
    - Gehe 1 Schritt vorwärts
    - Erhöhe `counter`
    - Schreibe Status-Chat
    - Warte 60 Sek.

---

## 📁 10. Projektstruktur (geplant)

```
Meteor-Plus/
├─ src/main/java/com/meteorplus/
│  ├─ scripting/
│  │  ├─ Variable.java
│  │  ├─ Block.java
│  │  ├─ ExecutionContext.java
│  │  ├─ ScriptExecutor.java
│  │  ├─ BlockRegistry.java
│  │  ├─ storage/
│  │  │  ├─ ScriptData.java
│  │  │  └─ ScriptManager.java
│  │  └─ blocks/
│  │     ├─ movement/
│  │     ├─ actions/
│  │     ├─ control/
│  │     ├─ sensing/
│  │     ├─ variables/
│  │     ├─ operators/
│  │     ├─ events/
│  │     └─ sound/
│  └─ modules/
│     └─ ScriptBuilder.java
├─ meteorplus/scripts/
├─ METEORPLUS_README.md
```

---

## ✅ 11. Phase 1 Status

Bereits geplant oder implementiert:
- Core-System mit Variable-, Block- und ExecutionContext-Komponenten
- ScriptExecutor und BlockRegistry
- Script-Serialisierung und -Management
- Basale Blocktypen wie Bewegung, Chat, Steuerung, Sensorik und Variablen
- `.meteorplus` Datenformat
- Threading, Error-Handling und Meteor-UI-Integration

---

## ⏳ 12. Geplante Weiterentwicklung

### Phase 2

- Visueller Block-Editor HUD
- Drag & Drop UI
- Parameter-Editor
- Script-Visualisierung

### Phase 3

- Custom Block Creator
- Debugging Tools
- Block-Bibliothek
- Community Sharing
- Erweiterte Action-Blöcke

---

## 🎯 13. Unterschied zu Standard-Meteor-Addons

| Feature | Meteor Client | MeteorPlus |
|---|---|---|
| Programmierung | Java-Code | Visuelle Blöcke |
| Komplexität | Hoch | Einsteigerfreundlich |
| Erstellung | Code schreiben | Drag & Drop |
| Speichern | Code-Repo | `.meteorplus` Dateien |
| Sharing | GitHub Push | Export/Import |
| Multiple Scripts | Einzelne Module | Mehrere Scripts |

---

## 🔐 14. Sicherheit & Performance

- Separater Ausführungs-Thread
- Stop/Start-Kontrolle
- Input-Validierung
- Kein unkontrollierter Dateizugriff außerhalb des Scripts-Ordners
- Optimierte Block-Ausführung
- Minimaler Memory-Overhead
- Lazy Loading der Block-Registry

---

## 🎓 15. Nutzeranleitung

1. Erstelle ein neues Script im MeteorPlus-Modul.
2. Ziehe Blöcke in den visuellen Editor.
3. Setze Parameter für jeden Block.
4. Speichere das Script als `.meteorplus` Datei.
5. Wähle das Script aus und aktiviere das Modul.

---

## 💡 16. Kernerkenntnisse

- Scratch-ähnliches System für Minecraft
- Survival-first Design
- Einfache Script-Verwaltung
- Threading-sicher
- Erweiterbares Format
- 9 klare Block-Kategorien

---

## 🎉 Fazit

MeteorPlus liefert ein modernes Visual-Scripting-Konzept für Minecraft Java Edition im Meteor Client. Der nächste Schritt ist der Bau des visuellen Editor-HUDs mit Drag & Drop.
