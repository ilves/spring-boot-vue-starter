let path = require('path')

module.exports = {
  src: path.resolve(__dirname, '../src/main/vuejs/'),
  resources: path.resolve(__dirname, '../src/main/resources/'),
  build: {
    env: require('./prod.env'),
    index: path.resolve(__dirname, '../src/main/resources/static/dist/index.html'),
    assetsRoot: path.resolve(__dirname, '../src/main/resources/static'),
    assetsSubDirectory: 'dist',
    assetsPublicPath: '/',
    productionSourceMap: true,
    productionGzip: false,
    productionGzipExtensions: ['js', 'css'],
    bundleAnalyzerReport: process.env.npm_config_report
  },
  dev: {
    env: require('./dev.env'),
    port: 8000,
    autoOpenBrowser: false,
    assetsSubDirectory: 'static',
    assetsPublicPath: '/',
    proxyTable: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    },
    cssSourceMap: false
  }
}
