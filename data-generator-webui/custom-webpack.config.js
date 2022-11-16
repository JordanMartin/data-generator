const path = require('path');
const MonacoWebpackPlugin = require('monaco-editor-webpack-plugin');

module.exports = {
  module: {
    rules: [
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader']
      }
    ]
  },
    plugins: [
        new MonacoWebpackPlugin({
            languages: ['yaml', 'json', 'xml', 'sql', 'csv']
        })
    ]
};

