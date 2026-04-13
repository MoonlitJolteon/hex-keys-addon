# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.5] - 2026-04-13

### Added
- DynamicKeybinds as a dependency
- Noetic Bookshelf with Dynamic Keybinds
    - Can only be created in the mindscape library
    - Generates a dimension & coordinate derived 6 character ID
        - I hadn't added the mindscape limit when I set this up (^^ゞ
    - Always within ambit
    - Stores a single list
    - Creates a new keybind tied to the specific shelf
        - Defaults to 'Not Bound'
        - When pressed, will execute the the list of iotas stored in the shelf as if you staff casted hermes' gambit
    - Unbreakable when non-empty
    - Drops an akashic bookshelf when broken
- Noetic Bookshelf Iota type
    - All patterns that reference a noetic bookshelf can use either a vector position or a noetic bookshelf iota
- Get Noetic Bookshelf pattern to get a reference to a noetic bookshelf
- Noetic Write (To Be Renamed)
    - This is a great spell
    - Writes to an empty noetic bookshelf
        - Also writes to an empty akashic bookshelf, which is how you create new noetic shelves
- Noetic Read (To Be Renamed)
    - Reads from a noetic bookshelf
- Noetic Concat (To Be Renamed)
    - Concat new data to the end of a non-empty noetic shelf
- Noetic Eraste (To Be Renamed)
    - Erases a noetic bookshelf to allow it to be re-written to

### TODO
- Write better documentation (Requires some lore to be written first)

## [0.1.4] - 2026-03-25

### Added
- Proper changelog
- Initial public release for version 0.1.4.

[Unreleased]: https://github.com/MoonlitJolteon/hex-keys-addon/compare/v0.1.4...HEAD
[0.1.4]: https://github.com/MoonlitJolteon/hex-keys-addon/releases/tag/v0.1.4
