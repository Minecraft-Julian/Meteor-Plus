# 🎮 MeteorPlus - Scratch-like Scripting System für Minecraft

Ein **visuelles Scripting-System** für Meteor Client, das wie Scratch funktioniert. Ermöglicht es dir, **automatisierte Scripts** mit farbigen Blöcken zu erstellen, ohne Code zu schreiben.

## ✨ Features

### 🔵 9 Scratch-Kategorien
- **Bewegung** (Dunkelblau) - Spielerbewegung steuern
- **Aussehen** (Lila) - Chat-Nachrichten, Items geben
- **Klang** (Rosa) - Sound-Effekte abspielen
- **Ereignisse** (Gelb) - Script-Trigger
- **Steuerung** (Orange) - Schleifen, Bedingungen
- **Fühlen** (Hellblau) - Sensoren, Input-Abfrage
- **Operatoren** (Hellgrün) - Mathematik, Logik
- **Variablen** (Dunkelorange) - Speicher und Abruf
- **Meine Blöcke** (Dunkelrot) - Custom Blocks (geplant)

### 💾 Script-Management
- ✅ Scripts als `.meteorplus`-Dateien speichern
- ✅ Import/Export von Scripts
- ✅ Liste aller Scripts im Menü
- ✅ Neue Scripts erstellen und löschen
- ✅ Auto-Run und Loop-Optionen

### 🎯 Survival-Kompatibilität
- ✅ Funktioniert **ohne Cheats** in Survival
- ✅ Input-Simulation (wie Meteor Client)
- ✅ Keine `/teleport` Commands nötig
- ✅ Alles was ein echter Spieler machen kann

---

## 📦 Installation

1. **Klone das Repository:**
   ```bash
   git clone https://github.com/Minecraft-Julian/Meteor-Plus.git
   cd Meteor-Plus
   ```

2. **Wechsle zum Feature-Branch:**
   ```bash
   git checkout feature/scratch-scripting-system
   ```

3. **Baue das Projekt:**
   ```bash
   ./gradlew build
   ```

4. **JAR-Datei installieren:**
   - Platziere die JAR-Datei aus `build/libs/` in deinem `mods`-Ordner
   - Starte Minecraft mit Meteor Client

---

## 🚀 Quick Start

### 1. Script erstellen
```
Meteor Client öffnen → MeteorPlus → Script Builder
→ "Neues Script" → Name eingeben (z.B. "AutoKiller")
```

### 2. Blöcke hinzufügen
```
Blöcke in den Editor ziehen:
- "Gehe 10 Schritte nach vorne"
- "Warte 1 Sekunde"
- "Schreibe 'Fertig!' in den Chat"
```

### 3. Script speichern & ausführen
```
Script speichern → Menü → ScriptBuilder aktivieren
→ Script aus Liste wählen → Start
```

---

## 📋 Verfügbare Blöcke

### Bewegung (Dunkelblau)
- `Gehe [X] Schritte nach vorne`
- `Drehe dich um [°] Grad nach rechts`
- `Schaue auf Koordinaten [X] [Y] [Z]`

### Aussehen (Lila)
- `Schreibe [TEXT] in den Chat`
- `Gib mir [ITEM]`

### Klang (Rosa)
- `Spiele Klang [SOUND]`

### Ereignisse (Gelb)
- `Wenn Script gestartet`

### Steuerung (Orange)
- `Wiederhole [N] mal`
- `Falls [BEDINGUNG] dann`
- `Wiederhole dauerhaft`

### Fühlen (Hellblau)
- `Wird [BLOCK] berührt?`
- `Wird Taste [KEY] gedrückt?`

### Operatoren (Hellgrün)
- `[A] [+|-|*|/|%] [B]`

### Variablen (Dunkelorange)
- `Setze [NAME] auf [WERT]`
- `Ändere [NAME] um [BETRAG]`

---

## 📁 Dateistruktur

```
src/main/java/com/meteorplus/
├── scripting/
│   ├── Variable.java                    # Variablen-Klasse
│   ├── Block.java                       # Block-Interface
│   ├── ExecutionContext.java            # Laufzeit-Kontext
│   ├── ScriptExecutor.java              # Script-Ausführer
│   ├── BlockRegistry.java               # Block-Verwaltung
│   ├── storage/
│   │   ├── ScriptData.java              # Datenstruktur
│   │   └── ScriptManager.java           # Speichern/Laden
│   └── blocks/
│       ├── movement/                    # Bewegungs-Blöcke
│       ├── sensing/                     # Sensor-Blöcke
│       ├── control/                     # Kontroll-Blöcke
│       ├── variables/                   # Variablen-Blöcke
│       ├── operators/                   # Operator-Blöcke
│       ├── actions/                     # Action-Blöcke
│       ├── events/                      # Event-Blöcke
│       └── sound/                       # Sound-Blöcke
└── modules/
    └── ScriptBuilder.java               # Meteor-Modul
```

---

## 🔧 .meteorplus File Format

Scripts werden als `.meteorplus`-Dateien gespeichert (intern JavaScript/JSON):

```javascript
/*
 * MeteorPlus Script: AutoKiller
 */
const scriptData = {
  "name": "AutoKiller",
  "description": "Auto aim and click",
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
    }
  ]
};
```

---

## 🎮 Beispiel-Scripts

### AutoWalker
```
Wenn Script gestartet
  → Wiederhole dauerhaft
    ├��� Gehe 5 Schritte nach vorne
    ├─ Warte 0.5 Sekunden
    ├─ Drehe dich um 45 Grad nach rechts
    └─ Warte 0.5 Sekunden
```

### AutoFarm
```
Wenn Script gestartet
  → Wiederhole 10 mal
    ├─ Falls wird Stein berührt?, dann:
    │  └─ Zerstöre Block (Attack)
    ├─ Gehe 1 Schritt nach vorne
    └─ Warte 0.2 Sekunden
```

### AutoChat
```
Wenn Script gestartet
  → Setze counter auf 0
  → Wiederhole dauerhaft
    ├─ Schreibe [counter] in den Chat
    ├─ Ändere counter um 1
    └─ Warte 2 Sekunden
```

---

## 🛠️ Entwicklung

### Eigene Blöcke erstellen

1. **Neue Block-Klasse** (erbt von `Block`):
```java
package com.meteorplus.scripting.blocks.custom;

import com.meteorplus.scripting.Block;
import com.meteorplus.scripting.ExecutionContext;
import com.meteorplus.scripting.Variable;

public class MyCustomBlock extends Block {
    public MyCustomBlock() {
        super(
            "my_custom",
            "Mein eigener Block [PARAM]",
            "Custom",
            "#FF0000"  // Farbe
        );
    }

    @Override
    public void execute(ExecutionContext ctx) throws Exception {
        String param = getParameterAsString("param");
        
        // Deine Logik hier
        
        executeNext(ctx);
    }

    @Override
    public String[] getInputSlots() {
        return new String[]{"param"};
    }

    @Override
    public Variable.VariableType getInputSlotType(String slotName) {
        return Variable.VariableType.TEXT;
    }
}
```

2. **Im BlockRegistry registrieren** (kommt später)

---

## 📝 Roadmap

### Phase 1 (Aktuell) ✅
- ✅ Block-System (9 Kategorien)
- ✅ Script-Speicherung (.meteorplus)
- ✅ ScriptBuilder-Modul
- ✅ Survival-Kompatibilität

### Phase 2 (Nächst)
- ⏳ Visueller Block-Editor HUD
- ⏳ Drag & Drop UI
- ⏳ Script-Parameter-Bearbeitung

### Phase 3 (Später)
- ⏳ Custom Block Creator
- ⏳ Debugging Tools
- ⏳ Block-Vorschau
- ⏳ Community Block-Sharing

---

## 🤝 Contributing

Beiträge sind willkommen! Bitte erstelle einen Pull Request mit:
1. Beschreibung der Änderungen
2. Tests (falls relevant)
3. Dokumentation für neue Features

---

## 📄 Lizenz

Dieses Projekt nutzt die selbe Lizenz wie das Meteor Addon Template.

---

## 🔗 Links

- [Meteor Client](https://meteorclient.com)
- [Meteor Addon Template](https://github.com/MeteorDevelopment/meteor-addon-template)
- [Scratch](https://scratch.mit.edu)

---

**Viel Spaß mit MeteorPlus!** 🚀

Du kannst jetzt automatisierte Scripts wie ein Pro programmieren - ganz ohne Code zu schreiben!
