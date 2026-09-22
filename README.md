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
0.0.3.5
Project lifecycle & session restore
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
