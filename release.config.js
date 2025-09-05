module.exports = {
    branches: ['release'],
    plugins: [
        '@semantic-release/commit-analyzer',
        '@semantic-release/release-notes-generator',
        '@semantic-release/changelog',
        [
            '@semantic-release/github',
            {
                assets: [
                    { path: 'dist/native-Linux/*', label: 'Linux Binary' },
                    { path: 'dist/native-macOS/*', label: 'macOS ARM64 Binary' },
                    { path: 'dist/native-Windows/*', label: 'Windows Binary' }
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
