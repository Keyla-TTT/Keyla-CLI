var config = require('semantic-release-preconfigured-conventional-commits');
config.plugins.push(
    ["@semantic-release/github", {
        "assets": [
            {"path": "artifacts/*.jar", "label": "JAR binaries"},
            {"path": "artifacts/docs", "label": "API Documentation"}
        ]
    }],
    ["@semantic-release/git", {
        "assets": ["CHANGELOG.md", "gradle.properties"],
        "message": "chore(release): ${nextRelease.version} [skip ci]\n\n${nextRelease.notes}"
    }]
)
module.exports = config