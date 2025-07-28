// release.config.js
var config = require('semantic-release-preconfigured-conventional-commits');
config.plugins.push(
    ["@semantic-release/exec", {
        "prepareCmd": "./gradlew build dokkaHtml -x test",
    }],
    "@semantic-release/github",
    ["@semantic-release/git", {
        "assets": ["CHANGELOG.md", "gradle.properties", "build.gradle.kts"],
        "message": "chore(release): ${nextRelease.version} [skip ci]\n\n${nextRelease.notes}"
    }]
)
module.exports = config