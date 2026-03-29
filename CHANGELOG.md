# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- DynamicKeybinds as a dependency
- Noetic Bookshelf with Dynamic Keybinds
    - Generates a dimension & coordinate derived 6 character ID
    - Always within ambit
    - Stores a single list
    - Creates a new keybind tied to the specific shelf
        - Currently just prints debug information about the shelf
        - will eventually execute the stored list as if the player used hermes' gambit
- Noetic Write (To Be Renamed)
    - Writes to an empty noetic bookshelf
        - Also writes to an empty akashic bookshelf, which is how you create new noetic shelves
- Noetic Read (To Be Renamed)
    - Reads from a noetic bookshelf
- Noetic Append (To Be Renamed)
    - Appends new data to the end of a non-empty noetic shelf
- Noetic Eraste (To Be Renamed)
    - Erases a noetic bookshelf to allow it to be re-written to

## [0.1.4] - 2026-03-25

### Added
- Proper changelog
- Initial public release for version 0.1.4.

[Unreleased]: https://github.com/MoonlitJolteon/hex-keys-addon/compare/v0.1.4...HEAD
[0.1.4]: https://github.com/MoonlitJolteon/hex-keys-addon/releases/tag/v0.1.4
