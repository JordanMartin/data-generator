const path = require('path');
const MonacoWebpackPlugin = require('monaco-editor-webpack-plugin');

module.exports = {
    plugins: [
        new MonacoWebpackPlugin({
            languages: ['yaml', 'json', 'xml', 'sql', 'csv']
        })
    ]
};
