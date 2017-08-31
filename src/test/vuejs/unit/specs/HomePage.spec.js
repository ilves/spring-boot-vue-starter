import Vue from 'vue'
import HomePage from './../../../../main/vuejs/pages/HomePage'

describe('HomePage.vue', () => {
  it('should render correct contents', () => {
    const Constructor = Vue.extend(HomePage)
    const vm = new Constructor().$mount()
    expect(vm.$el.querySelector('.marketing h1').textContent)
      .to.equal('Features')
  })
})
