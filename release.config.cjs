module.exports = {
    branches: ['*'],
    plugins: [
        '@semantic-release/commit-analyzer',
        '@semantic-release/release-notes-generator',
        '@semantic-release/changelog',
        '@semantic-release/git',
        [
            '@semantic-release/github',
            {
                assets: [
                    { path: 'dist/native-ubuntu-latest/keyla-linux', label: 'keyla-linux' },
                    { path: 'dist/native-macos-latest/keyla-macos', label: 'keyla-macos' },
                    { path: 'dist/native-macos-arm64/keyla-macos-arm64', label: 'keyla-macos-arm64' },
                    { path: 'dist/native-windows-latest/keyla-windows.exe', label: 'keyla-windows.exe' }
                ]
            }
        ]
    ]
}
