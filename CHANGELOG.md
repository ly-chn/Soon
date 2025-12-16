<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# IntelliJ Platform Plugin Template Changelog

## [Unreleased]

### Added

- Add `platformBundledModules` to `gradle.properties` along with `bundledModules()` helper to the Gradle build file

### Changed

- Dependencies - upgrade `org.jetbrains.intellij.platform` to `2.7.1`

### Fixed

- GitHub: Fixed the missing `$RELEASE_NOTE ` parent directory in the Release workflow

## [2.3.0] - 2025-08-09

### Added

- Added `.DS_Store` directory to `.gitignore`

### Changed

- GitHub Actions: simplify changelog handling
- Dependencies - upgrade `org.jetbrains.changelog` to `2.4.0`

[Unreleased]: https://github.com/JetBrains/intellij-platform-plugin-template/compare/v2.3.0...HEAD
[0.0.1]: https://github.com/JetBrains/intellij-platform-plugin-template/commits/v0.0.1
