import tab from './tab'
import auth from './auth'
import modal from './modal'
//vue3中Vue.prototype已经被废弃，所以需要变更为app.config.globalProperties
export default {
  install(app) {
    // 页签操作
    app.config.globalProperties.$tab = tab
    // 认证对象
    app.config.globalProperties.$auth = auth
    // 模态框对象
    app.config.globalProperties.$modal = modal
  }
}
