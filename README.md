# Ink IDE

Ink IDE is a hobby desktop IDE project built to explore how modern IDEs are structured and to learn Kotlin through a real application.

The project takes inspiration from two different approaches:

- **Atom** — a small, hackable editor core with functionality added through packages/plugins.
- **JetBrains IDEs** — a strongly structured platform built around services, extension points, language tooling, and rich project/editor abstractions.

The goal is to combine those ideas into a small, understandable IDE platform:

> Start with Atom’s plugin-first philosophy and evolve toward JetBrains-style intelligence as the editor grows.

## Project Rule

> **The core should know as little as possible. Features belong in plugins whenever practical.**

This rule should guide architectural decisions throughout the project.

---

## Technology Stack

### Adopted now

| Technology | Purpose |
|---|---|
| **Kotlin 2.4.20** | Main programming language |
| **JVM** | Runtime platform |
| **JDK 21 LTS** | Java toolchain |
| **Gradle 9.7.1** | Build system |
| **Gradle Kotlin DSL** | Build configuration using `build.gradle.kts` |
| **Compose Multiplatform 1.12.0** | Desktop UI framework |
| **Compose Desktop** | Windows, panels, layout, mouse interaction, and desktop UI |
| **Kotlin Coroutines 1.11.0** | Background and asynchronous work |
| **Flow / StateFlow** | Observable application state as the project grows |

### Planned later

These technologies will only be added when their corresponding feature is implemented.

| Technology | Purpose |
|---|---|
| **kotlin.test + JUnit 5** | Automated tests |
| **SLF4J + Logback** | Logging |
| **kotlinx.serialization** | Settings, plugin descriptors, and structured configuration |
| **LSP / JSON-RPC** | Language intelligence such as completion, diagnostics, definitions, and references |
| **Tree-sitter** | Incremental parsing and syntax structure |
| **JGit** | Git integration |
| **PTY / native terminal adapter** | Real interactive terminal support |

### Dependency rule

We do not add a framework merely because we expect to need it eventually.

The project should remain understandable and lightweight while it is still small.

---

# Architecture Direction

The eventual architecture is modular and extension-oriented.

```text
┌──────────────────────────────────────────────────────────────────┐
│                         IDE APPLICATION                          │
│                                                                  │
│  Main Window / Layout / Menus / Docking / Tabs / Notifications  │
└───────────────────────┬──────────────────────────────────────────┘
                        │
                        ▼
┌──────────────────────────────────────────────────────────────────┐
│                       WORKBENCH LAYER                            │
│                                                                  │
│  Workspace   Project   EditorArea   PanelHost   CommandRegistry  │
│  Selection   Navigation   Keymaps   Settings                     │
└───────────────────────┬──────────────────────────────────────────┘
                        │
                        ▼
┌──────────────────────────────────────────────────────────────────┐
│                         CORE PLATFORM                            │
│                                                                  │
│  DocumentManager       FileSystem/VFS      ProcessManager        │
│  EventBus              PluginManager       ServiceContainer      │
│  TaskScheduler         Persistence         Logging               │
└───────────────┬──────────────────────────────┬───────────────────┘
                │                              │
                ▼                              ▼
┌────────────────────────────┐   ┌───────────────────────────────┐
│       EDITOR ENGINE        │   │        PLUGIN SYSTEM          │
│                            │   │                               │
│ Document                   │   │ PluginDescriptor              │
│ TextBuffer                 │   │ PluginLoader                  │
│ Selection / Cursor         │   │ ExtensionPointRegistry        │
│ Undo / Redo                │   │ ServiceRegistry               │
│ EditorView                 │   │ Lifecycle / Dependencies      │
└─────────────┬──────────────┘   └───────────────┬───────────────┘
              │                                  │
              └──────────────┬───────────────────┘
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│                       FEATURE PLUGINS                            │
│                                                                  │
│  File Tree     Terminal      Search       Git        Tabs        │
│  Status Bar    Themes        Keymaps      Languages   Debugger   │
└───────────────────────┬──────────────────────────────────────────┘
                        │
                        ▼
┌──────────────────────────────────────────────────────────────────┐
│                      LANGUAGE TOOLING                            │
│                                                                  │
│        Tree-sitter / Parser          LSP Client                  │
│                │                        │                         │
│        Syntax / Structure       Language Servers                 │
│                                 Completion / Diagnostics         │
│                                 Definitions / References         │
└──────────────────────────────────────────────────────────────────┘
```

A critical dependency rule is:

```text
plugins ───────► platform-api
     │
     ├─────────► editor-api
     │
     └─────────► language-api

desktop-app ───► platform-core
             ├─► editor-ui
             └─► plugin-runtime

platform-core ─► platform-api
editor-ui ─────► editor-api
plugin-runtime ► plugin-api
              ► platform-api
```

The following dependencies should be treated as architectural smells:

```text
platform-core  ✗──► terminal plugin
platform-core  ✗──► file-tree plugin
platform-core  ✗──► Git plugin
platform-core  ✗──► language-specific plugins
```

---

# Milestone 0.0.1 — IDE Shell

The first milestone is intentionally limited.

We are **not** building a real editor, terminal, filesystem browser, or plugin system yet.

The goal is to build the basic desktop shell and prove that the layout feels right.

## Target UI

```text
┌─────────────────────────────────────────────────────────┐
│ File   Edit   View   Run                                │
├──────────────┬──────────────────────────────────────────┤
│              │ tab1.kt │ tab2.kt │ README.md            │
│ PROJECT      ├──────────────────────────────────────────┤
│              │                                          │
│ ▼ myproject  │                                          │
│   ▼ src      │               EDITOR                     │
│     main.kt  │                                          │
│     app.kt   │                                          │
│   build...   │                                          │
│              │                                          │
│              ├──────────────────────────────────────────┤
│              │ Terminal 1 │ Terminal 2 │ +              │
│              ├──────────────────────────────────────────┤
│              │                                          │
│              │               TERMINAL                   │
│              │                                          │
└──────────────┴──────────────────────────────────────────┘
```

## Initial UI areas

The application starts with four clearly separated areas:

1. **Top Menu**
   - File
   - Edit
   - View
   - Run

2. **Project File Tree**
   - Located on the left.
   - Initially contains static placeholder content.
   - Horizontally resizable.

3. **Editor Area**
   - Located in the main area.
   - Includes a tab row.
   - Initially contains placeholder content only.

4. **Terminal Area**
   - Located below the editor.
   - Includes terminal tabs.
   - Vertically resizable.

The UI hierarchy is:

```text
IDE Window
│
├── MenuBar
│
└── Workbench
    │
    ├── ProjectPanel
    │
    └── MainArea
        │
        ├── EditorArea
        │   ├── EditorTabs
        │   └── EditorContent
        │
        └── TerminalArea
            ├── TerminalTabs
            └── TerminalContent
```

---

# Resizable Layout

The shell contains two splitters.

## Project panel splitter

```text
┌──────────────┬───────────────────────────┐
│              │                           │
│ Project Tree │       Main Area           │
│              │                           │
└──────────────┴───────────────────────────┘
               ↔
             drag
```

The project panel should start with approximately:

```text
minimum: 160 dp
default: 240 dp
maximum: 500 dp
```

The width will be stored as UI state.

Conceptually:

```kotlin
var projectWidth by remember {
    mutableStateOf(240.dp)
}
```

## Terminal splitter

```text
┌──────────────────────────────────────────┐
│                                          │
│                 Editor                   │
│                                          │
├──────────────────────────────────────────┤
│                Terminal                  │
│                                          │
└──────────────────────────────────────────┘
                    ↕
                  drag
```

The terminal should start with approximately:

```text
minimum: 100 dp
default: 220 dp
maximum: about 60% of the available height
```

Its height will also be stored as UI state.

Conceptually:

```kotlin
var terminalHeight by remember {
    mutableStateOf(220.dp)
}
```

---

# Initial Project Structure

The first version should remain a single Gradle module.

```text
ink-ide/
│
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
│
└── src/
    ├── main/
    │   └── kotlin/
    │       └── dev/
    │           └── inkide/
    │               │
    │               ├── Main.kt
    │               │
    │               └── ui/
    │                   ├── IdeApplication.kt
    │                   ├── IdeTheme.kt
    │                   │
    │                   ├── workbench/
    │                   │   └── Workbench.kt
    │                   │
    │                   └── components/
    │                       ├── IdeMenuBar.kt
    │                       ├── ProjectPanel.kt
    │                       ├── EditorArea.kt
    │                       ├── TerminalArea.kt
    │                       └── Splitter.kt
    │
    └── test/
        └── kotlin/
```

We will move to multiple Gradle modules when the architectural boundaries are real enough to justify them.

---

# Initial Component Responsibilities

## `Main.kt`

`Main.kt` should remain extremely small.

```kotlin
fun main() = application {
    IdeApplication()
}
```

Its only responsibility is to launch the application.

## `IdeApplication`

Responsible for:

- Creating the desktop window.
- Setting the application title.
- Handling application exit.
- Applying the global theme.
- Hosting the menu and workbench.

Conceptually:

```kotlin
@Composable
fun ApplicationScope.IdeApplication() {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Ink IDE"
    ) {
        IdeTheme {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                IdeMenuBar()

                Workbench(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}
```

## `Workbench`

Responsible for the major IDE layout.

```text
Row

┌─────────────┬─┬─────────────────────────┐
│             │ │                         │
│ Project     │S│                         │
│ Panel       │P│       Main Area         │
│             │L│                         │
│             │I│                         │
│             │T│                         │
└─────────────┴─┴─────────────────────────┘
```

The splitter will be implemented as a reusable component because this interaction will be useful elsewhere in the IDE.

## Main Area

```text
┌────────────────────────────┐
│ Main.kt │ App.kt │ README  │
├────────────────────────────┤
│                            │
│          Editor            │
│                            │
├════════════════════════════┤
│ Terminal 1 │ Terminal 2 │+ │
├────────────────────────────┤
│                            │
│         Terminal           │
│                            │
└────────────────────────────┘
```

The editor consumes the flexible remaining space.

The terminal uses a controlled height.

---

# Placeholder Content

Milestone `0.0.1` uses fake content intentionally.

## Editor tabs

Example:

```text
Main.kt | App.kt | README.md
```

No real documents are opened yet.

## Terminal tabs

Example:

```text
Terminal 1 | Terminal 2 | +
```

No shell process exists yet.

## Project tree

Example:

```text
PROJECT

▼ ink-ide
  ▼ src
    ▼ main
      ▼ kotlin
          Main.kt
          IdeApplication.kt
    build.gradle.kts
    settings.gradle.kts
```

The tree is static Compose UI during this milestone.

Real filesystem scanning will be introduced later.

---

# UI State Rule

The UI should follow a one-way model:

```text
State
  ↓
Composable
  ↓
Pixels
```

UI components should not eventually become responsible for filesystem access, Git commands, terminals, or language servers.

For example, the future project panel should look conceptually like:

```kotlin
@Composable
fun ProjectPanel(
    state: ProjectPanelState,
    onAction: (ProjectAction) -> Unit
)
```

instead of having `ProjectPanel` directly crawl directories.

This separation will become increasingly important as the IDE grows.

---

# Initial Visual Theme

The visual direction is based on graphite, green, and orange.

Preliminary palette:

```text
Graphite        #2B2D30
Dark Graphite   #1E1F22
Light Graphite  #3A3D41
Green           #50C878
Orange          #E8903A
Light Text      #DFE1E5
Muted Text      #9DA1A8
```

The exact visual design is not final.

For milestone `0.0.1`, the priority is to make the following areas visually distinct:

- Menu
- Project panel
- Editor
- Terminal
- Tabs
- Splitters

---

# Out of Scope for 0.0.1

The following features are intentionally postponed:

```text
✗ Real text editing
✗ Syntax highlighting
✗ Filesystem scanning
✗ Opening projects
✗ File watching
✗ Actual terminal processes
✗ PTY integration
✗ Git integration
✗ Plugin runtime
✗ LSP
✗ Tree-sitter
✗ Settings
✗ Persistence
✗ Keyboard shortcuts
✗ Dragging tabs
✗ Multiple editor groups
```

The purpose of the first milestone is purely to establish the IDE shell.

---

# Definition of Done — 0.0.1

Running:

```bash
./gradlew run
```

should open a desktop IDE window containing:

- Top menu
- Project panel
- Editor tabs
- Editor area
- Terminal tabs
- Terminal area
- Draggable vertical splitter between project panel and main area
- Draggable horizontal splitter between editor and terminal
- Horizontally resizable project panel
- Vertically resizable terminal
- Initial graphite / green / orange theme
- Correct behavior when resizing the outer operating-system window

The editor area should consume the flexible remaining space without breaking the layout.

---

# Planned Milestones

```text
0.0.1
IDE shell
    │
    ▼
0.0.2
Workspace + Document model
    │
    ▼
0.0.3
Real filesystem project tree
    │
    ▼
0.0.4
Basic text editor
    │
    ▼
0.0.5
Commands + keybindings
    │
    ▼
0.0.6
Plugin API
    │
    ▼
0.0.7
Turn file tree into first bundled plugin
    │
    ▼
0.0.8
Terminal + PTY
    │
    ▼
0.0.9
Language API
    │
    ▼
0.1.0
First LSP integration
```

A particularly important milestone is `0.0.7`.

At that point, the already-working project tree will be extracted from the application and converted into the first bundled plugin.

That milestone will test whether the IDE is genuinely becoming a plugin-oriented platform rather than simply a monolithic desktop application with extension support added later.

---

# Long-Term Direction

The project should eventually provide a small, understandable platform with:

- Workspace and project abstractions
- Documents separated from editor views
- Command registry and keybindings
- Service container
- Event system
- Plugin lifecycle
- Extension points
- File system abstraction
- Process management
- Editor engine
- Terminal integration
- Git integration
- Language plugins
- LSP support
- Tree-sitter-based syntax structure
- Themes and UI customization

The project should remain modular enough that features such as the terminal, Git integration, file tree, themes, and language support can exist as plugins rather than becoming permanent dependencies of the core.

---

# Guiding Principle

When deciding whether something belongs in the platform core, ask:

> **Could this reasonably be implemented as a plugin?**

If the answer is yes, prefer exposing the platform capability necessary for the plugin rather than putting the feature itself into the core.
