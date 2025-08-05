module.exports = {
    branches: ['main'],
    plugins: [
        '@semantic-release/commit-analyzer',
        '@semantic-release/release-notes-generator',
        '@semantic-release/changelog',
        [
            '@semantic-release/github',
            {
                assets: [
                    { path: 'dist/native-ubuntu-latest/*', label: 'Linux Binary' },
                    { path: 'dist/native-macos-latest/*', label: 'macOS ARM64 Binary' },
                    { path: 'dist/native-windows-latest/*', label: 'Windows Binary' }
                ]
            }
        ],
        [
            '@semantic-release/git',
            {
                assets: ['CHANGELOG.md', 'gradle.properties'],
                message: 'chore(release): ${nextRelease.version} [skip ci]\n\n${nextRelease.notes}'
            }
        ]
    ]
}