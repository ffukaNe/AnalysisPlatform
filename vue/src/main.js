import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
// ElementUI
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
// 自己的CSS
import './assets/css/global.css'

Vue.config.productionTip = false
Vue.use(ElementUI,{size:'small'});
Vue.prototype.$host = 'http://localhost:9876/' // 服务器路径常量

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')






