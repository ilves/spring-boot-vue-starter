// For authoring Nightwatch tests, see
// http://nightwatchjs.org/guide#usage

module.exports = {
  'default e2e tests': function (browser) {
    // automatically uses dev Server port from /config.index.js
    // default: http://localhost:8080
    // see nightwatch.conf.js
    const devServer = browser.globals.devServerURL

    console.log(devServer + '/#/users');
    browser
      .url(devServer + '/#/users')
      .waitForElementVisible('.table', 5000)
    browser.useXpath().click('//a[text()="Add new user"]')
    browser.end()
  }
}
